package it.airgap.tezos.michelson.micheline.dsl.builder

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.dsl.MichelineDslMaker

public fun interface MichelineBuilder {
    public fun build(): MichelineNode
}

@MichelineDslMaker
public abstract class MichelineTransformableBuilder internal constructor(
    @PublishedApi internal val michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
) : MichelineBuilder {
    protected abstract val value: MichelineNode

    override fun build(): MichelineNode = transformations.fold(value) { acc, transform -> transform(acc) }

    // -- transform --

    private val transformations: MutableList<(MichelineNode) -> MichelineNode> = mutableListOf()

    internal fun <Self : MichelineTransformableBuilder> Self.mapNode(transform: (MichelineNode) -> MichelineNode): Self = apply { transformations.add(transform) }
}