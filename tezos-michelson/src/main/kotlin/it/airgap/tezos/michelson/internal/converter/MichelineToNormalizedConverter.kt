package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.isPrim
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

@InternalTezosSdkApi
public class MichelineToNormalizedConverter : Converter<MichelineNode, MichelineNode> {
    internal val primitiveApplicationToNormalizedConverter: MichelinePrimitiveApplicationToNormalizedConverter = MichelinePrimitiveApplicationToNormalizedConverter((this))
    internal val sequenceToNormalizedConverter: MichelineSequenceToNormalizedConverter = MichelineSequenceToNormalizedConverter(this)

    override fun convert(value: MichelineNode): MichelineNode =
        when (value) {
            is MichelineLiteral -> value
            is MichelinePrimitiveApplication -> primitiveApplicationToNormalizedConverter.convert(value)
            is MichelineSequence -> sequenceToNormalizedConverter.convert(value)
        }
}

@InternalTezosSdkApi
public class MichelinePrimitiveApplicationToNormalizedConverter(
    private val toNormalizedConverter: MichelineToNormalizedConverter,
) : Converter<MichelinePrimitiveApplication, MichelinePrimitiveApplication> {
    override fun convert(value: MichelinePrimitiveApplication): MichelinePrimitiveApplication =
        when {
            value.isPrim(MichelsonData.Pair) || value.isPrim(MichelsonType.Pair) || value.isPrim(MichelsonComparableType.Pair) -> {
                if (value.args.size <= 2) value
                else value.copy(
                    args = listOf(
                        toNormalizedConverter.convert(value.args.first()),
                        toNormalizedConverter.convert(
                            MichelinePrimitiveApplication(
                                prim = value.prim,
                                args = value.args.subList(1, value.args.size),
                                annots = emptyList(),
                            )
                        )
                    ),
                )
            }
            else -> value.copy(args = value.args.map { toNormalizedConverter.convert(it) })
        }
}

@InternalTezosSdkApi
public class MichelineSequenceToNormalizedConverter(
    private val toNormalizedConverter: MichelineToNormalizedConverter,
) : Converter<MichelineSequence, MichelineSequence> {
    override fun convert(value: MichelineSequence): MichelineSequence = MichelineSequence(value.nodes.map { toNormalizedConverter.convert(it) })
}