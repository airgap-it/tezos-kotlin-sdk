package it.airgap.tezos.core.internal.coder.number

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.*
import it.airgap.tezos.core.type.number.TezosInteger
import it.airgap.tezos.core.type.number.TezosNatural

internal class TezosIntegerBytesCoder(private val tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>) :
    ConsumingBytesCoder<TezosInteger> {
    override fun encode(value: TezosInteger): ByteArray {
        val int = value.toBigInt()
        val abs = int.abs()

        val byte = abs and 0b0011_1111
        val nextValue = abs shr 6

        val sequenceMask = if (nextValue == BigInt.zero) 0b0000_0000 else 0b1000_0000
        val signMask = if (int < BigInt.valueOf(0)) 0b0100_0000 else 0b0000_0000
        val encodedByte = byte or sequenceMask or signMask

        val nextValueEncoded = if (nextValue > BigInt.zero) tezosNaturalBytesCoder.encode(nextValue.toTezosNatural()) else byteArrayOf()

        return byteArrayOf(encodedByte.toByte()) + nextValueEncoded
    }

    override fun decode(value: ByteArray): TezosInteger = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): TezosInteger {
        val byte = value.consumeAt(0)?.toInt() ?: failWithInvalidIntegerBytes(value)

        val part = BigInt.valueOf(byte and 0b0011_1111)
        val sign = if (byte and 0b0100_0000 == 0b0100_0000) -1 else 1
        val hasNext = byte and 0b1000_0000 == 0b1000_0000

        val abs = if (hasNext) part + (tezosNaturalBytesCoder.decodeConsuming(value).toBigInt() shl 6) else part
        val int = abs * sign

        return int.toTezosInteger()
    }

    private fun failWithInvalidIntegerBytes(value: MutableList<Byte>): Nothing = failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos integer bytes.")
}