package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.consumeAt
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.toBigInt
import it.airgap.tezos.core.internal.utils.toZarithNatural
import it.airgap.tezos.core.type.zarith.ZarithNatural

@InternalTezosSdkApi
public class ZarithNaturalBytesCoder : ConsumingBytesCoder<ZarithNatural> {
    override fun encode(value: ZarithNatural): ByteArray = encode(value.toBigInt(), byteArrayOf())
    override fun decode(value: ByteArray): ZarithNatural = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): ZarithNatural {
        if (value.isEmpty()) failWithInvalidNaturalNumberBytes(value)
        val int = decode(value, BigInt.valueOf(0))

        return int.toZarithNatural()
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

    private fun failWithInvalidNaturalNumberBytes(value: MutableList<Byte>): Nothing = failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Zarith natural number bytes.")
}