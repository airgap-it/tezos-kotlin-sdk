package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.type.EncodedTag
import it.airgap.tezos.core.internal.utils.consumeUntil
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.startsWith
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.MetaEncoded
import it.airgap.tezos.core.type.encoded.MetaSignature
import it.airgap.tezos.core.type.encoded.Signature
import kotlin.math.min

internal class SignatureBytesCoder(encodedBytesCoder: EncodedBytesCoder) : EncodedGroupBytesCoder<Signature, MetaSignature<*, Signature>>(encodedBytesCoder) {
    override fun tag(encoded: Signature): EncodedTag<MetaEncoded.Kind<MetaSignature<*, Signature>, Signature>> = SignatureTag
    override fun tag(bytes: ByteArray): EncodedTag<MetaEncoded.Kind<MetaSignature<*, Signature>, Signature>>? = SignatureTag.recognize(bytes)
    override fun tagConsuming(bytes: MutableList<Byte>): EncodedTag<MetaEncoded.Kind<MetaSignature<*, Signature>, Signature>>? = SignatureTag.recognize(bytes)?.also { bytes.consumeUntil(it.value.size) }

    override fun failWithInvalidValue(value: Signature): Nothing = failWithUnknownSignature(value)
    private fun failWithUnknownSignature(value: Signature): Nothing = failWithIllegalArgument("Unknown Tezos signature `$value`.")

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidSignatureBytes(value.joinToString(prefix = "[", postfix = "]"))
    override fun failWithInvalidValue(value: MutableList<Byte>): Nothing = failWithInvalidSignatureBytes(value.joinToString(prefix = "[", postfix = "]"))
    private fun failWithInvalidSignatureBytes(value: String): Nothing = failWithIllegalArgument("Bytes `$value` are not valid Tezos signature bytes.")
}

private object SignatureTag : EncodedTag<MetaSignature.Kind<*, Signature>> {
    override val value: ByteArray = byteArrayOf()
    override val kind: MetaSignature.Kind<*, Signature> = GenericSignature

    private fun isValid(bytes: ByteArray): Boolean = bytes.startsWith(value) && kind.isValid(bytes.slice(value.size until bytes.size))
    private fun isValid(bytes: MutableList<Byte>): Boolean = bytes.startsWith(value) && kind.isValid(bytes.slice(value.size until min(value.size + kind.bytesLength, bytes.size)))

    fun recognize(bytes: ByteArray): SignatureTag? =
        if (bytes.isEmpty()) null
        else SignatureTag.takeIf { it.isValid(bytes) }

    fun recognize(bytes: MutableList<Byte>): SignatureTag? =
        if (bytes.isEmpty()) null
        else SignatureTag.takeIf { it.isValid(bytes) }
}