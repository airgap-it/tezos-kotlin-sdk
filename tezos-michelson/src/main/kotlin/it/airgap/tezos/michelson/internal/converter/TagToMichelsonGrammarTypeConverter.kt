package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.static.*

internal class TagToMichelsonGrammarTypeConverter : Converter<Int, Michelson.GrammarType> {
    override fun convert(value: Int): Michelson.GrammarType =
        grammarTypes.firstOrNull { value == it.tag } ?: failWithUnknownGrammarType(value)
}

internal class TagToMichelsonDataGrammarTypeConverter : Converter<Int, MichelsonData.GrammarType> {
    override fun convert(value: Int): MichelsonData.GrammarType =
        dataGrammarTypes.firstOrNull { value == it.tag } ?: failWithUnknownGrammarType(value)
}

internal class TagToMichelsonInstructionGrammarTypeConverter : Converter<Int, MichelsonInstruction.GrammarType> {
    override fun convert(value: Int): MichelsonInstruction.GrammarType =
        instructionGrammarTypes.firstOrNull { value == it.tag } ?: failWithUnknownGrammarType(value)
}

internal class TagToMichelsonTypeGrammarTypeConverter : Converter<Int, MichelsonType.GrammarType> {
    override fun convert(value: Int): MichelsonType.GrammarType =
        typeGrammarTypes.firstOrNull { value == it.tag } ?: failWithUnknownGrammarType(value)
}

internal class TagToMichelsonComparableTypeGrammarTypeConverter : Converter<Int, MichelsonComparableType.GrammarType> {
    override fun convert(value: Int): MichelsonComparableType.GrammarType =
        comparableTypeGrammarTypes.firstOrNull { value == it.tag } ?: failWithUnknownGrammarType(value)
}

private fun failWithUnknownGrammarType(grammarType: Int): Nothing =
    failWithIllegalArgument("Unknown Michelson grammar type: \"$grammarType\".")