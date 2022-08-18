package it.airgap.tezos.core.internal.coder.encoded

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.context.TezosCoreContext.consumeUntil
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.internal.context.TezosCoreContext.startsWith
import it.airgap.tezos.core.internal.type.BytesTag
import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.OriginatedAddress

internal class AddressBytesCoder(
    private val implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>,
    private val originatedAddressBytesCoder: ConsumingBytesCoder<OriginatedAddress>,
) : ConsumingBytesCoder<Address> {
    override fun encode(value: Address): ByteArray =
        when (value) {
            is ImplicitAddress -> AddressTag.Implicit + implicitAddressBytesCoder.encode(value)
            is OriginatedAddress -> AddressTag.Originated + originatedAddressBytesCoder.encode(value)
        }

    override fun decode(value: ByteArray): Address {
        val tag = AddressTag.recognize(value) ?: failWithInvalidAddressBytes(value)

        val decoder: (ByteArray) -> Address = when (tag) {
            AddressTag.Implicit -> implicitAddressBytesCoder::decode
            AddressTag.Originated -> originatedAddressBytesCoder::decode
        }

        return decoder(value.sliceArray(tag.value.size until value.size))
    }

    override fun decodeConsuming(value: MutableList<Byte>): Address {
        val tag = AddressTag.recognize(value) ?: failWithInvalidAddressBytes(value)
        value.consumeUntil(tag.value.size)

        val decoder: (MutableList<Byte>) -> Address = when (tag) {
            AddressTag.Implicit -> implicitAddressBytesCoder::decodeConsuming
            AddressTag.Originated -> originatedAddressBytesCoder::decodeConsuming
        }

        return decoder(value)
    }

    private fun failWithInvalidAddressBytes(value: ByteArray): Nothing = failWithInvalidAddressBytes(value.joinToString(prefix = "[", postfix = "]"))
    private fun failWithInvalidAddressBytes(value: MutableList<Byte>): Nothing = failWithInvalidAddressBytes(value.joinToString(prefix = "[", postfix = "]"))
    private fun failWithInvalidAddressBytes(value: String): Nothing = failWithIllegalArgument("Bytes `$value` are not valid Tezos address bytes.")
}

private enum class AddressTag(override val value: ByteArray) : BytesTag {
    Implicit(byteArrayOf(0)),
    Originated(byteArrayOf(1));

    companion object {
        fun recognize(bytes: ByteArray): AddressTag? =
            if (bytes.isEmpty()) null
            else find { bytes.startsWith(it) }

        fun recognize(bytes: List<Byte>): AddressTag? =
            if (bytes.isEmpty()) null
            else find { bytes.startsWith(it) }

        private fun find(startsWith: (ByteArray) -> Boolean): AddressTag? =
            values().find { startsWith(it.value) }
    }
}
