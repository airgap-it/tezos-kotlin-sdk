package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.type.EncodedTag
import it.airgap.tezos.core.internal.utils.consumeUntil
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.startsWith
import it.airgap.tezos.core.type.encoded.*
import kotlin.math.min

@InternalTezosSdkApi
public class PublicKeyBytesCoder(encodedBytesCoder: EncodedBytesCoder) : EncodedGroupBytesCoder<PublicKeyEncoded<*>>(encodedBytesCoder) {
    override fun tag(encoded: PublicKeyEncoded<*>): EncodedTag<Encoded.Kind<PublicKeyEncoded<*>>>? = PublicKeyTag.fromEncoded(encoded)
    override fun tag(bytes: ByteArray): EncodedTag<Encoded.Kind<PublicKeyEncoded<*>>>? = PublicKeyTag.recognize(bytes)
    override fun tagConsuming(bytes: MutableList<Byte>): EncodedTag<Encoded.Kind<PublicKeyEncoded<*>>>? = PublicKeyTag.recognize(bytes)?.also { bytes.consumeUntil(it.value.size) }

    override fun failWithInvalidValue(value: PublicKeyEncoded<*>): Nothing = failWithUnknownPublicKey(value)
    private fun failWithUnknownPublicKey(value: PublicKeyEncoded<*>): Nothing = failWithIllegalArgument("Unknown Tezos public key `$value`.")

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidPublicKeyBytes(value.joinToString(prefix = "[", postfix = "]"))
    override fun failWithInvalidValue(value: MutableList<Byte>): Nothing = failWithInvalidPublicKeyBytes(value.joinToString(prefix = "[", postfix = "]"))
    private fun failWithInvalidPublicKeyBytes(value: String): Nothing = failWithIllegalArgument("Bytes `$value` are not valid Tezos key bytes.")
}

private enum class PublicKeyTag(override val value: ByteArray, override val kind: PublicKeyEncoded.Kind<*>) : EncodedTag<PublicKeyEncoded.Kind<*>> {
    Edpk(byteArrayOf(0), Ed25519PublicKey),
    Sppk(byteArrayOf(1), Secp256K1PublicKey),
    P2pk(byteArrayOf(2), P256PublicKey);

    private fun isValid(bytes: ByteArray): Boolean = bytes.startsWith(value) && kind.isValid(bytes.slice(value.size until bytes.size))
    private fun isValid(bytes: MutableList<Byte>): Boolean = bytes.startsWith(value) && kind.isValid(bytes.slice(value.size until min(value.size + kind.bytesLength, bytes.size)))

    companion object {
        fun fromEncoded(encoded: PublicKeyEncoded<*>): PublicKeyTag? =
            values().find { it.kind == encoded.kind }

        fun recognize(bytes: ByteArray): PublicKeyTag? =
            if (bytes.isEmpty()) null
            else values().find { it.isValid(bytes) }

        fun recognize(bytes: MutableList<Byte>): PublicKeyTag? =
            if (bytes.isEmpty()) null
            else values().find { it.isValid(bytes) }
    }
}