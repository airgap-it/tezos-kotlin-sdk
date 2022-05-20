package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.michelson.*

internal class StringToMichelsonPrimConverter : Converter<String, Michelson.Prim> {
    override fun convert(value: String): Michelson.Prim =
        Michelson.Prim.values.firstOrNull { value == it.name } ?: failWithUnknownPrim(value)
}

internal class StringToMichelsonDataPrimConverter : Converter<String, MichelsonData.Prim> {
    override fun convert(value: String): MichelsonData.Prim =
        MichelsonData.Prim.values.firstOrNull { value == it.name } ?: failWithUnknownPrim(value)
}

internal class StringToMichelsonInstructionPrimConverter : Converter<String, MichelsonInstruction.Prim> {
    override fun convert(value: String): MichelsonInstruction.Prim =
        MichelsonInstruction.Prim.values.firstOrNull { value == it.name } ?: failWithUnknownPrim(value)
}

internal class StringToMichelsonTypePrimConverter : Converter<String, MichelsonType.Prim> {
    override fun convert(value: String): MichelsonType.Prim =
        MichelsonType.Prim.values.firstOrNull { value == it.name } ?: failWithUnknownPrim(value)
}

internal class StringToMichelsonComparableTypePrimConverter : Converter<String, MichelsonComparableType.Prim> {
    override fun convert(value: String): MichelsonComparableType.Prim =
        MichelsonComparableType.Prim.values.firstOrNull { value == it.name } ?: failWithUnknownPrim(value)
}

private fun failWithUnknownPrim(prim: String): Nothing =
    failWithIllegalArgument("Unknown Michelson prim: \"$prim\".")
