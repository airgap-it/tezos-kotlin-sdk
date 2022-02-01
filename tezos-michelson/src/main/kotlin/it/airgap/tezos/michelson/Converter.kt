package it.airgap.tezos.michelson

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.michelson.internal.converter.*
import it.airgap.tezos.michelson.internal.di.scoped
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

// -- Michelson: Micheline --

public fun <T : Michelson> T.toMicheline(): MichelineNode = toMicheline(TezosSdk.instance.dependencyRegistry.scoped().michelsonToMichelineConverter)
internal fun <T : Michelson> T.toMicheline(michelsonToMichelineConverter: MichelsonToMichelineConverter): MichelineNode = michelsonToMichelineConverter.convert(this)

// -- Michelson: GrammarType

public fun Michelson.GrammarType.Companion.fromStringOrNull(value: String): Michelson.GrammarType? = fromStringOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().stringToMichelsonGrammarTypeConverter)
internal fun Michelson.GrammarType.Companion.fromStringOrNull(value: String, stringToMichelsonGrammarTypeConverter: StringToMichelsonGrammarTypeConverter): Michelson.GrammarType? =
    runCatching { stringToMichelsonGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonData.GrammarType.Companion.fromStringOrNull(value: String): MichelsonData.GrammarType? = fromStringOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().stringToMichelsonDataGrammarTypeConverter)
internal fun MichelsonData.GrammarType.Companion.fromStringOrNull(value: String, stringToMichelsonDataGrammarTypeConverter: StringToMichelsonDataGrammarTypeConverter): MichelsonData.GrammarType? =
    runCatching { stringToMichelsonDataGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonInstruction.GrammarType.Companion.fromStringOrNull(value: String): MichelsonInstruction.GrammarType? = fromStringOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().stringToMichelsonInstructionGrammarTypeConverter)
internal fun MichelsonInstruction.GrammarType.Companion.fromStringOrNull(value: String, stringToMichelsonInstructionGrammarTypeConverter: StringToMichelsonInstructionGrammarTypeConverter): MichelsonInstruction.GrammarType? =
    runCatching { stringToMichelsonInstructionGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonType.GrammarType.Companion.fromStringOrNull(value: String): MichelsonType.GrammarType? = fromStringOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().stringToMichelsonTypeGrammarTypeConverter)
internal fun MichelsonType.GrammarType.Companion.fromStringOrNull(value: String, stringToMichelsonTypeGrammarTypeConverter: StringToMichelsonTypeGrammarTypeConverter): MichelsonType.GrammarType? =
    runCatching { stringToMichelsonTypeGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonComparableType.GrammarType.Companion.fromStringOrNull(value: String): MichelsonComparableType.GrammarType? = fromStringOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().stringToMichelsonComparableTypeGrammarTypeConverter)
internal fun MichelsonComparableType.GrammarType.Companion.fromStringOrNull(value: String, stringToMichelsonComparableTypeGrammarTypeConverter: StringToMichelsonComparableTypeGrammarTypeConverter): MichelsonComparableType.GrammarType? =
    runCatching { stringToMichelsonComparableTypeGrammarTypeConverter.convert(value) }.getOrNull()

public fun Michelson.GrammarType.Companion.fromTagOrNull(value: Int): Michelson.GrammarType? = fromTagOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().tagToMichelsonGrammarTypeConverter)
internal fun Michelson.GrammarType.Companion.fromTagOrNull(value: Int, tagToMichelsonGrammarTypeConverter: TagToMichelsonGrammarTypeConverter): Michelson.GrammarType? =
    runCatching { tagToMichelsonGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonData.GrammarType.Companion.fromTagOrNull(value: Int): MichelsonData.GrammarType? = fromTagOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().tagToMichelsonDataGrammarTypeConverter)
internal fun MichelsonData.GrammarType.Companion.fromTagOrNull(value: Int, tagToMichelsonDataGrammarTypeConverter: TagToMichelsonDataGrammarTypeConverter): MichelsonData.GrammarType? =
    runCatching { tagToMichelsonDataGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonInstruction.GrammarType.Companion.fromTagOrNull(value: Int): MichelsonInstruction.GrammarType? = fromTagOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().tagToMichelsonInstructionGrammarTypeConverter)
