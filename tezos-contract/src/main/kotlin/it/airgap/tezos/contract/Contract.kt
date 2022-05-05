package it.airgap.tezos.contract

import it.airgap.tezos.contract.entrypoint.ContractEntrypoint
import it.airgap.tezos.contract.entrypoint.MetaContractEntrypoint
import it.airgap.tezos.contract.internal.converter.MichelineToStorageEntryConverter
import it.airgap.tezos.contract.internal.storage.MetaContractStorage
import it.airgap.tezos.contract.storage.ContractStorage
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.michelson.internal.converter.MichelineToNormalizedConverter
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.normalized
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractEntrypointsResponse
import it.airgap.tezos.rpc.active.block.GetContractNormalizedScriptResponse
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import kotlin.reflect.KClass

public class Contract internal constructor(
    public val address: ContractHash,
    private val rpc: Block,
    private val michelineToNormalizedConverter: MichelineToNormalizedConverter,
    private val michelineToStorageEntryConverter: MichelineToStorageEntryConverter,
) {
    private val contractRpc: Block.Context.Contracts.Contract
        get() = rpc.context.contracts(address)

    private val codeCached: Cached<ContractCode> = Cached { headers -> contractRpc.script.normalized.post(RpcScriptParsing.OptimizedLegacy, headers).toContractCode() }
    private val metaStorageCached: Cached<MetaContractStorage> = Cached { headers -> codeCached.get(headers).toMetaContractStorage() }
    private val metaEntrypointsCached: Cached<Map<String, MetaContractEntrypoint>> = Cached { headers -> contractRpc.entrypoints.get(headers).toMetaContractEntrypoint() }

    public val storage: ContractStorage by lazy { ContractStorage(metaStorageCached, contractRpc) }
    public suspend fun entrypoint(name: String, headers: List<HttpHeader> = emptyList()): ContractEntrypoint = ContractEntrypoint(name, metaEntrypointsCached.get(headers)[name])
    public suspend fun code(headers: List<HttpHeader> = emptyList()): ContractCode = codeCached.get(headers)

    private fun GetContractNormalizedScriptResponse.toContractCode(): ContractCode {
        val script = script ?: failWithScriptNotFound()
        val contractCode = script.code as? MichelineSequence ?: failWithInvalidMichelineType(MichelineSequence::class, script.code::class)
        if (contractCode.nodes.size != 3) failWithUnknownCodeType()

        val parameter = contractCode.nodes[0]
        val storage = contractCode.nodes[1]
        val code = contractCode.nodes[2]

        return ContractCode(parameter, storage, code)
    }

    private fun ContractCode.toMetaContractStorage(): MetaContractStorage {
        if (storage !is MichelinePrimitiveApplication || storage.args.size != 1) failWithUnknownStorageType()
        val type = storage.args.first().normalized(michelineToNormalizedConverter)

        return MetaContractStorage(type, michelineToStorageEntryConverter)
    }

    private fun GetContractEntrypointsResponse.toMetaContractEntrypoint(): Map<String, MetaContractEntrypoint> {
        TODO()
    }

    // TODO: better error handling
    private fun failWithInvalidMichelineType(expected: KClass<out MichelineNode>, actual: KClass<out MichelineNode>): Nothing =
        throw Exception("Invalid Micheline type, expected `$expected` but got `$actual`.")

    private fun failWithScriptNotFound(): Nothing = throw Exception("Script not found for contract ${address.base58}.")
    private fun failWithUnknownCodeType(): Nothing = throw Exception("Unknown contract code type.")
    private fun failWithUnknownStorageType(): Nothing = throw Exception("Unknown contract storage type.")
}