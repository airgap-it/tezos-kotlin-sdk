package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.type.BytesTag
import it.airgap.tezos.core.type.number.TezosInteger
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.consumeAt
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.consumeUntil
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.decodeConsumingFromBytes
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.decodeConsumingInt32
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.decodeConsumingList
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.decodeConsumingString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.encodeToBytes
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.failWithIllegalArgument
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.fromStringOrNull
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.fromTagOrNull
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.startsWith
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toCompactExpression
import it.airgap.tezos.michelson.internal.utils.second
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal class MichelineBytesCoder(
    stringToMichelsonPrimConverter: Converter<String, Michelson.Prim>,
    tagToMichelsonPrimConverter: Converter<ByteArray, Michelson.Prim>,
    michelineToCompactStringConverter: Converter<Micheline, String>,
    tezosIntegerBytesCoder: ConsumingBytesCoder<TezosInteger>,
) : ConsumingBytesCoder<Micheline> {
    private val literalBytesCoder: MichelineLiteralBytesCoder = MichelineLiteralBytesCoder(tezosIntegerBytesCoder)
    private val primitiveApplicationBytesCoder: MichelinePrimitiveApplicationBytesCoder = MichelinePrimitiveApplicationBytesCoder(
        stringToMichelsonPrimConverter,
        tagToMichelsonPrimConverter,
        michelineToCompactStringConverter,
        this,
    )
    private val sequenceBytesCoder: MichelineSequenceBytesCoder = MichelineSequenceBytesCoder(this)

    override fun encode(value: Micheline): ByteArray =
        when (value) {
            is MichelineLiteral -> literalBytesCoder.encode(value)
            is MichelinePrimitiveApplication -> primitiveApplicationBytesCoder.encode(value)
            is MichelineSequence -> sequenceBytesCoder.encode(value)
        }

    override fun decode(value: ByteArray): Micheline = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): Micheline =
        when {
            literalBytesCoder.recognizesTag(value) -> literalBytesCoder.decodeConsuming(value)
            primitiveApplicationBytesCoder.recognizesTag(value) -> primitiveApplicationBytesCoder.decodeConsuming(value)
            sequenceBytesCoder.recognizesTag(value) -> sequenceBytesCoder.decodeConsuming(value)
            else -> failWithUnknownTag()
        }

    private fun failWithUnknownTag(): Nothing = failWithIllegalArgument("Unknown Micheline encoding tag.")
}

private class MichelineLiteralBytesCoder(private val tezosIntegerBytesCoder: ConsumingBytesCoder<TezosInteger>) : Coder<MichelineLiteral, ByteArray> {
    override fun encode(value: MichelineLiteral): ByteArray =
        when (value) {
            is MichelineLiteral.Integer -> encodeInteger(value)
            is MichelineLiteral.String -> encodeString(value)
            is MichelineLiteral.Bytes -> encodeBytes(value)
        }

    override fun decode(value: ByteArray): MichelineLiteral = decodeConsuming(value.toMutableList())
    fun decodeConsuming(value: MutableList<Byte>): MichelineLiteral =
        when (Tag.recognize(value)) {
            Tag.Int -> decodeInteger(value)
            Tag.String -> decodeString(value)
            Tag.Bytes -> decodeBytes(value)
            else -> failWithUnknownTag()
        }

    fun recognizesTag(value: List<Byte>): Boolean =
        listOf(Tag.Int, Tag.String, Tag.Bytes).any { it == Tag.recognize(value) }

    private fun encodeInteger(value: MichelineLiteral.Integer): ByteArray = Tag.Int + TezosInteger(value.int).encodeToBytes(tezosIntegerBytesCoder)
    private fun encodeString(value: MichelineLiteral.String): ByteArray = Tag.String + encodeString(value.string)
    private fun encodeBytes(value: MichelineLiteral.Bytes): ByteArray = Tag.Bytes + encodeBytes(value.toByteArray())

    private fun encodeString(value: String): ByteArray {
        val bytes = value.encodeToBytes()
        return encodeBytes(bytes)
    }

    private fun encodeBytes(value: ByteArray): ByteArray {
        val length = value.size.encodeToBytes()
        return length + value
    }

