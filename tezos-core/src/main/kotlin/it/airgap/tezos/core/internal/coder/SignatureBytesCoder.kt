package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.type.PrefixTag
import it.airgap.tezos.core.internal.utils.consumeAt
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.startsWith

public class SignatureBytesCoder(base58BytesCoder: Base58BytesCoder) : Base58PrefixedBytesCoder(base58BytesCoder) {
    override fun tag(prefix: Tezos.Prefix): PrefixTag? = SignatureTag.fromPrefix(prefix)
    override fun tag(bytes: ByteArray): PrefixTag? = SignatureTag.recognize(bytes)
    override fun tag(bytes: MutableList<Byte>): PrefixTag? = SignatureTag.recognize(bytes)
    override fun tagConsuming(bytes: MutableList<Byte>): PrefixTag? = tag(bytes)?.also { bytes.consumeAt(0 until it.value.size) }

    override fun failWithInvalidValue(value: String): Nothing = failWithInvalidSignature(value)
    private fun failWithInvalidSignature(value: String): Nothing = failWithIllegalArgument("Value `$value` is not a valid Tezos signature.")

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidSignatureBytes(value.joinToString(prefix = "[", postfix = "]"))
    override fun failWithInvalidValue(value: MutableList<Byte>): Nothing = failWithInvalidSignatureBytes(value.joinToString(prefix = "[", postfix = "]"))

    private fun failWithInvalidSignatureBytes(value: String): Nothing = failWithIllegalArgument("Bytes `$value` are not valid Tezos signature bytes.")
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
        else takeIf(bytes.size, bytes::startsWith)

    fun recognize(bytes: MutableList<Byte>): SignatureTag? =
        if (bytes.isEmpty()) null
        else takeIf(bytes.size, bytes::startsWith)

    private fun takeIf(size: Int, startsWith: (ByteArray) -> Boolean): SignatureTag? =
        SignatureTag.takeIf { startsWith(value) && size == prefix.dataLength + value.size }
}