package it.airgap.tezos.core.internal.coder.encoded

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.context.TezosCoreContext.consumeAt
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithIllegalArgument
import it.airgap.tezos.core.internal.context.TezosCoreContext.padEnd
import it.airgap.tezos.core.internal.context.TezosCoreContext.takeFrom
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.MetaEncoded
import it.airgap.tezos.core.type.encoded.MetaOriginatedAddress
import it.airgap.tezos.core.type.encoded.OriginatedAddress

internal class OriginatedAddressBytesCoder(private val encodedBytesCoder: EncodedBytesCoder) : ConsumingBytesCoder<OriginatedAddress> {
    override fun encode(value: OriginatedAddress): ByteArray =
        encodedBytesCoder.encode(value).padEnd(TARGET_SIZE, fillValue = PADDING_VALUE.toUByte())

    override fun decode(value: ByteArray): OriginatedAddress {
        val kind = ContractHash
        return encodedBytesCoder.decode(value.dropPadding(kind), kind)
    }

    override fun decodeConsuming(value: MutableList<Byte>): OriginatedAddress {
        val kind = ContractHash

        if (!value.isPadded(kind)) failWithInvalidValue(value)
        return encodedBytesCoder.decodeConsuming(value, kind).also { value.consumePadding(kind) }
    }

    private val MetaEncoded.Kind<MetaOriginatedAddress<*, OriginatedAddress>, OriginatedAddress>.paddingLength: Int
        get() = TARGET_SIZE - bytesLength

    private fun ByteArray.dropPadding(kind: MetaEncoded.Kind<MetaOriginatedAddress<*, OriginatedAddress>, OriginatedAddress>): ByteArray =
        if (isPadded(kind)) this.sliceArray(0 until kind.bytesLength)
        else this

    private fun ByteArray.isPadded(kind: MetaEncoded.Kind<MetaOriginatedAddress<*, OriginatedAddress>, OriginatedAddress>): Boolean =
        toList().isPadded(kind)

    private fun List<Byte>.isPadded(kind: MetaEncoded.Kind<MetaOriginatedAddress<*, OriginatedAddress>, OriginatedAddress>): Boolean {
        if (size < TARGET_SIZE) return false
        return takeFrom(kind.bytesLength, kind.paddingLength).all { it == PADDING_VALUE.toByte() }
    }

    private fun MutableList<Byte>.consumePadding(kind: MetaEncoded.Kind<MetaOriginatedAddress<*, OriginatedAddress>, OriginatedAddress>) {
        consumeAt(0 until kind.paddingLength)
    }

    private fun failWithInvalidValue(value: MutableList<Byte>): Nothing = failWithInvalidOriginatedAddressBytes(value.joinToString(prefix = "[", postfix = "]"))
    private fun failWithInvalidOriginatedAddressBytes(value: String): Nothing = failWithIllegalArgument("Bytes `$value` are not valid Tezos originated address bytes.")

    companion object {
        private const val TARGET_SIZE = 21
        private const val PADDING_VALUE = 0U
    }
}