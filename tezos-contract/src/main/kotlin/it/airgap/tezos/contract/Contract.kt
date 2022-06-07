package it.airgap.tezos.contract

import it.airgap.tezos.contract.entrypoint.ContractEntrypoint
import it.airgap.tezos.contract.internal.context.TezosContractContext.normalized
import it.airgap.tezos.contract.internal.contractModule
import it.airgap.tezos.contract.internal.utils.failWithContractException
import it.airgap.tezos.contract.storage.ContractStorage
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing

/**
 * The main entry point to interact with Tezos contracts.
 *
 * Fo a sample usage, see:
 *  * `samples/src/test/kotlin/contract/Contract/ContractSamples.Usage`
 *  * `samples/src/test/kotlin/Rpc/RpcSamples`
 *
 * @property address The contract's address.
 */
public class Contract internal constructor(
    public val address: ContractHash,
    private val contract: Block.Context.Contracts.Contract,
    private val contractStorageFactory: ContractStorage.Factory,
    private val contractEntrypointFactory: ContractEntrypoint.Factory,
    private val scriptToContractCodeConverter: Converter<Script, ContractCode>,
    private val michelineNormalizer: Normalizer<MichelineNode>,
) {
    private val codeCached: Cached<ContractCode> = Cached { headers -> contract.script.getNormalized(headers).toContractCode() }

    /**
     * The contract's storage handler.
     */
    public val storage: ContractStorage by lazy { contractStorageFactory.create(codeCached) }

    /**
     * Creates the contract's entrypoint handler specified by its [name].
     */
    public fun entrypoint(name: String = Entrypoint.Default.value): ContractEntrypoint =
        contractEntrypointFactory.create(codeCached, name)

    /**
     * Fetches the contract's code from the node. Can be configured with optional [HTTP headers][headers] to customize the request.
     */
    public suspend fun code(headers: List<HttpHeader> = emptyList()): ContractCode = codeCached.get(headers)

    private suspend fun Block.Context.Contracts.Contract.Script.getNormalized(headers: List<HttpHeader>): Script =
        try {
            normalized.post(RpcScriptParsing.OptimizedLegacy, headers).script
        } catch (e: Exception) {
            get(headers).script?.normalized()
        } ?: failWithScriptNotFound()

    private fun Script.normalized(): Script =
        Script(
            code.normalized(michelineNormalizer),
            storage.normalized(michelineNormalizer),
        )

    private fun Script.toContractCode(): ContractCode {
        return scriptToContractCodeConverter.convert(this)
    }

    private fun failWithScriptNotFound(): Nothing = failWithContractException("Script not found for contract ${address.base58}.")
}

/**
 * Creates a new [Contract] instance for the contract with the given [address] on the node specified by the [nodeUrl].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Contract(nodeUrl: String, address: ContractHash, tezos: Tezos = Tezos.Default): Contract =
    tezos.contractModule.dependencyRegistry.contract(nodeUrl, address)