internal fun MichelsonInstruction.GrammarType.Companion.fromTagOrNull(value: Int, tagToMichelsonInstructionGrammarTypeConverter: TagToMichelsonInstructionGrammarTypeConverter): MichelsonInstruction.GrammarType? =
    runCatching { tagToMichelsonInstructionGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonType.GrammarType.Companion.fromTagOrNull(value: Int): MichelsonType.GrammarType? = fromTagOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().tagToMichelsonTypeGrammarTypeConverter)
internal fun MichelsonType.GrammarType.Companion.fromTagOrNull(value: Int, tagToMichelsonTypeGrammarTypeConverter: TagToMichelsonTypeGrammarTypeConverter): MichelsonType.GrammarType? =
    runCatching { tagToMichelsonTypeGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonComparableType.GrammarType.Companion.fromTagOrNull(value: Int): MichelsonComparableType.GrammarType? = fromTagOrNull(value, TezosSdk.instance.dependencyRegistry.scoped().tagToMichelsonComparableTypeGrammarTypeConverter)
internal fun MichelsonComparableType.GrammarType.Companion.fromTagOrNull(value: Int, tagToMichelsonComparableTypeGrammarTypeConverter: TagToMichelsonComparableTypeGrammarTypeConverter): MichelsonComparableType.GrammarType? =
    runCatching { tagToMichelsonComparableTypeGrammarTypeConverter.convert(value) }.getOrNull()

// -- Micheline: Michelson --

public fun <T : MichelineNode> T.toMichelson(): Michelson = toMichelson(TezosSdk.instance.dependencyRegistry.scoped().michelineToMichelsonConverter)
internal fun <T : MichelineNode> T.toMichelson(michelineToMichelsonConverter: MichelineToMichelsonConverter): Michelson = michelineToMichelsonConverter.convert(this)

// -- Micheline: String

public fun <T : MichelineNode> T.toExpression(): String = toExpression(TezosSdk.instance.dependencyRegistry.scoped().michelineToStringConverter)
internal fun <T : MichelineNode> T.toExpression(michelineToStringConverter: MichelineToStringConverter): String = michelineToStringConverter.convert(this)

public fun <T : MichelineNode> T.toCompactExpression(): String = toCompactExpression(TezosSdk.instance.dependencyRegistry.scoped().michelineToCompactStringConverter)
internal fun <T : MichelineNode> T.toCompactExpression(michelineToCompactStringConverter: MichelineToCompactStringConverter): String = michelineToCompactStringConverter.convert(this)

// -- Micheline: normalized --

public fun <T : MichelineNode> T.normalized(): MichelineNode = normalized(TezosSdk.instance.dependencyRegistry.scoped().michelineToNormalizedConverter)
internal fun <T : MichelineNode> T.normalized(michelineToNormalizedConverter: MichelineToNormalizedConverter): MichelineNode = michelineToNormalizedConverter.convert(this)

public fun MichelinePrimitiveApplication.normalized(): MichelinePrimitiveApplication = normalized(TezosSdk.instance.dependencyRegistry.scoped().michelinePrimitiveApplicationToNormalizedConverter)
internal fun MichelinePrimitiveApplication.normalized(michelinePrimitiveApplicationToNormalizedConverter: MichelinePrimitiveApplicationToNormalizedConverter): MichelinePrimitiveApplication = michelinePrimitiveApplicationToNormalizedConverter.convert(this)

public fun MichelineSequence.normalized(): MichelineSequence = normalized(TezosSdk.instance.dependencyRegistry.scoped().michelineSequenceToNormalizedConverter)
internal fun MichelineSequence.normalized(michelineSequenceToNormalizedConverter: MichelineSequenceToNormalizedConverter): MichelineSequence = michelineSequenceToNormalizedConverter.convert(this)