package it.airgap.tezos.core.internal.coder.number

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.consumeAt
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.toBigInt
import it.airgap.tezos.core.internal.utils.toTezosNatural
import it.airgap.tezos.core.type.number.TezosNatural

internal class TezosNaturalBytesCoder : ConsumingBytesCoder<TezosNatural> {
    override fun encode(value: TezosNatural): ByteArray =
        when (value.toBigInt()) {
            BigInt.zero -> byteArrayOf(0)
            else -> encode(value.toBigInt(), byteArrayOf())
        }

    override fun decode(value: ByteArray): TezosNatural = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): TezosNatural {
        if (value.isEmpty()) failWithInvalidNaturalNumberBytes(value)
        val int = decode(value, BigInt.valueOf(0))

        return int.toTezosNatural()
    }

    private tailrec fun encode(value: BigInt, encoded: ByteArray): ByteArray {
        if (value == BigInt.zero) return encoded

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

    private fun failWithInvalidNaturalNumberBytes(value: MutableList<Byte>): Nothing = failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos natural number bytes.")
}