package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.*
import it.airgap.tezos.core.type.zarith.ZarithInteger
import it.airgap.tezos.core.type.zarith.ZarithNatural

internal class ZarithIntegerBytesCoder(private val zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural>) : ConsumingBytesCoder<ZarithInteger> {
    override fun encode(value: ZarithInteger): ByteArray {
        val int = value.toBigInt()
        val abs = int.abs()

        val byte = abs and 0b0011_1111
        val nextValue = abs shr 6

        val sequenceMask = if (nextValue == BigInt.zero) 0b0000_0000 else 0b1000_0000
        val signMask = if (int < BigInt.valueOf(0)) 0b0100_0000 else 0b0000_0000
        val encodedByte = byte or sequenceMask or signMask

        val nextValueEncoded = if (nextValue > BigInt.zero) zarithNaturalBytesCoder.encode(nextValue.toZarithNatural()) else byteArrayOf()

        return byteArrayOf(encodedByte.toByte()) + nextValueEncoded
    }

    override fun decode(value: ByteArray): ZarithInteger = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): ZarithInteger {
        val byte = value.consumeAt(0)?.toInt() ?: failWithInvalidIntegerBytes(value)

        val part = BigInt.valueOf(byte and 0b0011_1111)
        val sign = if (byte and 0b0100_0000 == 0b0100_0000) -1 else 1
        val hasNext = byte and 0b1000_0000 == 0b1000_0000

        val abs = if (hasNext) part + (zarithNaturalBytesCoder.decodeConsuming(value).toBigInt() shl 6) else part
        val int = abs * sign

        return int.toZarithInteger()
    }

    private fun failWithInvalidIntegerBytes(value: MutableList<Byte>): Nothing = failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Zarith integer bytes.")
}