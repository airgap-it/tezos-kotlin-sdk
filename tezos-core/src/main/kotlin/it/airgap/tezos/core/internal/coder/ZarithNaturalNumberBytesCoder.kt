package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.consumeAt
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument

public class ZarithNaturalNumberBytesCoder : ConsumingBytesCoder<BigInt> {
    override fun encode(value: BigInt): ByteArray {
        if (value < BigInt.valueOf(0)) failWithInvalidNaturalNumber(value)
        return encode(value, byteArrayOf())
    }

    override fun decode(value: ByteArray): BigInt = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): BigInt {
        if (value.isEmpty()) failWithInvalidNaturalNumberBytes(value)
        return decode(value, BigInt.valueOf(0))
    }

    private tailrec fun encode(value: BigInt, encoded: ByteArray): ByteArray {
        if (value == BigInt.valueOf(0)) return encoded

        val byte = value and 0b0111_1111
        val nextValue = value shr 7

        val sequenceMask = if (nextValue == BigInt.valueOf(0)) 0b0000_0000 else 0b1000_0000
        val encodedByte = byte or sequenceMask

        return encode(nextValue, encoded + encodedByte.toByte())
    }

    private tailrec fun decode(value: MutableList<Byte>, decoded: BigInt, shift: Int = 0): BigInt {
        val byte = value.consumeAt(0)?.toInt() ?: return decoded

        val part = BigInt.valueOf(byte and 0b0111_1111)
        val hasNext = byte and 0b1000_0000 == 0b1000_0000

        return decode(if (hasNext) value else mutableListOf(), decoded + (part shl shift), shift = shift + 7)
    }

    private fun failWithInvalidNaturalNumber(value: BigInt): Nothing = failWithIllegalArgument("Value ${value.toString(10)} is not a valid Zarith natural number.")
    private fun failWithInvalidNaturalNumberBytes(value: MutableList<Byte>): Nothing = failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Zarith natural number bytes.")
}