package it.airgap.tezos.contract

import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.toMichelson
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractEntrypointsResponse
import it.airgap.tezos.rpc.active.block.GetContractStorageResponse
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached
import it.airgap.tezos.rpc.type.contract.RpcScript
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import kotlin.reflect.KClass

public class Contract(public val address: ContractHash, baseRpc: TezosRpc) {
    private val rpc: Block.Context.Contracts.Contract = baseRpc.chains.main.blocks.head.context.contracts(address)

    private val scriptCached: Cached<RpcScript?> = Cached { headers -> rpc.script.normalized.post(RpcScriptParsing.OptimizedLegacy, headers).script }
    private val storageCached: Cached<MetaContractStorage> = Cached { headers -> rpc.storage.get(headers).toMeta() }
    private val entrypointsCached: Cached<Map<String, MetaContractEntrypoint>> = Cached { headers -> rpc.entrypoints.get(headers).toMeta() }

    public suspend fun storage(headers: List<HttpHeader> = emptyList()): ContractStorage = ContractStorage(storageCached.get(headers), rpc)
    public suspend fun entrypoint(name: String, headers: List<HttpHeader> = emptyList()): ContractEntrypoint = ContractEntrypoint(name, entrypointsCached.get(headers)[name])

    public suspend fun code(headers: List<HttpHeader> = emptyList()): ContractCode {
        val contractCode = scriptCached.get(headers)?.code as? MichelineSequence ?: failWithScriptNotFound()

        // TODO: inject converter
        val parameter = contractCode.nodes[0].toMichelson().assertType<MichelsonType.Parameter>()
        val storage = contractCode.nodes[1].toMichelson().assertType<MichelsonType.Storage>()
        val code = contractCode.nodes[2].toMichelson().assertType<MichelsonType.Code>()

        return ContractCode(parameter, storage, code)
    }

    private inline fun <reified T : Michelson> Michelson.assertType(): T {
        if (this !is T) failWithInvalidMichelsonType(T::class, this::class)
        return this
    }

    private fun GetContractStorageResponse.toMeta(): MetaContractStorage {
        TODO()
    }

    private fun GetContractEntrypointsResponse.toMeta(): Map<String, MetaContractEntrypoint> {
        TODO()
    }

    // TODO: better error handling
    private fun failWithInvalidMichelsonType(expected: KClass<out Michelson>, actual: KClass<out Michelson>): Nothing =
        throw Exception("Invalid Michelson type, expected `$expected` but got `$actual`.")

    private fun failWithScriptNotFound(): Nothing = throw Exception("Script not found for contract ${address.base58}.")
}