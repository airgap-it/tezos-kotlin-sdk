package it.airgap.tezos.contract.internal.di

import it.airgap.tezos.contract.Contract
import it.airgap.tezos.contract.entrypoint.ContractEntrypoint
import it.airgap.tezos.contract.entrypoint.ContractEntrypointArgument
import it.airgap.tezos.contract.internal.converter.EntrypointArgumentToMichelineConverter
import it.airgap.tezos.contract.internal.converter.MichelineToStorageEntryConverter
import it.airgap.tezos.contract.internal.converter.RpcScriptToContractCodeConverter
import it.airgap.tezos.contract.internal.converter.TypedConverter
import it.airgap.tezos.contract.internal.entrypoint.MetaContractEntrypoint
import it.airgap.tezos.contract.internal.storage.MetaContractStorage
import it.airgap.tezos.contract.storage.ContractStorage
import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.utils.getOrPutWeak
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.michelson.internal.di.MichelsonDependencyRegistry
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.internal.di.RpcDependencyRegistry
import it.airgap.tezos.rpc.type.contract.RpcScript
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
        val block = rpc.chains.main.blocks.head
        val contract = block.context.contracts(address)

        Contract(
            address,
            contract,
            contractStorageFactory(block, contract),
            contractEntrypointFactory(address, block, contract, rpc),
            rpcStrictToContractCodeConverter,
        )
    }

    // -- storage --

    private fun contractStorageFactory(block: Block, contract: Block.Context.Contracts.Contract): ContractStorage.Factory =
        ContractStorage.Factory(
            metaContractStorageFactory(block),
            contract,
            michelson.michelineNormalizer,
        )

    private fun metaContractStorageFactory(block: Block): MetaContractStorage.Factory =
        MetaContractStorage.Factory(michelineToStorageEntryConverter(block))

    // -- entrypoint --

    private fun contractEntrypointFactory(
        address: ContractHash,
        block: Block,
        contract: Block.Context.Contracts.Contract,
        rpc: TezosRpc,
    ): ContractEntrypoint.Factory =
        ContractEntrypoint.Factory(
            metaContractEntrypointFactory,
            address,
            block,
            contract,
            rpc,
            michelson.michelineNormalizer,
        )

    private val metaContractEntrypointFactory: MetaContractEntrypoint.Factory by lazy { MetaContractEntrypoint.Factory(entrypointArgumentToMichelineConverter) }

    // -- converter --

    private fun michelineToStorageEntryConverter(block: Block): TypedConverter<MichelineNode, ContractStorageEntry> =
        MichelineToStorageEntryConverter(
            block,
            core.encodedBytesCoder,
            michelson.michelinePacker,
            michelson.michelineToCompactStringConverter,
            michelson.stringToMichelsonPrimConverter,
        )

    private val entrypointArgumentToMichelineConverter: TypedConverter<ContractEntrypointArgument, MichelineNode> by lazy {
        EntrypointArgumentToMichelineConverter(
            michelson.michelineToCompactStringConverter,
            michelson.stringToMichelsonPrimConverter,
        )
    }

    private val rpcStrictToContractCodeConverter: Converter<RpcScript, ContractCode> by lazy { RpcScriptToContractCodeConverter() }
}