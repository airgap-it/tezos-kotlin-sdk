package it.airgap.tezos.contract.internal.di

import it.airgap.tezos.contract.Contract
import it.airgap.tezos.contract.entrypoint.ContractEntrypoint
import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.contract.internal.context.TezosContractContext.getOrPutWeak
import it.airgap.tezos.contract.internal.converter.EntrypointParameterToMichelineConverter
import it.airgap.tezos.contract.internal.converter.MichelineToStorageEntryConverter
import it.airgap.tezos.contract.internal.converter.ScriptToContractCodeConverter
import it.airgap.tezos.contract.internal.converter.TypedConverter
import it.airgap.tezos.contract.internal.entrypoint.MetaContractEntrypoint
import it.airgap.tezos.contract.internal.storage.MetaContractStorage
import it.airgap.tezos.contract.storage.ContractStorage
import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.michelson.internal.di.MichelsonDependencyRegistry
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.internal.di.RpcDependencyRegistry
import it.airgap.tezos.rpc.internal.estimator.FeeEstimator
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
        val tezosRpc = rpc.tezosRpc(nodeUrl)
        val block = tezosRpc.chains.main.blocks.head
        val contract = block.context.contracts(address)

        val operationFeeEstimator = rpc.operationFeeEstimator(nodeUrl)

        Contract(
            address,
            contract,
            contractStorageFactory(block, contract),
            contractEntrypointFactory(address, block, contract, operationFeeEstimator),
            scriptToContractCodeConverter,
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
        operationFeeEstimator: FeeEstimator<Operation>,
    ): ContractEntrypoint.Factory =
        ContractEntrypoint.Factory(
            metaContractEntrypointFactory,
            address,
            block,
            contract,
            operationFeeEstimator,
            michelson.michelineNormalizer,
            michelson.michelsonToMichelineConverter,
        )

    private val metaContractEntrypointFactory: MetaContractEntrypoint.Factory by lazy { MetaContractEntrypoint.Factory(entrypointParameterToMichelineConverter) }

    // -- converter --

    private fun michelineToStorageEntryConverter(block: Block): TypedConverter<MichelineNode, ContractStorageEntry> =
        MichelineToStorageEntryConverter(
            block,
            core.encodedBytesCoder,
            michelson.michelinePacker,
            michelson.michelineToCompactStringConverter,
            michelson.stringToMichelsonPrimConverter,
        )

    private val entrypointParameterToMichelineConverter: TypedConverter<ContractEntrypointParameter, MichelineNode> by lazy {
        EntrypointParameterToMichelineConverter(
            michelson.michelineToCompactStringConverter,
            michelson.stringToMichelsonPrimConverter,
        )
    }

    private val scriptToContractCodeConverter: Converter<Script, ContractCode> by lazy { ScriptToContractCodeConverter() }
}