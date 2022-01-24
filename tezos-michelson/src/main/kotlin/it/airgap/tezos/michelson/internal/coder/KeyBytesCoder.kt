package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coder.Base58BytesCoder
import it.airgap.tezos.core.internal.coder.Base58PrefixedBytesCoder
import it.airgap.tezos.core.internal.type.PrefixTag
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.startsWith

internal class KeyBytesCoder(base58BytesCoder: Base58BytesCoder) : Base58PrefixedBytesCoder(base58BytesCoder) {
    override fun tag(prefix: Tezos.Prefix): PrefixTag? = KeyTag.fromPrefix(prefix)
    override fun tag(bytes: ByteArray): PrefixTag? = KeyTag.recognize(bytes)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidKey(value)
    private fun failWithInvalidKey(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos key.")

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidKeyBytes(value)
    private fun failWithInvalidKeyBytes(value: ByteArray): Nothing = failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos key bytes.")
}

private enum class KeyTag(override val value: ByteArray, override val prefix: Tezos.Prefix) : PrefixTag {
    Edpk(byteArrayOf(0), Tezos.Prefix.Ed25519PublicKey),
    Sppk(byteArrayOf(1), Tezos.Prefix.Secp256K1PublicKey),
    P2pk(byteArrayOf(2), Tezos.Prefix.P256PublicKey);

    companion object {
        fun fromPrefix(prefix: Tezos.Prefix): KeyTag? =
            values().find { it.prefix == prefix }

        fun recognize(bytes: ByteArray): KeyTag? =
            if (bytes.isEmpty()) null
            else values().find { bytes.startsWith(it.value) && bytes.size == it.prefix.dataLength + it.value.size }
    }
}