    private fun decodeInteger(value: MutableList<Byte>): MichelineLiteral.Integer {
        requireConsumingTag(Tag.Int, value)

        if (value.isEmpty()) failWithInvalidEncodedInteger()
        val integer = TezosInteger.decodeConsumingFromBytes(value, tezosIntegerBytesCoder)

        return MichelineLiteral.Integer(integer.int)
    }

    private fun decodeString(value: MutableList<Byte>): MichelineLiteral.String {
        requireConsumingTag(Tag.String, value)

        val length = value.decodeConsumingInt32()
        require(value.size >= length) { "Invalid encoded MichelineLiteral.String value." }

        val string = value.decodeConsumingString(length)

        return MichelineLiteral.String(string)
    }

    private fun decodeBytes(value: MutableList<Byte>): MichelineLiteral.Bytes {
        requireConsumingTag(Tag.Bytes, value)

        val length = value.decodeConsumingInt32()
        require(value.size >= length) { "Invalid encoded MichelineLiteral.Bytes value." }

        val bytes = value.consumeAt(0 until length).toByteArray()

        return MichelineLiteral.Bytes(bytes)
    }

    private fun failWithInvalidEncodedInteger(): Nothing = failWithIllegalArgument("Invalid encoded MichelineLiteral.Integer value.")
    private fun failWithUnknownTag(): Nothing = failWithIllegalArgument("Unknown Micheline encoding tag.")
}

