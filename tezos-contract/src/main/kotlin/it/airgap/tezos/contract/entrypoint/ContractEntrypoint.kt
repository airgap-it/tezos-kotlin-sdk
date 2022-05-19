package it.airgap.tezos.contract.entrypoint

import it.airgap.tezos.contract.internal.entrypoint.LazyMetaContractEntrypoint
import it.airgap.tezos.contract.internal.entrypoint.MetaContractEntrypoint
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.contract.type.LazyContractCode
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.toZarithNatural
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.zarith.ZarithNatural
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.comparator.isPrim
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.normalizer.normalized
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.operation.fee
import it.airgap.tezos.operation.limits
import it.airgap.tezos.operation.type.OperationLimits
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractEntrypointsResponse
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached

// -- ContractEntrypoint --

public class ContractEntrypoint internal constructor(
    public val name: String,
    private val contractAddress: ContractHash,
    private val block: Block,
    private val rpc: TezosRpc, // TODO: extract fee calculation logic to a separate class and use it here instead
    private val meta: LazyMetaContractEntrypoint,
) {

    public suspend fun call(
        args: MichelineNode,
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
                    counter = counter?.let { ZarithNatural(it) } ?: ZarithNatural(0U),
                    gasLimit = limits?.gas?.toZarithNatural() ?: ZarithNatural(0U),
                    storageLimit = limits?.gas?.toZarithNatural() ?: ZarithNatural(0U),
                    amount = amount ?: Mutez(0U),
                    destination = contractAddress,
                    parameters = Parameters(
                        Entrypoint.fromString(name),
                        args,
                    ),
                ),
            ),
        )

        return if (operation.fee == fee && operation.limits == limits) operation
        else rpc.minFee(operation = operation, headers = headers).asUnsigned()
    }

    public suspend fun call(
        args: ContractEntrypointArgument,
        source: ImplicitAddress,
        branch: BlockHash? = null,
        fee: Mutez? = null,
        counter: String? = null,
        limits: OperationLimits? = null,
        amount: Mutez? = null,
        headers: List<HttpHeader> = emptyList(),
    ): Operation.Unsigned {
        val meta = meta.get(headers)
        val value = meta.valueFrom(args)

        return call(value, source, branch, fee, counter, limits, amount, headers)
    }

    private fun Operation.asUnsigned(): Operation.Unsigned =
        when (this) {
            is Operation.Unsigned -> this
            is Operation.Signed -> Operation.Unsigned(branch, contents)
        }

    internal class Factory(
        private val meta: MetaContractEntrypoint.Factory,
        private val contractAddress: ContractHash,
        private val block: Block,
        private val contract: Block.Context.Contracts.Contract,
        private val rpc: TezosRpc,
        private val michelineNormalizer: Normalizer<MichelineNode>,
    ) {
        private val contractEntrypointsCached: Cached<Map<String, MetaContractEntrypoint>> = Cached { headers -> contract.entrypoints.get(headers).toMetaContractEntrypoint() }

        fun create(code: LazyContractCode, name: String): ContractEntrypoint {
            val meta = contractEntrypointsCached.combine(code).map { (entrypoints, code) -> entrypoints[name] ?: code.findEntrypoint(name) ?: failWithUnknownEntrypoint(name) }
            return ContractEntrypoint(name, contractAddress, block, rpc, meta)
        }

        private fun GetContractEntrypointsResponse.toMetaContractEntrypoint(): Map<String, MetaContractEntrypoint> =
            entrypoints.mapValues { meta.create(it.value.normalized(michelineNormalizer)) }

        private fun ContractCode.findEntrypoint(name: String): MetaContractEntrypoint? {
            if (name != Entrypoint.Default.value) return null
            if (!parameter.isPrim(MichelsonType.Parameter) || parameter.args.size != 1) failWithUnknownCodeType()

            return meta.create(parameter.args.first())
        }

        private fun failWithUnknownCodeType(): Nothing = throw Exception("Unknown contract code type.")
        private fun failWithUnknownEntrypoint(name: String): Nothing = failWithIllegalArgument("Entrypoint $name could not be found.")
    }
}

// -- ContractEntrypointArgument --

public sealed interface ContractEntrypointArgument {
    public val name: String?

    public class Value(public val value: MichelineNode? = null, override val name: String? = null) : ContractEntrypointArgument {
        public companion object {}
    }

    public class Object(internal val elements: MutableList<ContractEntrypointArgument>, override val name: String? = null) : ContractEntrypointArgument {
        public constructor(vararg elements: ContractEntrypointArgument, name: String? = null) : this(elements.toMutableList(), name)

        internal val fields: Set<String> = elements.mapNotNull { it.name }.toSet()

        public companion object {}
    }

    public class Sequence internal constructor(internal val elements: List<ContractEntrypointArgument>, override val name: String? = null) : ContractEntrypointArgument {
        public constructor(vararg elements: ContractEntrypointArgument, name: String? = null) : this(elements.toList(), name)

        public companion object {}
    }

    public class Map internal constructor(internal val map: kotlin.collections.Map<ContractEntrypointArgument, ContractEntrypointArgument>, override val name: String? = null) : ContractEntrypointArgument {
        public constructor(vararg elements: Pair<ContractEntrypointArgument, ContractEntrypointArgument>, name: String? = null) : this(elements.toMap().toMutableMap(), name)

        public companion object {}
    }

    public companion object {}
}
