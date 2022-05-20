package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.michelson.*

internal class TagToMichelsonPrimConverter : Converter<ByteArray, Michelson.Prim> {
    override fun convert(value: ByteArray): Michelson.Prim =
        Michelson.Prim.values.firstOrNull { value.contentEquals(it.tag) } ?: failWithUnknownTag(value)
}

internal class TagToMichelsonDataPrimConverter : Converter<ByteArray, MichelsonData.Prim> {
    override fun convert(value: ByteArray): MichelsonData.Prim =
        MichelsonData.Prim.values.firstOrNull { value.contentEquals(it.tag) } ?: failWithUnknownTag(value)
}

internal class TagToMichelsonInstructionPrimConverter : Converter<ByteArray, MichelsonInstruction.Prim> {
    override fun convert(value: ByteArray): MichelsonInstruction.Prim =
        MichelsonInstruction.Prim.values.firstOrNull { value.contentEquals(it.tag) } ?: failWithUnknownTag(value)
}

internal class TagToMichelsonTypePrimConverter : Converter<ByteArray, MichelsonType.Prim> {
    override fun convert(value: ByteArray): MichelsonType.Prim =
        MichelsonType.Prim.values.firstOrNull { value.contentEquals(it.tag) } ?: failWithUnknownTag(value)
}

internal class TagToMichelsonComparableTypePrimConverter : Converter<ByteArray, MichelsonComparableType.Prim> {
    override fun convert(value: ByteArray): MichelsonComparableType.Prim =
        MichelsonComparableType.Prim.values.firstOrNull { value.contentEquals(it.tag) } ?: failWithUnknownTag(value)
}

private fun failWithUnknownTag(tag: ByteArray): Nothing =
    failWithIllegalArgument("Unknown Michelson prim tag: \"${tag.joinToString(prefix = "[", postfix = "]")}\".")