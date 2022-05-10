package it.airgap.tezos.contract.internal.entrypoint

import it.airgap.tezos.contract.entrypoint.ContractEntrypointArgument
import it.airgap.tezos.contract.internal.converter.EntrypointArgumentToMichelineConverter
import it.airgap.tezos.contract.internal.converter.toMicheline
import it.airgap.tezos.contract.internal.micheline.MichelineTrace
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication

// -- MetaContractEntrypoint --

internal class MetaContractEntrypoint(
    private val type: MichelineNode,
    private val entrypointArgumentToMichelineConverter: EntrypointArgumentToMichelineConverter,
) {
    fun valueFrom(argument: ContractEntrypointArgument): MichelineNode = argument.toMicheline(type, entrypointArgumentToMichelineConverter)
}

// -- MetaContractEntrypointArgument --

internal sealed interface MetaContractEntrypointArgument {
    val type: MichelineNode
    val trace: MichelineTrace
    val names: Set<String>
        get() {
            val type = type as? MichelinePrimitiveApplication ?: return emptySet()
            return type.annots.map { it.value }.toSet()
        }

    fun relativeToPrevious(trace: MichelineTrace): MetaContractEntrypointArgument
    fun relativeToNext(): MetaContractEntrypointArgument?

    class Value(override val type: MichelineNode, override val trace: MichelineTrace) : MetaContractEntrypointArgument {
        override fun relativeToPrevious(trace: MichelineTrace): Value = Value(type, trace + this.trace)
        override fun relativeToNext(): Value? = trace.next?.let { Value(type, it) }
    }

    class Object(
        override val type: MichelineNode,
        override val trace: MichelineTrace,
        val elements: List<MetaContractEntrypointArgument>,
    ) : MetaContractEntrypointArgument {
        private val map: kotlin.collections.Map<String, MetaContractEntrypointArgument> =
            elements.flatMap { element ->
                element.names.map { it to element }
            }.toMap()

        operator fun get(name: String): MetaContractEntrypointArgument? = map[name]

        override fun relativeToPrevious(trace: MichelineTrace): Object = Object(type, trace + this.trace, elements)
        override fun relativeToNext(): Object? = trace.next?.let { Object(type, it, elements) }
    }

    class Sequence(
        override val type: MichelineNode,
        override val trace: MichelineTrace,
        val elements: List<MetaContractEntrypointArgument>
    ) : MetaContractEntrypointArgument {
        override fun relativeToPrevious(trace: MichelineTrace): Sequence = Sequence(type, trace + this.trace, elements)
        override fun relativeToNext(): Sequence? = trace.next?.let { Sequence(type, it, elements) }
    }

    class Map(
        override val type: MichelineNode,
        override val trace: MichelineTrace,
        val key: MetaContractEntrypointArgument,
        val value: MetaContractEntrypointArgument,
    ) : MetaContractEntrypointArgument {
        override fun relativeToPrevious(trace: MichelineTrace): Map = Map(type, trace + this.trace, key, value)
        override fun relativeToNext(): Map? = trace.next?.let { Map(type, it, key, value) }
    }
}