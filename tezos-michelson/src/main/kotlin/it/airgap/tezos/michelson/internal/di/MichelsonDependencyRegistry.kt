package it.airgap.tezos.michelson.internal.di

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.michelson.internal.coder.MichelineBytesCoder
import it.airgap.tezos.michelson.internal.coder.MichelineJsonCoder
import it.airgap.tezos.michelson.internal.converter.*
import it.airgap.tezos.michelson.internal.packer.MichelinePacker

internal class MichelsonDependencyRegistry(dependencyRegistry: DependencyRegistry) : ScopedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- coder --

    override val michelineBytesCoder: MichelineBytesCoder by lazy {
        MichelineBytesCoder(
            stringToMichelsonGrammarTypeConverter,
            tagToMichelsonGrammarTypeConverter,
            michelineToCompactStringConverter,
            zarithIntegerBytesCoder,
        )
    }
    override val michelineJsonCoder: MichelineJsonCoder = MichelineJsonCoder()

    // -- converter --

    override val michelineToMichelsonConverter: MichelineToMichelsonConverter by lazy { MichelineToMichelsonConverter(stringToMichelsonGrammarTypeConverter, michelineToCompactStringConverter) }

    override val michelineToNormalizedConverter: MichelineToNormalizedConverter = MichelineToNormalizedConverter()
    override val michelinePrimitiveApplicationToNormalizedConverter: MichelinePrimitiveApplicationToNormalizedConverter get() = michelineToNormalizedConverter.primitiveApplicationToNormalizedConverter
    override val michelineSequenceToNormalizedConverter: MichelineSequenceToNormalizedConverter get() = michelineToNormalizedConverter.sequenceToNormalizedConverter

    override val michelineToStringConverter: MichelineToStringConverter = MichelineToStringConverter()
    override val michelineToCompactStringConverter: MichelineToCompactStringConverter = MichelineToCompactStringConverter()

    override val michelsonToMichelineConverter: MichelsonToMichelineConverter = MichelsonToMichelineConverter()

    override val stringToMichelsonGrammarTypeConverter: StringToMichelsonGrammarTypeConverter = StringToMichelsonGrammarTypeConverter()
    override val stringToMichelsonDataGrammarTypeConverter: StringToMichelsonDataGrammarTypeConverter = StringToMichelsonDataGrammarTypeConverter()
    override val stringToMichelsonInstructionGrammarTypeConverter: StringToMichelsonInstructionGrammarTypeConverter = StringToMichelsonInstructionGrammarTypeConverter()
    override val stringToMichelsonTypeGrammarTypeConverter: StringToMichelsonTypeGrammarTypeConverter = StringToMichelsonTypeGrammarTypeConverter()
    override val stringToMichelsonComparableTypeGrammarTypeConverter: StringToMichelsonComparableTypeGrammarTypeConverter = StringToMichelsonComparableTypeGrammarTypeConverter()

    override val tagToMichelsonGrammarTypeConverter: TagToMichelsonGrammarTypeConverter = TagToMichelsonGrammarTypeConverter()
    override val tagToMichelsonDataGrammarTypeConverter: TagToMichelsonDataGrammarTypeConverter = TagToMichelsonDataGrammarTypeConverter()
    override val tagToMichelsonInstructionGrammarTypeConverter: TagToMichelsonInstructionGrammarTypeConverter = TagToMichelsonInstructionGrammarTypeConverter()
    override val tagToMichelsonTypeGrammarTypeConverter: TagToMichelsonTypeGrammarTypeConverter = TagToMichelsonTypeGrammarTypeConverter()
    override val tagToMichelsonComparableTypeGrammarTypeConverter: TagToMichelsonComparableTypeGrammarTypeConverter = TagToMichelsonComparableTypeGrammarTypeConverter()

    // -- packer --

    override val michelinePacker: MichelinePacker by lazy {
        MichelinePacker(
            michelineBytesCoder,
            stringToMichelsonGrammarTypeConverter,
            michelinePrimitiveApplicationToNormalizedConverter,
            michelineToCompactStringConverter,
            base58BytesCoder,
            addressBytesCoder,
            keyBytesCoder,
            keyHashBytesCoder,
            signatureBytesCoder,
            timestampBigIntCoder,
        )
    }
}