private class MichelinePrimitiveApplicationBytesCoder(
    private val stringToMichelsonPrimConverter: Converter<String, Michelson.Prim>,
    private val tagToMichelsonPrimConverter: Converter<ByteArray, Michelson.Prim>,
    private val michelineToCompactStringConverter: Converter<Micheline, String>,
    private val bytesCoder: MichelineBytesCoder,
) : Coder<MichelinePrimitiveApplication, ByteArray> {
    override fun encode(value: MichelinePrimitiveApplication): ByteArray =
        when {
            value.args.isEmpty() -> if (value.annots.isEmpty()) encodePrimNoArgsNoAnnots(value) else encodePrimNoArgsSomeAnnots(value)
            value.args.size == 1 -> if (value.annots.isEmpty()) encodePrim1ArgNoAnnots(value) else encodePrim1ArgSomeAnnots(value)
            value.args.size == 2 -> if (value.annots.isEmpty()) encodePrim2ArgsNoAnnots(value) else encodePrim2ArgsSomeAnnots(value)
            else -> encodePrimGeneric(value)
        }

    override fun decode(value: ByteArray): MichelinePrimitiveApplication = decodeConsuming(value.toMutableList())
    fun decodeConsuming(value: MutableList<Byte>): MichelinePrimitiveApplication =
        when (Tag.recognize(value)) {
            Tag.PrimNoArgsNoAnnots -> decodePrimNoArgsNoAnnots(value)
            Tag.PrimNoArgsSomeAnnots -> decodePrimNoArgsSomeAnnots(value)
            Tag.Prim1ArgNoAnnots -> decodePrim1ArgNoAnnots(value)
            Tag.Prim1ArgSomeAnnots -> decodePrim1ArgSomeAnnots(value)
            Tag.Prim2ArgsNoAnnots -> decodePrim2ArgsNoAnnots(value)
            Tag.Prim2ArgsSomeAnnots -> decodePrim2ArgsSomeAnnots(value)
            Tag.PrimGeneric -> decodePrimGeneric(value)
            else -> failWithUnknownTag()
        }

    fun recognizesTag(value: List<Byte>): Boolean =
        listOf(
            Tag.PrimNoArgsNoAnnots,
            Tag.PrimNoArgsSomeAnnots,
            Tag.Prim1ArgNoAnnots,
            Tag.Prim1ArgSomeAnnots,
            Tag.Prim2ArgsNoAnnots,
            Tag.Prim2ArgsSomeAnnots,
            Tag.PrimGeneric,
        ).any { it == Tag.recognize(value) }

    private fun encodePrimNoArgsNoAnnots(value: MichelinePrimitiveApplication): ByteArray =
        Tag.PrimNoArgsNoAnnots + encodePrim(value)

    private fun encodePrimNoArgsSomeAnnots(value: MichelinePrimitiveApplication): ByteArray =
        Tag.PrimNoArgsSomeAnnots + encodePrim(value) + encodeAnnots(value)

    private fun encodePrim1ArgNoAnnots(value: MichelinePrimitiveApplication): ByteArray =
        Tag.Prim1ArgNoAnnots + encodePrim(value) + value.args.first().encodeToBytes(bytesCoder)

    private fun encodePrim1ArgSomeAnnots(value: MichelinePrimitiveApplication): ByteArray =
        Tag.Prim1ArgSomeAnnots +
                encodePrim(value) +
                value.args.first().encodeToBytes(bytesCoder) +
                encodeAnnots(value)

    private fun encodePrim2ArgsNoAnnots(value: MichelinePrimitiveApplication): ByteArray =
        Tag.Prim2ArgsNoAnnots + encodePrim(value) + value.args.first().encodeToBytes(bytesCoder) + value.args.second().encodeToBytes(bytesCoder)

    private fun encodePrim2ArgsSomeAnnots(value: MichelinePrimitiveApplication): ByteArray =
        Tag.Prim2ArgsSomeAnnots +
                encodePrim(value) +
                value.args.first().encodeToBytes(bytesCoder) +
                value.args.second().encodeToBytes(bytesCoder) +
                encodeAnnots(value)

    private fun encodePrimGeneric(value: MichelinePrimitiveApplication): ByteArray =
        Tag.PrimGeneric + encodePrim(value) + encodeArgs(value) + encodeAnnots(value)

    private fun encodePrim(value: MichelinePrimitiveApplication): ByteArray =
        Michelson.Prim.fromStringOrNull(value.prim.value, stringToMichelsonPrimConverter)?.tag ?: failWithUnknownPrimitiveApplication(value)

    private fun encodeArgs(value: MichelinePrimitiveApplication): ByteArray {
        val bytes = value.args.encodeToBytes(bytesCoder::encode)
        val length = bytes.size.encodeToBytes()

        return length + bytes
    }

    private fun encodeAnnots(value: MichelinePrimitiveApplication): ByteArray {
        val bytes = value.annots.joinToString(" ") { it.value }.encodeToBytes()
        val length = bytes.size.encodeToBytes()

        return length + bytes
    }

    private fun decodePrimNoArgsNoAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        requireConsumingTag(Tag.PrimNoArgsNoAnnots, value)

        val prim = decodePrim(value)

        return MichelinePrimitiveApplication(prim)
    }

    private fun decodePrimNoArgsSomeAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        requireConsumingTag(Tag.PrimNoArgsSomeAnnots, value)

        val prim = decodePrim(value)
        val annots = decodeAnnots(value)

        return MichelinePrimitiveApplication(prim, annots = annots)
    }

    private fun decodePrim1ArgNoAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        requireConsumingTag(Tag.Prim1ArgNoAnnots, value)

        val prim = decodePrim(value)
        val arg = Micheline.decodeConsumingFromBytes(value, bytesCoder)

        return MichelinePrimitiveApplication(prim, args = listOf(arg))
    }

    private fun decodePrim1ArgSomeAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        requireConsumingTag(Tag.Prim1ArgSomeAnnots, value)

        val prim = decodePrim(value)
        val arg = Micheline.decodeConsumingFromBytes(value, bytesCoder)
        val annots = decodeAnnots(value)

        return MichelinePrimitiveApplication(prim, args = listOf(arg), annots)
    }

    private fun decodePrim2ArgsNoAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        requireConsumingTag(Tag.Prim2ArgsNoAnnots, value)

        val prim = decodePrim(value)

        val arg1 = Micheline.decodeConsumingFromBytes(value, bytesCoder)
        val arg2 = Micheline.decodeConsumingFromBytes(value, bytesCoder)

        return MichelinePrimitiveApplication(prim, args = listOf(arg1, arg2))
    }

    private fun decodePrim2ArgsSomeAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        requireConsumingTag(Tag.Prim2ArgsSomeAnnots, value)

        val prim = decodePrim(value)

        val arg1 = Micheline.decodeConsumingFromBytes(value, bytesCoder)
        val arg2 = Micheline.decodeConsumingFromBytes(value, bytesCoder)
        val annots = decodeAnnots(value)

        return MichelinePrimitiveApplication(prim, args = listOf(arg1, arg2), annots)
    }

    private fun decodePrimGeneric(value: MutableList<Byte>): MichelinePrimitiveApplication {
        requireConsumingTag(Tag.PrimGeneric, value)

        val prim = decodePrim(value)

        val args = decodeArgs(value)
        val annots = decodeAnnots(value)

        return MichelinePrimitiveApplication(prim, args, annots)
    }

    private fun decodePrim(value: MutableList<Byte>): Michelson.Prim {
        val byte = value.consumeAt(0) ?: failWithInvalidEncodedPrimitiveApplication()
        return Michelson.Prim.fromTagOrNull(byteArrayOf(byte), tagToMichelsonPrimConverter) ?: failWithUnknownPrimitiveApplication(byte.toInt())
    }

    private fun decodeArgs(value: MutableList<Byte>): List<Micheline> {
        val argsLength = value.decodeConsumingInt32()
        return value.consumeUntil(argsLength).decodeConsumingList(decoder = bytesCoder::decodeConsuming)
    }

    private fun decodeAnnots(value: MutableList<Byte>): List<String> {
        val length = value.decodeConsumingInt32()
        return value.decodeConsumingString(length)
            .split(" ")
            .filterNot { it.isBlank() }
    }

    private fun failWithUnknownPrimitiveApplication(primitiveApplication: MichelinePrimitiveApplication): Nothing =
        failWithIllegalArgument("Unknown Micheline Primitive Application: ${primitiveApplication.toCompactExpression(michelineToCompactStringConverter)}.")

    private fun failWithUnknownPrimitiveApplication(tag: Int): Nothing =
        failWithIllegalArgument("Unknown Micheline Primitive Application: $tag.")

    private fun failWithInvalidEncodedPrimitiveApplication(): Nothing = failWithIllegalArgument("Invalid encoded MichelinePrimitiveApplication value.")
    private fun failWithUnknownTag(): Nothing = failWithIllegalArgument("Unknown Micheline encoding tag.")
}

