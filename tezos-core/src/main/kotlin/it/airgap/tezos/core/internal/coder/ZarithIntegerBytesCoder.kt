package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.consumeAt
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument

public class ZarithIntegerBytesCoder(private val zarithNaturalNumberBytesCoder: ZarithNaturalNumberBytesCoder) : ConsumingBytesCoder<BigInt> {
    override fun encode(value: BigInt): ByteArray {
        val abs = value.abs()

        val byte = abs and 0b0011_1111
        val nextValue = abs shr 6

        val sequenceMask = if (nextValue == BigInt.valueOf(0)) 0b0000_0000 else 0b1000_0000
        val signMask = if (value < BigInt.valueOf(0)) 0b0100_0000 else 0b0000_0000
        val encodedByte = byte or sequenceMask or signMask

        return byteArrayOf(encodedByte.toByte()) + zarithNaturalNumberBytesCoder.encode(nextValue)
    }

    override fun decode(value: ByteArray): BigInt = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): BigInt {
        val byte = value.consumeAt(0)?.toInt() ?: failWithInvalidIntegerBytes(value)

        val part = BigInt.valueOf(byte and 0b0011_1111)
        val sign = if (byte and 0b0100_0000 == 0b0100_0000) -1 else 1
        val hasNext = byte and 0b1000_0000 == 0b1000_0000

        val abs = if (hasNext) part + (zarithNaturalNumberBytesCoder.decodeConsuming(value) shl 6) else part

        return abs * sign
    }

    private fun failWithInvalidIntegerBytes(value: MutableList<Byte>): Nothing = failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Zarith integer bytes.")
}