package it.airgap.tezos.contract

import it.airgap.tezos.contract.entrypoint.ContractEntrypoint
import it.airgap.tezos.contract.internal.contractModule
import it.airgap.tezos.contract.internal.utils.failWithContractException
import it.airgap.tezos.contract.storage.ContractStorage
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractNormalizedScriptResponse
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached
import it.airgap.tezos.rpc.type.contract.RpcScript
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing

public class Contract internal constructor(
    public val address: ContractHash,
    private val contract: Block.Context.Contracts.Contract,
    private val contractStorageFactory: ContractStorage.Factory,
    private val contractEntrypointFactory: ContractEntrypoint.Factory,
    private val rpcScriptToContractCodeConverter: Converter<RpcScript, ContractCode>,
) {
    private val codeCached: Cached<ContractCode> = Cached { headers -> contract.script.normalized.post(RpcScriptParsing.OptimizedLegacy, headers).toContractCode() }

    public val storage: ContractStorage by lazy { contractStorageFactory.create(codeCached) }
    public fun entrypoint(name: String = Entrypoint.Default.value): ContractEntrypoint = contractEntrypointFactory.create(codeCached, name)

    public suspend fun code(headers: List<HttpHeader> = emptyList()): ContractCode = codeCached.get(headers)

    private fun GetContractNormalizedScriptResponse.toContractCode(): ContractCode {
        val script = script ?: failWithScriptNotFound()
        return rpcScriptToContractCodeConverter.convert(script)
    }

    private fun failWithScriptNotFound(): Nothing = failWithContractException("Script not found for contract ${address.base58}.")
}

public fun Contract(nodeUrl: String, address: ContractHash, tezos: Tezos = Tezos.Default): Contract =
    tezos.contractModule.dependencyRegistry.contract(nodeUrl, address)