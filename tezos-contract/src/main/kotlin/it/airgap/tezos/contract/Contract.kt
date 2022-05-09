package it.airgap.tezos.contract

import it.airgap.tezos.contract.entrypoint.ContractEntrypoint
import it.airgap.tezos.contract.entrypoint.MetaContractEntrypoint
import it.airgap.tezos.contract.internal.converter.MichelineToStorageEntryConverter
import it.airgap.tezos.contract.internal.storage.MetaContractStorage
import it.airgap.tezos.contract.storage.ContractStorage
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.internal.converter.MichelineToNormalizedConverter
import it.airgap.tezos.michelson.isPrim
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.normalized
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractEntrypointsResponse
import it.airgap.tezos.rpc.active.block.GetContractNormalizedScriptResponse
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import kotlin.reflect.KClass

public class Contract internal constructor(
    public val address: ContractHash,
    private val rpc: TezosRpc,
    private val michelineToNormalizedConverter: MichelineToNormalizedConverter,
    private val michelineToStorageEntryConverter: MichelineToStorageEntryConverter,
) {
    private val TezosRpc.block: Block
        get() = chains.main.blocks.head

    private val TezosRpc.contract: Block.Context.Contracts.Contract
        get() = block.context.contracts(address)

    private val codeCached: Cached<ContractCode> = Cached { headers -> rpc.contract.script.normalized.post(RpcScriptParsing.OptimizedLegacy, headers).toContractCode() }
    private val metaStorageCached: Cached<MetaContractStorage> = Cached { headers -> codeCached.get(headers).toMetaContractStorage() }
    private val metaEntrypointsCached: Cached<Map<String, MetaContractEntrypoint>> = Cached { headers -> rpc.contract.entrypoints.get(headers).toMetaContractEntrypoint(headers) }

    public val storage: ContractStorage by lazy { ContractStorage(metaStorageCached, rpc.contract) }
    public fun entrypoint(name: String = Entrypoint.Default.value): ContractEntrypoint = ContractEntrypoint(name, address, rpc.block, rpc, metaEntrypointsCached.map { it[name] })

    public suspend fun code(headers: List<HttpHeader> = emptyList()): ContractCode = codeCached.get(headers)

    private fun GetContractNormalizedScriptResponse.toContractCode(): ContractCode {
        val script = script ?: failWithScriptNotFound()
        val contractCode = script.code as? MichelineSequence ?: failWithInvalidMichelineType(MichelineSequence::class, script.code::class)
        if (contractCode.nodes.size != 3) failWithUnknownCodeType()

        val parameter = contractCode.nodes[0].takeIf { it.isPrim(MichelsonType.Parameter) } ?: failWithUnknownCodeType()
        val storage = contractCode.nodes[1].takeIf { it.isPrim(MichelsonType.Storage) } ?: failWithUnknownCodeType()
        val code = contractCode.nodes[2].takeIf { it.isPrim(MichelsonType.Code) } ?: failWithUnknownCodeType()

        return ContractCode(parameter, storage, code)
    }

    private fun ContractCode.toMetaContractStorage(): MetaContractStorage {
        if (storage !is MichelinePrimitiveApplication || storage.args.size != 1) failWithUnknownStorageType()
        val type = storage.args.first().normalized(michelineToNormalizedConverter)

        return MetaContractStorage(type, michelineToStorageEntryConverter)
    }

    private suspend fun GetContractEntrypointsResponse.toMetaContractEntrypoint(headers: List<HttpHeader>): Map<String, MetaContractEntrypoint> {
        val defaultEntrypoint = entrypoints[Entrypoint.Default.value] ?: run {
            val parameter = codeCached.get(headers).parameter.normalized(michelineToNormalizedConverter)
            if (parameter is MichelinePrimitiveApplication && parameter.args.size == 1) parameter.args.first() else failWithUnknownCodeType()
        }
        val entrypoints = entrypoints + Pair(Entrypoint.Default.value, defaultEntrypoint)

        return entrypoints.mapValues { MetaContractEntrypoint(it.value) }
    }

    // TODO: better error handling
    private fun failWithInvalidMichelineType(expected: KClass<out MichelineNode>, actual: KClass<out MichelineNode>): Nothing =
        throw Exception("Invalid Micheline type, expected `$expected` but got `$actual`.")

    private fun failWithScriptNotFound(): Nothing = throw Exception("Script not found for contract ${address.base58}.")
    private fun failWithUnknownCodeType(): Nothing = throw Exception("Unknown contract code type.")
    private fun failWithUnknownStorageType(): Nothing = throw Exception("Unknown contract storage type.")
}