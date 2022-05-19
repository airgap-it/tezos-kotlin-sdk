package it.airgap.tezos.contract.entrypoint

import it.airgap.tezos.contract.internal.entrypoint.LazyMetaContractEntrypoint
import it.airgap.tezos.contract.internal.entrypoint.MetaContractEntrypoint
import it.airgap.tezos.contract.internal.estimator.withMinFee
import it.airgap.tezos.contract.internal.utils.failWithContractException
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.contract.type.LazyContractCode
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.core.internal.utils.asTezosNatural
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.number.TezosNatural
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.comparator.isPrim
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.normalizer.normalized
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractEntrypointsResponse
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached
import it.airgap.tezos.rpc.internal.estimator.FeeEstimator
import it.airgap.tezos.rpc.type.limits.OperationLimits

// -- ContractEntrypoint --

public class ContractEntrypoint internal constructor(
    public val name: String,
    private val contractAddress: ContractHash,
    private val block: Block,
    private val operationFeeEstimator: FeeEstimator<Operation>,
    private val meta: LazyMetaContractEntrypoint,
) {

    public suspend fun call(
        parameters: MichelineNode,
        source: ImplicitAddress,
        branch: BlockHash? = null,
        fee: Mutez? = null,
        counter: String? = null,
        limits: OperationLimits? = null,
        amount: Mutez? = null,
        headers: List<HttpHeader> = emptyList(),
    ): Operation.Unsigned {
        val branch = branch ?: block.header.get(headers).header.hash
        val counter = counter ?: block.context.contracts(source).counter.get(headers).counter

        val operation = Operation.Unsigned(
            branch,
            listOf(
                OperationContent.Transaction(
                    source = source,
                    fee = fee ?: Mutez(0U),
                    counter = counter?.asTezosNatural() ?: TezosNatural(0U),
                    gasLimit = limits?.gas?.asTezosNatural() ?: TezosNatural(0U),
                    storageLimit = limits?.gas?.asTezosNatural() ?: TezosNatural(0U),
                    amount = amount ?: Mutez(0U),
                    destination = contractAddress,
                    parameters = Parameters(
                        Entrypoint.fromString(name),
                        parameters,
                    ),
                ),
            ),
        )

        return if (fee != null && limits != null) operation
        else operation.withMinFee(operationFeeEstimator, headers)
    }

    public suspend fun call(
        parameters: ContractEntrypointParameter,
        source: ImplicitAddress,
        branch: BlockHash? = null,
        fee: Mutez? = null,
        counter: String? = null,
        limits: OperationLimits? = null,
        amount: Mutez? = null,
        headers: List<HttpHeader> = emptyList(),
    ): Operation.Unsigned {
        val meta = meta.get(headers)
        val value = meta.valueFrom(parameters)

        return call(value, source, branch, fee, counter, limits, amount, headers)
    }

    internal class Factory(
        private val meta: MetaContractEntrypoint.Factory,
        private val contractAddress: ContractHash,
        private val block: Block,
        private val contract: Block.Context.Contracts.Contract,
        private val operationFeeEstimator: FeeEstimator<Operation>,
        private val michelineNormalizer: Normalizer<MichelineNode>,
    ) {

        private val contractEntrypointsCached: Cached<Map<String, MetaContractEntrypoint>> = Cached { headers -> contract.entrypoints.get(headers).toMetaContractEntrypoint() }

        fun create(code: LazyContractCode, name: String): ContractEntrypoint {
            val meta = contractEntrypointsCached.combine(code).map { (entrypoints, code) -> entrypoints[name] ?: code.findEntrypoint(name) ?: failWithUnknownEntrypoint(name) }
            return ContractEntrypoint(name, contractAddress, block, operationFeeEstimator, meta)
        }

        private fun GetContractEntrypointsResponse.toMetaContractEntrypoint(): Map<String, MetaContractEntrypoint> =
            entrypoints.mapValues { meta.create(it.value.normalized(michelineNormalizer)) }

        private fun ContractCode.findEntrypoint(name: String): MetaContractEntrypoint? {
            if (name != Entrypoint.Default.value) return null
            if (!parameter.isPrim(MichelsonType.Parameter) || parameter.args.size != 1) failWithUnknownCodeType()

            return meta.create(parameter.args.first())
        }

        private fun failWithUnknownCodeType(): Nothing = failWithContractException("Unknown contract code type.")
        private fun failWithUnknownEntrypoint(name: String): Nothing = failWithContractException("Entrypoint $name could not be found.")
    }
}

// -- ContractEntrypointParameter --

public sealed interface ContractEntrypointParameter {
    public val name: String?

    public class Value(public val value: MichelineNode?, override val name: String? = null) : ContractEntrypointParameter {
        public companion object {}
    }

    public class Object(internal val elements: MutableList<ContractEntrypointParameter>, override val name: String? = null) : ContractEntrypointParameter {
        public constructor(vararg elements: ContractEntrypointParameter, name: String? = null) : this(elements.toMutableList(), name)

        internal val fields: Set<String> = elements.mapNotNull { it.name }.toSet()

        public companion object {}
    }

    public class Sequence internal constructor(internal val elements: List<ContractEntrypointParameter>, override val name: String? = null) : ContractEntrypointParameter {
        public constructor(vararg elements: ContractEntrypointParameter, name: String? = null) : this(elements.toList(), name)

        public companion object {}
    }

    public class Map internal constructor(internal val map: kotlin.collections.Map<ContractEntrypointParameter, ContractEntrypointParameter>, override val name: String? = null) : ContractEntrypointParameter {
        public constructor(vararg elements: Pair<ContractEntrypointParameter, ContractEntrypointParameter>, name: String? = null) : this(elements.toMap().toMutableMap(), name)

        public companion object {}
    }

    public companion object {}
}
