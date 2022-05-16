package it.airgap.tezos.michelson.internal.normalizer

import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.comparator.isPrim
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal class MichelineToNormalizedConverter : Normalizer<MichelineNode> {
    internal val primitiveApplicationToNormalizedConverter: MichelinePrimitiveApplicationToNormalizedConverter = MichelinePrimitiveApplicationToNormalizedConverter((this))
    internal val sequenceToNormalizedConverter: MichelineSequenceToNormalizedConverter = MichelineSequenceToNormalizedConverter(this)

    override fun normalize(value: MichelineNode): MichelineNode =
        when (value) {
            is MichelineLiteral -> value
            is MichelinePrimitiveApplication -> primitiveApplicationToNormalizedConverter.normalize(value)
            is MichelineSequence -> sequenceToNormalizedConverter.normalize(value)
        }
}

internal class MichelinePrimitiveApplicationToNormalizedConverter(private val toNormalizedConverter: MichelineToNormalizedConverter) : Normalizer<MichelinePrimitiveApplication> {
    override fun normalize(value: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        when {
            value.isPrim(MichelsonData.Pair) || value.isPrim(MichelsonType.Pair) || value.isPrim(MichelsonComparableType.Pair) -> {
                if (value.args.size <= 2) value
                else value.copy(
                    args = listOf(
                        toNormalizedConverter.normalize(value.args.first()),
                        toNormalizedConverter.normalize(
                            MichelinePrimitiveApplication(
                                prim = value.prim,
                                args = value.args.subList(1, value.args.size),
                                annots = emptyList(),
                            )
                        )
                    ),
                )
            }
            else -> value.copy(args = value.args.map { toNormalizedConverter.normalize(it) })
        }
}

internal class MichelineSequenceToNormalizedConverter(private val toNormalizedConverter: MichelineToNormalizedConverter) : Normalizer<MichelineSequence> {
    override fun normalize(value: MichelineSequence): MichelineSequence = MichelineSequence(value.nodes.map { toNormalizedConverter.normalize(it) })
}