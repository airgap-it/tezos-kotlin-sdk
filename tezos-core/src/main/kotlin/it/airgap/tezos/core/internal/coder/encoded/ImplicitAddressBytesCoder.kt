package it.airgap.tezos.core.internal.coder.encoded

import it.airgap.tezos.core.internal.type.EncodedTag
import it.airgap.tezos.core.internal.utils.consumeUntil
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.internal.utils.startsWith
import it.airgap.tezos.core.type.encoded.*
import kotlin.math.min

internal class ImplicitAddressBytesCoder(encodedBytesCoder: EncodedBytesCoder) : EncodedGroupBytesCoder<ImplicitAddress, MetaImplicitAddress<*, ImplicitAddress>>(encodedBytesCoder) {
    override fun tag(encoded: ImplicitAddress): EncodedTag<MetaEncoded.Kind<MetaImplicitAddress<*, ImplicitAddress>, ImplicitAddress>>? = ImplicitAddressTag.fromEncoded(encoded)
    override fun tag(bytes: ByteArray): EncodedTag<MetaEncoded.Kind<MetaImplicitAddress<*, ImplicitAddress>, ImplicitAddress>>? = ImplicitAddressTag.recognize(bytes)
    override fun tagConsuming(bytes: MutableList<Byte>): EncodedTag<MetaEncoded.Kind<MetaImplicitAddress<*, ImplicitAddress>, ImplicitAddress>>? = ImplicitAddressTag.recognize(bytes)?.also { bytes.consumeUntil(it.value.size) }

    override fun failWithInvalidValue(value: ImplicitAddress): Nothing = failWithUnknownImplicitAddress(value)
    private fun failWithUnknownImplicitAddress(value: ImplicitAddress): Nothing = failWithIllegalArgument("Unknown Tezos implicit address `$value`.")

    override fun failWithInvalidValue(value: ByteArray): Nothing = failWithInvalidImplicitAddressBytes(value.joinToString(prefix = "[", postfix = "]"))
    override fun failWithInvalidValue(value: MutableList<Byte>): Nothing = failWithInvalidImplicitAddressBytes(value.joinToString(prefix = "[", postfix = "]"))
    private fun failWithInvalidImplicitAddressBytes(value: String): Nothing = failWithIllegalArgument("Bytes `$value` are not valid Tezos implicit address bytes.")
}

private enum class ImplicitAddressTag(override val value: ByteArray, override val kind: MetaImplicitAddress.Kind<*, ImplicitAddress>) : EncodedTag<MetaImplicitAddress.Kind<*, ImplicitAddress>> {
    Tz1(byteArrayOf(0), Ed25519PublicKeyHash),
    Tz2(byteArrayOf(1), Secp256K1PublicKeyHash),
    Tz3(byteArrayOf(2), P256PublicKeyHash);

    private fun isValid(bytes: ByteArray): Boolean = bytes.startsWith(value) && kind.isValid(bytes.slice(value.size until bytes.size))
    private fun isValid(bytes: MutableList<Byte>): Boolean = bytes.startsWith(value) && kind.isValid(bytes.slice(value.size until min(value.size + kind.bytesLength, bytes.size)))

    companion object {
        fun fromEncoded(encoded: ImplicitAddress): ImplicitAddressTag? =
           values().find { it.kind == encoded.meta.kind }

        fun recognize(bytes: ByteArray): ImplicitAddressTag? =
            if (bytes.isEmpty()) null
            else values().find { it.isValid(bytes) }

        fun recognize(bytes: MutableList<Byte>): ImplicitAddressTag? =
            if (bytes.isEmpty()) null
            else values().find { it.isValid(bytes) }
    }
}