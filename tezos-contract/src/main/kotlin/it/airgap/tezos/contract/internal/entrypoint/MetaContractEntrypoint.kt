package it.airgap.tezos.contract.internal.entrypoint

import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.contract.internal.context.TezosContractContext.flatten
import it.airgap.tezos.contract.internal.converter.TypedConverter
import it.airgap.tezos.contract.internal.converter.toMicheline
import it.airgap.tezos.contract.internal.micheline.MichelineTrace
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.rpc.internal.cache.Cached

// -- MetaContractEntrypoint --

internal class MetaContractEntrypoint(
    private val type: MichelineNode,
    private val entrypointParameterToMichelineConverter: TypedConverter<ContractEntrypointParameter, MichelineNode>,
) {
    fun valueFrom(parameter: ContractEntrypointParameter): MichelineNode = parameter.toMicheline(type, entrypointParameterToMichelineConverter)

    class Factory(private val entrypointParameterToMichelineConverter: TypedConverter<ContractEntrypointParameter, MichelineNode>) {
        fun create(type: MichelineNode): MetaContractEntrypoint = MetaContractEntrypoint(type, entrypointParameterToMichelineConverter)
    }
}

internal typealias LazyMetaContractEntrypoint = Cached<MetaContractEntrypoint>

// -- MetaContractEntrypointArgument --

internal sealed interface MetaContractEntrypointArgument {
    val type: MichelineNode
    val trace: MichelineTrace

    val names: Set<String>
        get() {
            val type = type as? MichelinePrimitiveApplication ?: return emptySet()
            return type.annots.map { it.value }.toSet()
        }

    fun trace(name: String): MichelineTrace?

    class Value(override val type: MichelineNode, override val trace: MichelineTrace) : MetaContractEntrypointArgument {
        override fun trace(name: String): MichelineTrace? = if (names.contains(name)) trace else null
    }

    class Object(
        override val type: MichelineNode,
        override val trace: MichelineTrace,
        val elements: List<MetaContractEntrypointArgument>,
    ) : MetaContractEntrypointArgument {
        val namedTraces: kotlin.collections.Map<String, MichelineTrace> = flattenTraces

        override fun trace(name: String): MichelineTrace? = namedTraces[name]
    }

    class Sequence(
        override val type: MichelineNode,
        override val trace: MichelineTrace,
        val elements: List<MetaContractEntrypointArgument>,
    ) : MetaContractEntrypointArgument {
        val namedTraces: kotlin.collections.Map<String, MichelineTrace> = elements.map { it.flattenTraces }.flatten()

        override fun trace(name: String): MichelineTrace? = namedTraces[name]
    }

    class Map(
        override val type: MichelineNode,
        override val trace: MichelineTrace,
        val key: MetaContractEntrypointArgument,
        val value: MetaContractEntrypointArgument,
    ) : MetaContractEntrypointArgument {
        val namedTraces: kotlin.collections.Map<String, MichelineTrace> = key.flattenTraces + value.flattenTraces

        override fun trace(name: String): MichelineTrace? = namedTraces[name]
    }

    val MetaContractEntrypointArgument.flattenTraces: kotlin.collections.Map<String, MichelineTrace>
        get() = when {
            this is Object && names.isEmpty() -> elements.map { element ->
                element.flattenTraces.mapValues { element.trace + it.value }
            }.flatten()
            else -> names.associateWith { trace }
        }
}