package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.type.EncodedTag
import it.airgap.tezos.core.internal.utils.consumeUntil
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.startsWith
import it.airgap.tezos.core.type.encoded.*
import kotlin.math.min

@InternalTezosSdkApi
public class SignatureBytesCoder(encodedBytesCoder: EncodedBytesCoder) : EncodedGroupBytesCoder<MetaSignatureEncoded<*>>(encodedBytesCoder) {
    override fun tag(encoded: MetaSignatureEncoded<*>): EncodedTag<MetaEncoded.Kind<MetaSignatureEncoded<*>>> = SignatureTag
    override fun tag(bytes: ByteArray): EncodedTag<MetaEncoded.Kind<MetaSignatureEncoded<*>>>? = SignatureTag.recognize(bytes)
    override fun tagConsuming(bytes: MutableList<Byte>): EncodedTag<MetaEncoded.Kind<MetaSignatureEncoded<*>>>? = SignatureTag.recognize(bytes)?.also { bytes.consumeUntil(it.value.size) }

    override fun failWithInvalidValue(value: MetaSignatureEncoded<*>): Nothing = failWithUnknownSignature(value)
    private fun failWithUnknownSignature(value: MetaSignatureEncoded<*>): Nothing = failWithIllegalArgument("Unknown Tezos signature `$value`.")

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidSignatureBytes(value.joinToString(prefix = "[", postfix = "]"))
    override fun failWithInvalidValue(value: MutableList<Byte>): Nothing = failWithInvalidSignatureBytes(value.joinToString(prefix = "[", postfix = "]"))
    private fun failWithInvalidSignatureBytes(value: String): Nothing = failWithIllegalArgument("Bytes `$value` are not valid Tezos signature bytes.")
}

private object SignatureTag : EncodedTag<MetaSignatureEncoded.Kind<*>> {
    override val value: ByteArray = byteArrayOf()
    override val kind: MetaSignatureEncoded.Kind<*> = GenericSignature

    private fun isValid(bytes: ByteArray): Boolean = bytes.startsWith(value) && kind.isValid(bytes.slice(value.size until bytes.size))
    private fun isValid(bytes: MutableList<Byte>): Boolean = bytes.startsWith(value) && kind.isValid(bytes.slice(value.size until min(value.size + kind.bytesLength, bytes.size)))

    fun recognize(bytes: ByteArray): SignatureTag? =
        if (bytes.isEmpty()) null
        else SignatureTag.takeIf { it.isValid(bytes) }

    fun recognize(bytes: MutableList<Byte>): SignatureTag? =
        if (bytes.isEmpty()) null
        else SignatureTag.takeIf { it.isValid(bytes) }
}