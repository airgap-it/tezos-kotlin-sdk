package it.airgap.tezos.michelson.internal.di

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.di.findScoped
import it.airgap.tezos.michelson.internal.coder.*
import it.airgap.tezos.michelson.internal.converter.*
import it.airgap.tezos.michelson.internal.packer.MichelinePacker

internal interface ScopedDependencyRegistry : DependencyRegistry {

    // -- coder --

    val michelineBytesCoder: MichelineBytesCoder
    val michelineJsonCoder: MichelineJsonCoder

    val addressBytesCoder: AddressBytesCoder
    val keyBytesCoder: KeyBytesCoder
    val keyHashBytesCoder: KeyHashBytesCoder
    val signatureBytesCoder: SignatureBytesCoder

    val timestampBigIntCoder: TimestampBigIntCoder

    // -- converter --

    val michelineToMichelsonConverter: MichelineToMichelsonConverter

    val michelineToNormalizedConverter: MichelineToNormalizedConverter
    val michelinePrimitiveApplicationToNormalizedConverter: MichelinePrimitiveApplicationToNormalizedConverter
    val michelineSequenceToNormalizedConverter: MichelineSequenceToNormalizedConverter

    val michelineToStringConverter: MichelineToStringConverter
    val michelineToCompactStringConverter: MichelineToCompactStringConverter

    val michelsonToMichelineConverter: MichelsonToMichelineConverter

    val stringToMichelsonGrammarTypeConverter: StringToMichelsonGrammarTypeConverter
    val stringToMichelsonDataGrammarTypeConverter: StringToMichelsonDataGrammarTypeConverter
    val stringToMichelsonInstructionGrammarTypeConverter: StringToMichelsonInstructionGrammarTypeConverter
    val stringToMichelsonTypeGrammarTypeConverter: StringToMichelsonTypeGrammarTypeConverter
    val stringToMichelsonComparableTypeGrammarTypeConverter: StringToMichelsonComparableTypeGrammarTypeConverter

    val tagToMichelsonGrammarTypeConverter: TagToMichelsonGrammarTypeConverter
    val tagToMichelsonDataGrammarTypeConverter: TagToMichelsonDataGrammarTypeConverter
    val tagToMichelsonInstructionGrammarTypeConverter: TagToMichelsonInstructionGrammarTypeConverter
    val tagToMichelsonTypeGrammarTypeConverter: TagToMichelsonTypeGrammarTypeConverter
    val tagToMichelsonComparableTypeGrammarTypeConverter: TagToMichelsonComparableTypeGrammarTypeConverter

    // -- packer --

    val michelinePacker: MichelinePacker
}

internal fun DependencyRegistry.scoped(): ScopedDependencyRegistry =
    if (this is ScopedDependencyRegistry) this
    else findScoped<MichelsonDependencyRegistry>() ?: MichelsonDependencyRegistry(this).also { addScoped(it) }