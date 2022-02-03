package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.michelson.*

@InternalTezosSdkApi
public class StringToMichelsonPrimConverter : Converter<String, Michelson.Prim> {
    override fun convert(value: String): Michelson.Prim =
        Michelson.Prim.values.firstOrNull { value == it.name } ?: failWithUnknownPrim(value)
}

@InternalTezosSdkApi
public class StringToMichelsonDataPrimConverter : Converter<String, MichelsonData.Prim> {
    override fun convert(value: String): MichelsonData.Prim =
        MichelsonData.Prim.values.firstOrNull { value == it.name } ?: failWithUnknownPrim(value)
}

@InternalTezosSdkApi
public class StringToMichelsonInstructionPrimConverter : Converter<String, MichelsonInstruction.Prim> {
    override fun convert(value: String): MichelsonInstruction.Prim =
        MichelsonInstruction.Prim.values.firstOrNull { value == it.name } ?: failWithUnknownPrim(value)
}

@InternalTezosSdkApi
public class StringToMichelsonTypePrimConverter : Converter<String, MichelsonType.Prim> {
    override fun convert(value: String): MichelsonType.Prim =
        MichelsonType.Prim.values.firstOrNull { value == it.name } ?: failWithUnknownPrim(value)
}

@InternalTezosSdkApi
public class StringToMichelsonComparableTypePrimConverter : Converter<String, MichelsonComparableType.Prim> {
    override fun convert(value: String): MichelsonComparableType.Prim =
        MichelsonComparableType.Prim.values.firstOrNull { value == it.name } ?: failWithUnknownPrim(value)
}

private fun failWithUnknownPrim(prim: String): Nothing =
    failWithIllegalArgument("Unknown Michelson prim: \"$prim\".")