private class MichelineSequenceBytesCoder(private val bytesCoder: MichelineBytesCoder) : Coder<MichelineSequence, ByteArray> {
    override fun encode(value: MichelineSequence): ByteArray =
        Tag.Sequence + encodeNodes(value.nodes)

    override fun decode(value: ByteArray): MichelineSequence = decodeConsuming(value.toMutableList())
    fun decodeConsuming(value: MutableList<Byte>): MichelineSequence {
        requireConsumingTag(Tag.Sequence, value)

        val nodes = decodeNodes(value)

        return MichelineSequence(nodes)
    }

    fun recognizesTag(value: List<Byte>): Boolean = listOf(Tag.Sequence).any { it == Tag.recognize(value) }

    private fun encodeNodes(nodes: List<Micheline>): ByteArray {
        val bytes = nodes.encodeToBytes(bytesCoder::encode)
        val length = bytes.size.encodeToBytes()

        return length + bytes
    }

    private fun decodeNodes(value: MutableList<Byte>): List<Micheline> {
        val length = value.decodeConsumingInt32()
        return value.consumeUntil(length).decodeConsumingList(decoder = bytesCoder::decodeConsuming)
    }
}

private fun requireConsumingTag(expected: Tag, bytes: MutableList<Byte>) {
    if (Tag.recognize(bytes) != expected) failWithInvalidTag(expected)
    bytes.consumeUntil(expected.value.size)
}

private enum class Tag(override val value: ByteArray) : BytesTag {
    Int(byteArrayOf(0)),
    String(byteArrayOf(1)),
    Sequence(byteArrayOf(2)),
    PrimNoArgsNoAnnots(byteArrayOf(3)),
    PrimNoArgsSomeAnnots(byteArrayOf(4)),
    Prim1ArgNoAnnots(byteArrayOf(5)),
    Prim1ArgSomeAnnots(byteArrayOf(6)),
    Prim2ArgsNoAnnots(byteArrayOf(7)),
    Prim2ArgsSomeAnnots(byteArrayOf(8)),
    PrimGeneric(byteArrayOf(9)),
    Bytes(byteArrayOf(10));

    companion object {
        fun recognize(bytes: List<Byte>): Tag? =
            if (bytes.isEmpty()) null
            else values().find { bytes.startsWith(it.value.toList()) }
    }
}

private fun failWithInvalidTag(expected: Tag): Nothing =
    failWithIllegalArgument("Invalid tag, encoded value is not Micheline ${expected::class.qualifiedName}.")