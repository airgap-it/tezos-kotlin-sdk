package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.ZarithIntegerBytesCoder
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.type.BytesTag
import it.airgap.tezos.core.internal.utils.*
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.fromStringOrNull
import it.airgap.tezos.michelson.fromTagOrNull
import it.airgap.tezos.michelson.internal.converter.MichelineToCompactStringConverter
import it.airgap.tezos.michelson.internal.converter.StringToMichelsonGrammarTypeConverter
import it.airgap.tezos.michelson.internal.converter.TagToMichelsonGrammarTypeConverter
import it.airgap.tezos.michelson.internal.utils.second
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.toCompactExpression

internal class MichelineBytesCoder(
    stringToMichelsonGrammarTypeConverter: StringToMichelsonGrammarTypeConverter,
    tagToMichelsonGrammarTypeConverter: TagToMichelsonGrammarTypeConverter,
    michelineToCompactStringConverter: MichelineToCompactStringConverter,
    zarithIntegerBytesCoder: ZarithIntegerBytesCoder,
) : ConsumingBytesCoder<MichelineNode> {
    private val literalBytesCoder: MichelineLiteralBytesCoder = MichelineLiteralBytesCoder(zarithIntegerBytesCoder)
    private val primitiveApplicationBytesCoder: MichelinePrimitiveApplicationBytesCoder = MichelinePrimitiveApplicationBytesCoder(
        stringToMichelsonGrammarTypeConverter,
        tagToMichelsonGrammarTypeConverter,
        michelineToCompactStringConverter,
        this,
    )
    private val sequenceBytesCoder: MichelineSequenceBytesCoder = MichelineSequenceBytesCoder(this)

    override fun encode(value: MichelineNode): ByteArray =
        when (value) {
            is MichelineLiteral -> literalBytesCoder.encode(value)
            is MichelinePrimitiveApplication -> primitiveApplicationBytesCoder.encode(value)
            is MichelineSequence -> sequenceBytesCoder.encode(value)
        }

    override fun decode(value: ByteArray): MichelineNode = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): MichelineNode =
        when {
            literalBytesCoder.recognizesTag(value) -> literalBytesCoder.decodeConsuming(value)
            primitiveApplicationBytesCoder.recognizesTag(value) -> primitiveApplicationBytesCoder.decodeConsuming(value)
            sequenceBytesCoder.recognizesTag(value) -> sequenceBytesCoder.decodeConsuming(value)
            else -> failWithUnknownTag()
        }

    private fun failWithUnknownTag(): Nothing = failWithIllegalArgument("Unknown Micheline encoding tag.")
}

private class MichelineLiteralBytesCoder(private val zarithIntegerBytesCoder: ZarithIntegerBytesCoder) : Coder<MichelineLiteral, ByteArray> {
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

    private fun encodeInteger(value: MichelineLiteral.Integer): ByteArray = Tag.Int + zarithIntegerBytesCoder.encode(BigInt.valueOf(value.int))
    private fun encodeString(value: MichelineLiteral.String): ByteArray = Tag.String + encodeString(value.string)
    private fun encodeBytes(value: MichelineLiteral.Bytes): ByteArray = Tag.Bytes + encodeBytes(value.toByteArray())

    private fun encodeString(value: String): ByteArray {
        val bytes = value.toByteArray(charset = Charsets.UTF_8)
        val length = BigInt.valueOf(bytes.size).toByteArray()

        return length.asInt32Encoded() + bytes
    }

    private fun encodeBytes(value: ByteArray): ByteArray {
        val length = BigInt.valueOf(value.size).toByteArray()

        return length.asInt32Encoded() + value
    }

    private fun decodeInteger(value: MutableList<Byte>): MichelineLiteral.Integer {
        require(Tag.recognize(value) == Tag.Int) { "Invalid tag, encoded value is not MichelineLiteral.Integer." }
        value.consumeAt(0)

        if (value.isEmpty()) failWithInvalidEncodedInteger()
        val int = zarithIntegerBytesCoder.decodeConsuming(value)

        return MichelineLiteral.Integer(int.toString(10))
    }

    private fun decodeString(value: MutableList<Byte>): MichelineLiteral.String {
        require(Tag.recognize(value) == Tag.String) { "Invalid tag, encoded value is not MichelineLiteral.String." }
        value.consumeAt(0)

        val length = BigInt.valueOf(value.consumeAt(0 until 4).toHexString()).toInt()
        require(value.size >= length) { "Invalid encoded MichelineLiteral.String value." }

        val string = value.consumeAt(0 until length).toByteArray().decodeToString()

        return MichelineLiteral.String(string)
    }

    private fun decodeBytes(value: MutableList<Byte>): MichelineLiteral.Bytes {
        require(Tag.recognize(value) == Tag.Bytes) { "Invalid tag, encoded value is not MichelineLiteral.Bytes." }
        value.consumeAt(0)

        val length = BigInt.valueOf(value.consumeAt(0 until 4).toHexString()).toInt()
        require(value.size >= length) { "Invalid encoded MichelineLiteral.Bytes value." }

        val bytes = value.consumeAt(0 until length).toByteArray()

        return MichelineLiteral.Bytes(bytes)
    }

