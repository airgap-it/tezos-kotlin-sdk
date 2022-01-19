package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.static.*

internal class StringToMichelsonGrammarTypeConverter : Converter<String, Michelson.GrammarType> {
    override fun convert(value: String): Michelson.GrammarType =
        grammarTypes.firstOrNull { value == it.name } ?: failWithUnknownGrammarType(value)
}

internal class StringToMichelsonDataGrammarTypeConverter : Converter<String, MichelsonData.GrammarType> {
    override fun convert(value: String): MichelsonData.GrammarType =
        dataGrammarTypes.firstOrNull { value == it.name } ?: failWithUnknownGrammarType(value)
}

internal class StringToMichelsonInstructionGrammarTypeConverter : Converter<String, MichelsonInstruction.GrammarType> {
    override fun convert(value: String): MichelsonInstruction.GrammarType =
        instructionGrammarTypes.firstOrNull { value == it.name } ?: failWithUnknownGrammarType(value)
}

internal class StringToMichelsonTypeGrammarTypeConverter : Converter<String, MichelsonType.GrammarType> {
    override fun convert(value: String): MichelsonType.GrammarType =
        typeGrammarTypes.firstOrNull { value == it.name } ?: failWithUnknownGrammarType(value)
}

internal class StringToMichelsonComparableTypeGrammarTypeConverter :
    Converter<String, MichelsonComparableType.GrammarType> {
    override fun convert(value: String): MichelsonComparableType.GrammarType =
        comparableTypeGrammarTypes.firstOrNull { value == it.name } ?: failWithUnknownGrammarType(value)
}

private fun failWithUnknownGrammarType(grammarType: String): Nothing =
    failWithIllegalArgument("Unknown Michelson grammar type: \"$grammarType\".")
