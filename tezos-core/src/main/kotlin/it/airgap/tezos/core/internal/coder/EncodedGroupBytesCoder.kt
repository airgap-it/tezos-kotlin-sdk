package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.type.EncodedTag
import it.airgap.tezos.core.type.encoded.Encoded
import it.airgap.tezos.core.type.encoded.MetaEncoded

internal abstract class EncodedGroupBytesCoder<E : Encoded, M : MetaEncoded<M, E>>(private val encodedBytesCoder: EncodedBytesCoder) : ConsumingBytesCoder<E> {
    protected abstract fun tag(encoded: E): EncodedTag<MetaEncoded.Kind<M, E>>?
    protected abstract fun tag(bytes: ByteArray): EncodedTag<MetaEncoded.Kind<M, E>>?
    protected abstract fun tagConsuming(bytes: MutableList<Byte>): EncodedTag<MetaEncoded.Kind<M, E>>?

    protected abstract fun failWithInvalidValue(value: E): Nothing
    protected abstract fun failWithInvalidValue(value: ByteArray): Nothing
    protected abstract fun failWithInvalidValue(value: MutableList<Byte>): Nothing

    override fun encode(value: E): ByteArray {
        val tag = tag(value) ?: failWithInvalidValue(value)
        return tag + encodedBytesCoder.encode(value)
    }

    override fun decode(value: ByteArray): E {
        val tag = tag(value) ?: failWithInvalidValue(value)
        return encodedBytesCoder.decode(value.sliceArray(tag.value.size until value.size), tag.kind)
    }
    override fun decodeConsuming(value: MutableList<Byte>): E {
        val tag = tagConsuming(value) ?: failWithInvalidValue(value)
        return encodedBytesCoder.decodeConsuming(value, tag.kind)
    }
}