package it.airgap.tezos.contract.internal.entrypoint

import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.contract.internal.context.TezosContractContext.flatten
import it.airgap.tezos.contract.internal.converter.TypedConverter
import it.airgap.tezos.contract.internal.converter.toMicheline
import it.airgap.tezos.contract.internal.micheline.MichelineTrace
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.rpc.internal.cache.Cached

// -- MetaContractEntrypoint --

internal class MetaContractEntrypoint(
    private val type: Micheline,
    private val entrypointParameterToMichelineConverter: TypedConverter<ContractEntrypointParameter, Micheline>,
) {
    fun valueFrom(parameter: ContractEntrypointParameter): Micheline = parameter.toMicheline(type, entrypointParameterToMichelineConverter)

    class Factory(private val entrypointParameterToMichelineConverter: TypedConverter<ContractEntrypointParameter, Micheline>) {
        fun create(type: Micheline): MetaContractEntrypoint = MetaContractEntrypoint(type, entrypointParameterToMichelineConverter)
    }
}

internal typealias LazyMetaContractEntrypoint = Cached<MetaContractEntrypoint>

// -- MetaContractEntrypointArgument --

internal sealed interface MetaContractEntrypointParameter {
    val type: Micheline
    val trace: MichelineTrace

    val names: Set<String>
        get() {
            val type = type as? MichelinePrimitiveApplication ?: return emptySet()
            return type.annots.map { it.value }.toSet()
        }

    fun trace(name: String): MichelineTrace?

    class Value(override val type: Micheline, override val trace: MichelineTrace) : MetaContractEntrypointParameter {
        override fun trace(name: String): MichelineTrace? = if (names.contains(name)) trace else null
    }

    class Object(
        override val type: Micheline,
        override val trace: MichelineTrace,
        val elements: List<MetaContractEntrypointParameter>,
    ) : MetaContractEntrypointParameter {
        val namedTraces: kotlin.collections.Map<String, MichelineTrace> = flattenTraces

        override fun trace(name: String): MichelineTrace? = namedTraces[name]
    }

    class Sequence(
        override val type: Micheline,
        override val trace: MichelineTrace,
        val elements: List<MetaContractEntrypointParameter>,
    ) : MetaContractEntrypointParameter {
        override fun trace(name: String): MichelineTrace? = null
    }

    class Map(
        override val type: Micheline,
        override val trace: MichelineTrace,
        val key: MetaContractEntrypointParameter,
        val value: MetaContractEntrypointParameter,
    ) : MetaContractEntrypointParameter {
        override fun trace(name: String): MichelineTrace? = null
    }

    val MetaContractEntrypointParameter.flattenTraces: kotlin.collections.Map<String, MichelineTrace>
        get() = when {
            this is Object && names.isEmpty() -> elements.map { element ->
                element.flattenTraces.mapValues { element.trace + it.value }
            }.flatten()
            else -> names.associateWith { trace }
        }
}