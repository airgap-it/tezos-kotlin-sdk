package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coder.Base58BytesCoder
import it.airgap.tezos.core.internal.coder.StringPrefixedBytesCoder
import it.airgap.tezos.core.internal.type.ByteTag
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument

internal class SignatureBytesCoder(base58BytesCoder: Base58BytesCoder) : StringPrefixedBytesCoder(base58BytesCoder) {
    override fun decode(value: ByteArray): String {
        TODO("Not yet implemented")
    }

    override fun decode(value: ByteArray, prefix: Tezos.Prefix?): String {
        TODO("Not yet implemented")
    }

    override fun tag(prefix: Tezos.Prefix?): ByteTag? = SignatureTag.fromPrefix(prefix)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidSignature(value)
    private fun failWithInvalidSignature(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos signature.")
}

private object SignatureTag : ByteTag {
    override val value: Byte? = null

    fun fromPrefix(prefix: Tezos.Prefix?): SignatureTag? =
        when (prefix) {
            Tezos.Prefix.Ed25519Signature, Tezos.Prefix.Secp256K1Signature, Tezos.Prefix.P256Signature, Tezos.Prefix.GenericSignature -> SignatureTag
            else -> null
        }
}