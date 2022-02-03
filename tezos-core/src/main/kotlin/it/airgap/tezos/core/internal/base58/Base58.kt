package it.airgap.tezos.core.internal.base58

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.tail
import it.airgap.tezos.core.internal.utils.toHexString

@InternalTezosSdkApi
public class Base58 {
    private val bs58Regex = Regex("^[$ALPHABET]+$")

    private val bi0: BigInt by lazy { BigInt.valueOf(0) }
    private val biAlphabet: BigInt by lazy { BigInt.valueOf(ALPHABET.length) }

    public fun encode(bytes: ByteArray): String = bytesToBase58(bytes)

    public fun decode(base58: String): ByteArray {
        if (!base58.matches(bs58Regex)) failWithInvalidString()
        return bytesFromBase58(base58)
    }

    private fun bytesToBase58(bytes: ByteArray): String {
        if (bytes.isEmpty()) return ""

        val hex = bytes.toHexString()
        val base58 = bigIntToBase58("", hex.toBigInt()).reversed()

        val leadingZeros = bytes.takeWhile { it == 0.toByte() }.size

        return ALPHABET[0].toString().repeat(leadingZeros) + base58
    }

    private fun bytesFromBase58(base58: String): ByteArray {
        if (base58.isEmpty()) return ByteArray(0)

        val leadingZeros = base58.takeWhile { it == ALPHABET[0] }.length
        val chars = base58.substring(leadingZeros).toCharArray().toList()

        val bytes =
            if (chars.isNotEmpty()) base58ToBigInt(bi0, chars).toHexString().toByteArray()
            else ByteArray(0)

        return ByteArray(leadingZeros) { 0 } + bytes
    }

    private tailrec fun bigIntToBase58(acc: String, next: BigInt): String {
        if (next <= bi0) return acc

        val reminder = (next % biAlphabet).toInt()
        return bigIntToBase58(acc + ALPHABET[reminder % ALPHABET.length], next / biAlphabet)
    }

    private tailrec fun base58ToBigInt(acc: BigInt, next: List<Char>): BigInt {
        if (next.isEmpty()) return acc

        val char = next.first()
        val reminder = BigInt.valueOf(ALPHABET.indexOf(char))

        return base58ToBigInt(acc * biAlphabet + reminder, next.tail())
    }

    private fun failWithInvalidString(): Nothing = throw IllegalArgumentException("Base58 string contains invalid characters")

    public companion object {
        private const val ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
    }
}