    private fun failWithInvalidEncodedInteger(): Nothing = throw IllegalArgumentException("Invalid encoded MichelineLiteral.Integer value.")
    private fun failWithUnknownTag(): Nothing = failWithIllegalArgument("Unknown Micheline encoding tag.")
}

private class MichelinePrimitiveApplicationBytesCoder(
    private val stringToMichelsonGrammarTypeConverter: StringToMichelsonGrammarTypeConverter,
    private val tagToMichelsonGrammarTypeConverter: TagToMichelsonGrammarTypeConverter,
    private val michelineToCompactStringConverter: MichelineToCompactStringConverter,
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
        Tag.Prim1ArgNoAnnots + encodePrim(value) + bytesCoder.encode(value.args.first())

    private fun encodePrim1ArgSomeAnnots(value: MichelinePrimitiveApplication): ByteArray =
        Tag.Prim1ArgSomeAnnots +
                encodePrim(value) +
                bytesCoder.encode(value.args.first()) +
                encodeAnnots(value)

    private fun encodePrim2ArgsNoAnnots(value: MichelinePrimitiveApplication): ByteArray =
        Tag.Prim2ArgsNoAnnots + encodePrim(value) + bytesCoder.encode(value.args.first()) + bytesCoder.encode(value.args.second())

    private fun encodePrim2ArgsSomeAnnots(value: MichelinePrimitiveApplication): ByteArray =
        Tag.Prim2ArgsSomeAnnots +
                encodePrim(value) +
                bytesCoder.encode(value.args.first()) +
                bytesCoder.encode(value.args.second()) +
                encodeAnnots(value)

    private fun encodePrimGeneric(value: MichelinePrimitiveApplication): ByteArray =
        Tag.PrimGeneric + encodePrim(value) + encodeArgs(value) + encodeAnnots(value)

    private fun encodePrim(value: MichelinePrimitiveApplication): ByteArray {
        val tag = Michelson.GrammarType.fromStringOrNull(value.prim.value, stringToMichelsonGrammarTypeConverter)?.tag ?: failWithUnknownPrimitiveApplication(value)
        return byteArrayOf(tag.toByte())
    }

    private fun encodeArgs(value: MichelinePrimitiveApplication): ByteArray {
        val bytes = value.args.fold(byteArrayOf()) { acc, node ->  acc + bytesCoder.encode(node) }
        val length = BigInt.valueOf(bytes.size).toByteArray()

        return length.asInt32Encoded() + bytes
    }

    private fun encodeAnnots(value: MichelinePrimitiveApplication): ByteArray {
        val bytes = value.annots.joinToString(" ") { it.value }.toByteArray(charset = Charsets.UTF_8)
        val length = BigInt.valueOf(bytes.size).toByteArray()

        return length.asInt32Encoded() + bytes
    }

    private fun decodePrimNoArgsNoAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        require(Tag.recognize(value) == Tag.PrimNoArgsNoAnnots) { "Invalid tag, encoded value is not MichelinePrimitiveApplication with no arguments and no annotations." }
        value.consumeAt(0)

        val primByte = value.consumeAt(0) ?: failWithInvalidEncodedPrimitiveApplication()
        val prim = Michelson.GrammarType.fromTagOrNull(primByte.toUByte().toInt(), tagToMichelsonGrammarTypeConverter) ?: failWithUnknownPrimitiveApplication(primByte.toInt())

        return MichelinePrimitiveApplication(prim)
    }

    private fun decodePrimNoArgsSomeAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        require(Tag.recognize(value) == Tag.PrimNoArgsSomeAnnots) { "Invalid tag, encoded value is not MichelinePrimitiveApplication with no arguments and some annotations." }
        value.consumeAt(0)

        val primByte = value.consumeAt(0) ?: failWithInvalidEncodedPrimitiveApplication()
        val prim = Michelson.GrammarType.fromTagOrNull(primByte.toUByte().toInt(), tagToMichelsonGrammarTypeConverter) ?: failWithUnknownPrimitiveApplication(primByte.toInt())
        val annots = decodeAnnots(value)

        return MichelinePrimitiveApplication(prim, annots = annots)
    }

    private fun decodePrim1ArgNoAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        require(Tag.recognize(value) == Tag.Prim1ArgNoAnnots) { "Invalid tag, encoded value is not MichelinePrimitiveApplication with 1 argument and no annotations." }
        value.consumeAt(0)

        val prim = decodePrim(value)
        val arg = bytesCoder.decodeConsuming(value)

        return MichelinePrimitiveApplication(prim, args = listOf(arg))
    }

    private fun decodePrim1ArgSomeAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        require(Tag.recognize(value) == Tag.Prim1ArgSomeAnnots) { "Invalid tag, encoded value is not MichelinePrimitiveApplication with 1 argument and some annotations." }
        value.consumeAt(0)

        val prim = decodePrim(value)
        val arg = bytesCoder.decodeConsuming(value)
        val annots = decodeAnnots(value)

        return MichelinePrimitiveApplication(prim, args = listOf(arg), annots)
    }

    private fun decodePrim2ArgsNoAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        require(Tag.recognize(value) == Tag.Prim2ArgsNoAnnots) { "Invalid tag, encoded value is not MichelinePrimitiveApplication with 2 arguments and no annotations." }
        value.consumeAt(0)

        val prim = decodePrim(value)

        val arg1 = bytesCoder.decodeConsuming(value)
        val arg2 = bytesCoder.decodeConsuming(value)

        return MichelinePrimitiveApplication(prim, args = listOf(arg1, arg2))
    }

    private fun decodePrim2ArgsSomeAnnots(value: MutableList<Byte>): MichelinePrimitiveApplication {
        require(Tag.recognize(value) == Tag.Prim2ArgsSomeAnnots) { "Invalid tag, encoded value is not MichelinePrimitiveApplication with 2 arguments and some annotations." }
        value.consumeAt(0)

        val prim = decodePrim(value)

        val arg1 = bytesCoder.decodeConsuming(value)
        val arg2 = bytesCoder.decodeConsuming(value)
        val annots = decodeAnnots(value)

        return MichelinePrimitiveApplication(prim, args = listOf(arg1, arg2), annots)
    }

    private fun decodePrimGeneric(value: MutableList<Byte>): MichelinePrimitiveApplication {
        require(Tag.recognize(value) == Tag.PrimGeneric) { "Invalid tag, encoded value is not generic MichelinePrimitiveApplication." }
        value.consumeAt(0)

        val prim = decodePrim(value)

        val argsLength = BigInt.valueOf(value.consumeAt(0 until 4).toHexString()).toInt()
        val args = decodeArgs(value.consumeAt(0 until argsLength))
        val annots = decodeAnnots(value)

        return MichelinePrimitiveApplication(prim, args, annots)
    }

    private fun decodePrim(value: MutableList<Byte>): Michelson.GrammarType {
        val byte = value.consumeAt(0) ?: failWithInvalidEncodedPrimitiveApplication()
        return Michelson.GrammarType.fromTagOrNull(byte.toUByte().toInt(), tagToMichelsonGrammarTypeConverter) ?: failWithUnknownPrimitiveApplication(byte.toInt())
    }

    private tailrec fun decodeArgs(value: MutableList<Byte>, decoded: List<MichelineNode> = emptyList()): List<MichelineNode> {
        if (value.isEmpty()) return decoded
        val arg = bytesCoder.decodeConsuming(value)

        return decodeArgs(value, decoded + arg)
    }

    private fun decodeAnnots(value: MutableList<Byte>): List<String> {
        val length = BigInt.valueOf(value.consumeAt(0 until 4).toHexString()).toInt()
        return value.consumeAt(0 until length).toByteArray().decodeToString()
            .split(" ")
            .filterNot { it.isBlank() }
    }

    private fun failWithUnknownPrimitiveApplication(primitiveApplication: MichelinePrimitiveApplication): Nothing =
        failWithIllegalArgument("Unknown Micheline Primitive Application: ${primitiveApplication.toCompactExpression(michelineToCompactStringConverter)}.")

    private fun failWithUnknownPrimitiveApplication(tag: Int): Nothing =
        failWithIllegalArgument("Unknown Micheline Primitive Application: $tag.")

    private fun failWithInvalidEncodedPrimitiveApplication(): Nothing = throw IllegalArgumentException("Invalid encoded MichelinePrimitiveApplication value.")
    private fun failWithUnknownTag(): Nothing = failWithIllegalArgument("Unknown Micheline encoding tag.")
}

