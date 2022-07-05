package it.airgap.tezos.michelson.micheline.dsl.builder

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.dsl.MichelineDslMaker

public fun interface MichelineBuilder {
    public fun build(): Micheline
}

@MichelineDslMaker
public abstract class MichelineTransformableBuilder internal constructor(
    @PublishedApi internal val michelsonToMichelineConverter: Converter<Michelson, Micheline>,
) : MichelineBuilder {
    protected abstract val value: Micheline

    override fun build(): Micheline = transformations.fold(value) { acc, transform -> transform(acc) }

    // -- transform --

    private val transformations: MutableList<(Micheline) -> Micheline> = mutableListOf()

    internal fun <Self : MichelineTransformableBuilder> Self.mapNode(transform: (Micheline) -> Micheline): Self = apply { transformations.add(transform) }
}