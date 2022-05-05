package it.airgap.tezos.contract

import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.internal.converter.MichelineToCompactStringConverter
import it.airgap.tezos.michelson.internal.converter.MichelineToMichelsonConverter
import it.airgap.tezos.michelson.internal.converter.MichelineToNormalizedConverter
import it.airgap.tezos.michelson.internal.packer.MichelinePacker
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.normalized
import it.airgap.tezos.michelson.toMichelson
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractEntrypointsResponse
import it.airgap.tezos.rpc.active.block.GetContractNormalizedScriptResponse
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import kotlin.reflect.KClass

public class Contract(
    public val address: ContractHash,
    private val baseRpc: TezosRpc,
    private val encodedBytesCoder: EncodedBytesCoder,
    private val michelinePacker: MichelinePacker,
    private val michelineToMichelsonConverter: MichelineToMichelsonConverter,
    private val michelineToNormalizedConverter: MichelineToNormalizedConverter,
    private val michelineToCompactStringConverter: MichelineToCompactStringConverter,
) {
    private val blockRpc: Block
        get() = baseRpc.chains.main.blocks.head

    private val contractRpc: Block.Context.Contracts.Contract
        get() = blockRpc.context.contracts(address)

    private val rpcCodeCached: Cached<RpcContractCode> = Cached { headers -> contractRpc.script.normalized.post(RpcScriptParsing.OptimizedLegacy, headers).toRpcContractCode() }
    private val codeCached: Cached<ContractCode> = Cached { headers -> rpcCodeCached.get(headers).toContractCode() }
    private val metaStorageCached: Cached<MetaContractStorage> = Cached { headers -> rpcCodeCached.get(headers).toMetaContractStorage() }
    private val metaEntrypointsCached: Cached<Map<String, MetaContractEntrypoint>> = Cached { headers -> contractRpc.entrypoints.get(headers).toMetaContractEntrypoint() }

    public val storage: ContractStorage by lazy { ContractStorage(metaStorageCached, contractRpc) }

    public suspend fun entrypoint(name: String, headers: List<HttpHeader> = emptyList()): ContractEntrypoint = ContractEntrypoint(name, metaEntrypointsCached.get(headers)[name])

    public suspend fun code(headers: List<HttpHeader> = emptyList()): ContractCode = codeCached.get(headers)

    private inline fun <reified T : Michelson> Michelson.assertType(): T {
        if (this !is T) failWithInvalidMichelsonType(T::class, this::class)
        return this
    }

    private fun GetContractNormalizedScriptResponse.toRpcContractCode(): RpcContractCode {
        val script = script ?: failWithScriptNotFound()
        val contractCode = script.code as? MichelineSequence ?: failWithInvalidMichelineType(MichelineSequence::class, script.code::class)
        if (contractCode.nodes.size != 3) failWithUnknownCodeType()

        val parameter = contractCode.nodes[0]
        val storage = contractCode.nodes[1]
        val code = contractCode.nodes[2]

        return RpcContractCode(parameter, storage, code)
    }

    private fun RpcContractCode.toContractCode(): ContractCode {
        val parameter = parameter.toMichelson(michelineToMichelsonConverter).assertType<MichelsonType.Parameter>()
        val storage = storage.toMichelson(michelineToMichelsonConverter).assertType<MichelsonType.Storage>()
        val code = code.toMichelson(michelineToMichelsonConverter).assertType<MichelsonType.Code>()

        return ContractCode(parameter, storage, code)
    }

    private fun RpcContractCode.toMetaContractStorage(): MetaContractStorage {
        if (storage !is MichelinePrimitiveApplication || storage.args.size != 1) failWithUnknownStorageType()
        val type = storage.args.first().normalized(michelineToNormalizedConverter)

        return MetaContractStorage(type, blockRpc, encodedBytesCoder, michelinePacker, michelineToCompactStringConverter)
    }

    private fun GetContractEntrypointsResponse.toMetaContractEntrypoint(): Map<String, MetaContractEntrypoint> {
        TODO()
    }

    // TODO: better error handling
    private fun failWithInvalidMichelsonType(expected: KClass<out Michelson>, actual: KClass<out Michelson>): Nothing =
        throw Exception("Invalid Michelson type, expected `$expected` but got `$actual`.")

    private fun failWithInvalidMichelineType(expected: KClass<out MichelineNode>, actual: KClass<out MichelineNode>): Nothing =
        throw Exception("Invalid Micheline type, expected `$expected` but got `$actual`.")

    private fun failWithScriptNotFound(): Nothing = throw Exception("Script not found for contract ${address.base58}.")
    private fun failWithUnknownCodeType(): Nothing = throw Exception("Unknown contract code type.")
    private fun failWithUnknownStorageType(): Nothing = throw Exception("Unknown contract storage type.")
}