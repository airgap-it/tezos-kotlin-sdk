package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coder.Base58BytesCoder
import it.airgap.tezos.core.internal.coder.Base58PrefixedBytesCoder
import it.airgap.tezos.core.internal.type.PrefixTag
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.startsWith

internal class SignatureBytesCoder(base58BytesCoder: Base58BytesCoder) : Base58PrefixedBytesCoder(base58BytesCoder) {
    override fun tag(prefix: Tezos.Prefix): PrefixTag? = SignatureTag.fromPrefix(prefix)
    override fun tag(bytes: ByteArray): PrefixTag? = SignatureTag.recognize(bytes)

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidSignature(value)
    private fun failWithInvalidSignature(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos signature.")

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidSignatureBytes(value)
    private fun failWithInvalidSignatureBytes(value: ByteArray): Nothing = failWithIllegalArgument("Bytes `${value.joinToString(prefix = "[", postfix = "]")}` are not valid Tezos signature bytes.")
}

private object SignatureTag : PrefixTag {
    override val value: ByteArray = byteArrayOf()
    override val prefix: Tezos.Prefix = Tezos.Prefix.GenericSignature

    fun fromPrefix(prefix: Tezos.Prefix): SignatureTag? =
        when (prefix) {
            Tezos.Prefix.Ed25519Signature, Tezos.Prefix.Secp256K1Signature, Tezos.Prefix.P256Signature, Tezos.Prefix.GenericSignature -> SignatureTag
            else -> null
        }

    fun recognize(bytes: ByteArray): SignatureTag? =
        if (bytes.isEmpty()) null
        else SignatureTag.takeIf { bytes.startsWith(it.value) && bytes.size == it.prefix.dataLength + it.value.size }
}