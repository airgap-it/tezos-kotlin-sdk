package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coder.Base58BytesCoder
import it.airgap.tezos.core.internal.coder.StringPrefixedBytesCoder
import it.airgap.tezos.core.internal.type.ByteTag
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument

public class KeyHashBytesCoder(base58BytesCoder: Base58BytesCoder) : StringPrefixedBytesCoder(base58BytesCoder) {
    override fun decode(value: ByteArray): String {
        TODO("Not yet implemented")
    }

    override fun decode(value: ByteArray, prefix: Tezos.Prefix?): String {
        TODO("Not yet implemented")
    }

    override fun tag(prefix: Tezos.Prefix?): ByteTag? = KeyHashTag.fromPrefix(prefix)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidKeyHash(value)
    private fun failWithInvalidKeyHash(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos key hash.")
}

private enum class KeyHashTag(override val value: Byte) : ByteTag {
    Tz1(0),
    Tz2(1),
    Tz3(2);

    companion object {
        fun fromPrefix(prefix: Tezos.Prefix?): KeyHashTag? =
            when (prefix) {
                Tezos.Prefix.Ed25519PublicKeyHash -> Tz1
                Tezos.Prefix.Secp256K1PublicKeyHash -> Tz2
                Tezos.Prefix.P256PublicKeyHash -> Tz3
                else -> null
            }
    }
}