package it.airgap.tezos.michelson

import it.airgap.tezos.michelson.internal.converter.*
import it.airgap.tezos.michelson.micheline.MichelineNode

public fun <T : Michelson> T.toMicheline(): MichelineNode = MichelsonToMichelineConverter.convert(this)

public fun Michelson.GrammarType.Companion.fromStringOrNull(value: String): Michelson.GrammarType? =
    runCatching { StringToMichelsonGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonData.GrammarType.Companion.fromStringOrNull(value: String): MichelsonData.GrammarType? =
    runCatching { StringToMichelsonDataGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonInstruction.GrammarType.Companion.fromStringOrNull(value: String): MichelsonInstruction.GrammarType? =
    runCatching { StringToMichelsonInstructionGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonType.GrammarType.Companion.fromStringOrNull(value: String): MichelsonType.GrammarType? =
    runCatching { StringToMichelsonTypeGrammarTypeConverter.convert(value) }.getOrNull()

public fun MichelsonComparableType.GrammarType.Companion.fromStringOrNull(value: String): MichelsonComparableType.GrammarType? =
    runCatching { StringToMichelsonComparableTypeGrammarTypeConverter.convert(value) }.getOrNull()

public fun <T : MichelineNode> T.toMichelson(): Michelson = MichelineToMichelsonConverter.convert(this)
public fun <T : MichelineNode> T.toExpression(): String = MichelineToStringConverter.convert(this)
public fun <T : MichelineNode> T.toCompactExpression(): String = MichelineToCompactStringConverter.convert(this)