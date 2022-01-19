package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coder.Base58BytesCoder
import it.airgap.tezos.core.internal.coder.StringPrefixedBytesCoder
import it.airgap.tezos.core.internal.type.ByteTag
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument

internal class KeyBytesCoder(base58BytesCoder: Base58BytesCoder) : StringPrefixedBytesCoder(base58BytesCoder) {
    override fun decode(value: ByteArray): String {
        TODO("Not yet implemented")
    }

    override fun decode(value: ByteArray, prefix: Tezos.Prefix?): String {
        TODO("Not yet implemented")
    }

    override fun tag(prefix: Tezos.Prefix?): ByteTag? = KeyTag.fromPrefix(prefix)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidKey(value)
    private fun failWithInvalidKey(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos key.")
}

private enum class KeyTag(override val value: Byte) : ByteTag {
    Edpk(0),
    Sppk(1),
    P2pk(2);

    companion object {
        fun fromPrefix(prefix: Tezos.Prefix?): KeyTag? =
            when (prefix) {
                Tezos.Prefix.Ed25519PublicKey -> Edpk
                Tezos.Prefix.Secp256K1PublicKey -> Sppk
                Tezos.Prefix.P256PublicKey -> P2pk
                else -> null
            }
    }
}