private class MichelineSequenceBytesCoder(private val bytesCoder: MichelineBytesCoder) : Coder<MichelineSequence, ByteArray> {
    override fun encode(value: MichelineSequence): ByteArray {
        val bytes = encodeNodes(value.nodes)
        val length = BigInt.valueOf(bytes.size).toByteArray()

        return Tag.Sequence + length.asInt32Encoded() + bytes
    }

    override fun decode(value: ByteArray): MichelineSequence = decodeConsuming(value.toMutableList())
    fun decodeConsuming(value: MutableList<Byte>): MichelineSequence {
        require(Tag.recognize(value) == Tag.Sequence) { "Invalid tag, encoded value is not MichelineSequence." }
        value.consumeAt(0)

        val length = BigInt.valueOf(value.consumeAt(0 until 4).toHexString()).toInt()
        val nodes = decodeNodes(value.consumeAt(0 until length))

        return MichelineSequence(nodes)
    }

    fun recognizesTag(value: List<Byte>): Boolean = listOf(Tag.Sequence).any { it == Tag.recognize(value) }

    private fun encodeNodes(nodes: List<MichelineNode>): ByteArray =
        nodes.fold(byteArrayOf()) { acc, node -> acc + bytesCoder.encode(node) }

    private tailrec fun decodeNodes(value: MutableList<Byte>, decoded: List<MichelineNode> = emptyList()): List<MichelineNode> {
        if (value.isEmpty()) return decoded
        val node = bytesCoder.decodeConsuming(value)

        return decodeNodes(value, decoded + node)
    }
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
