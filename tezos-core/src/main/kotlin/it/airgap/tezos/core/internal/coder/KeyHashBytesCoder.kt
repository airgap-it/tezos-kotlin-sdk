package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.type.PrefixTag
import it.airgap.tezos.core.internal.utils.consumeAt
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.startsWith

public class KeyHashBytesCoder(base58BytesCoder: Base58BytesCoder) : Base58PrefixedBytesCoder(base58BytesCoder) {
    override fun tag(prefix: Tezos.Prefix): PrefixTag? = KeyHashTag.fromPrefix(prefix)
    override fun tag(bytes: ByteArray): PrefixTag? = KeyHashTag.recognize(bytes)
    override fun tag(bytes: MutableList<Byte>): PrefixTag? = KeyHashTag.recognize(bytes)
    override fun tagConsuming(bytes: MutableList<Byte>): PrefixTag? = tag(bytes)?.also { bytes.consumeAt(0 until it.value.size) }

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidKeyHash(value)
    private fun failWithInvalidKeyHash(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos key hash.")

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidKeyHashBytes(value.joinToString(prefix = "[", postfix = "]"))
    override fun failWithInvalidValue(value: MutableList<Byte>): Nothing = failWithInvalidKeyHashBytes(value.joinToString(prefix = "[", postfix = "]"))

    private fun failWithInvalidKeyHashBytes(value: String): Nothing = failWithIllegalArgument("Bytes `$value` are not valid Tezos key hash bytes.")
}

private enum class KeyHashTag(override val value: ByteArray, override val prefix: Tezos.Prefix) : PrefixTag {
    Tz1(byteArrayOf(0), Tezos.Prefix.Ed25519PublicKeyHash),
    Tz2(byteArrayOf(1), Tezos.Prefix.Secp256K1PublicKeyHash),
    Tz3(byteArrayOf(2), Tezos.Prefix.P256PublicKeyHash);

    companion object {
        fun fromPrefix(prefix: Tezos.Prefix): KeyHashTag? =
           values().find { it.prefix == prefix }

        fun recognize(bytes: ByteArray): KeyHashTag? =
            if (bytes.isEmpty()) null
            else values().find { bytes.startsWith(it.value) && bytes.size == it.prefix.dataLength + it.value.size }

        fun recognize(bytes: MutableList<Byte>): KeyHashTag? =
            if (bytes.isEmpty()) null
            else values().find { bytes.startsWith(it.value) && bytes.size >= it.prefix.dataLength + it.value.size }
    }
}