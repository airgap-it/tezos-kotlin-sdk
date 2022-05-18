package it.airgap.tezos.contract.internal.di

import it.airgap.tezos.contract.Contract
import it.airgap.tezos.contract.entrypoint.ContractEntrypointArgument
import it.airgap.tezos.contract.internal.converter.EntrypointArgumentToMichelineConverter
import it.airgap.tezos.contract.internal.converter.MichelineToStorageEntryConverter
import it.airgap.tezos.contract.internal.converter.TypedConverter
import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.utils.getOrPutWeak
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.michelson.internal.di.MichelsonDependencyRegistry
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.internal.di.RpcDependencyRegistry
import java.lang.ref.WeakReference

@InternalTezosSdkApi
public class ContractDependencyRegistry internal constructor(
    private val core: CoreDependencyRegistry,
    private val michelson: MichelsonDependencyRegistry,
    private val rpc: RpcDependencyRegistry,
) {

    // -- contract --

    private val contracts: MutableMap<String, WeakReference<Contract>> = mutableMapOf()
    public fun contract(nodeUrl: String, address: ContractHash): Contract = contracts.getOrPutWeak("${nodeUrl}_${address}") {
        val rpc = rpc.tezosRpc(nodeUrl)

        Contract(
            address,
            rpc,
            michelson.michelineNormalizer,
            michelineToStorageEntryConverter(rpc.chains.main.blocks.head),
            entrypointArgumentToMichelineConverter,
        )
    }

    // -- converter --

    public fun michelineToStorageEntryConverter(block: Block): TypedConverter<MichelineNode, ContractStorageEntry> =
        MichelineToStorageEntryConverter(
            block,
            core.encodedBytesCoder,
            michelson.michelinePacker,
            michelson.michelineToCompactStringConverter,
            michelson.stringToMichelsonPrimConverter,
        )

    public val entrypointArgumentToMichelineConverter: TypedConverter<ContractEntrypointArgument, MichelineNode> by lazy {
        EntrypointArgumentToMichelineConverter(
            michelson.michelineToCompactStringConverter,
            michelson.stringToMichelsonPrimConverter,
        )
    }
}