package it.airgap.tezos.core.internal.coder.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.consumeUntil
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.internal.context.TezosCoreContext.startsWith
import it.airgap.tezos.core.internal.type.EncodedTag
import it.airgap.tezos.core.type.encoded.*
import kotlin.math.min

internal class PublicKeyBytesCoder(encodedBytesCoder: EncodedBytesCoder) : EncodedGroupBytesCoder<PublicKey, MetaPublicKey<*, PublicKey>>(encodedBytesCoder) {
    override fun tag(encoded: PublicKey): EncodedTag<MetaEncoded.Kind<MetaPublicKey<*, PublicKey>, PublicKey>>? = PublicKeyTag.fromEncoded(encoded)
    override fun tag(bytes: ByteArray): EncodedTag<MetaEncoded.Kind<MetaPublicKey<*, PublicKey>, PublicKey>>? = PublicKeyTag.recognize(bytes)
    override fun tagConsuming(bytes: MutableList<Byte>): EncodedTag<MetaEncoded.Kind<MetaPublicKey<*, PublicKey>, PublicKey>>? = PublicKeyTag.recognize(bytes)?.also { bytes.consumeUntil(it.value.size) }

    override fun failWithInvalidValue(value: PublicKey): Nothing = failWithUnknownPublicKey(value)
    private fun failWithUnknownPublicKey(value: PublicKey): Nothing = failWithIllegalArgument("Unknown Tezos public key `$value`.")

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidPublicKeyBytes(value.joinToString(prefix = "[", postfix = "]"))
    override fun failWithInvalidValue(value: MutableList<Byte>): Nothing = failWithInvalidPublicKeyBytes(value.joinToString(prefix = "[", postfix = "]"))
    private fun failWithInvalidPublicKeyBytes(value: String): Nothing = failWithIllegalArgument("Bytes `$value` are not valid Tezos key bytes.")
}

private enum class PublicKeyTag(override val value: ByteArray, override val kind: MetaPublicKey.Kind<*, PublicKey>) : EncodedTag<MetaPublicKey.Kind<*, PublicKey>> {
    Edpk(byteArrayOf(0), Ed25519PublicKey),
    Sppk(byteArrayOf(1), Secp256K1PublicKey),
    P2pk(byteArrayOf(2), P256PublicKey);

    private fun isValid(bytes: ByteArray): Boolean = bytes.startsWith(value) && kind.isValid(bytes.slice(value.size until bytes.size))
    private fun isValid(bytes: MutableList<Byte>): Boolean = bytes.startsWith(value) && kind.isValid(bytes.slice(value.size until min(value.size + kind.bytesLength, bytes.size)))

    companion object {
        fun fromEncoded(encoded: PublicKey): PublicKeyTag? =
            values().find { it.kind == encoded.meta.kind }

        fun recognize(bytes: ByteArray): PublicKeyTag? =
            if (bytes.isEmpty()) null
            else values().find { it.isValid(bytes) }

        fun recognize(bytes: MutableList<Byte>): PublicKeyTag? =
            if (bytes.isEmpty()) null
            else values().find { it.isValid(bytes) }
    }
}