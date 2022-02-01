package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.type.PrefixTag
import it.airgap.tezos.core.internal.utils.consumeAt

public interface Coder<T, S> {
    public fun encode(value: T): S
    public fun decode(value: S): T
}

public interface BytesCoder<T> : Coder<T, ByteArray>
public interface ConsumingBytesCoder<T> : BytesCoder<T> {
    public fun decodeConsuming(value: MutableList<Byte>): T
}

public interface PrefixedCoder<T> {
    public fun encode(value: T, prefix: Tezos.Prefix): ByteArray
    public fun decode(value: ByteArray, prefix: Tezos.Prefix): T
}
public interface ConsumingPrefixedCoder<T> : PrefixedCoder<T> {
    public fun decodeConsuming(value: MutableList<Byte>, prefix: Tezos.Prefix): T
}

public abstract class Base58PrefixedBytesCoder(
    private val base58BytesCoder: Base58BytesCoder,
) : ConsumingBytesCoder<String>, ConsumingPrefixedCoder<String> {
    public abstract fun tag(prefix: Tezos.Prefix): PrefixTag?
    public abstract fun tag(bytes: ByteArray): PrefixTag?
    public abstract fun tag(bytes: MutableList<Byte>): PrefixTag?
    public abstract fun tagConsuming(bytes: MutableList<Byte>): PrefixTag?

    public abstract fun failWithInvalidValue(value: String): Nothing
    public abstract fun failWithInvalidValue(value: ByteArray): Nothing
    public abstract fun failWithInvalidValue(value: MutableList<Byte>): Nothing

    override fun encode(value: String): ByteArray {
        val prefix = Tezos.Prefix.recognize(value) ?: failWithInvalidValue(value)
        return encode(value, prefix)
    }

    override fun encode(value: String, prefix: Tezos.Prefix): ByteArray {
        if (!value.startsWith(prefix.value)) failWithInvalidValue(value)

        val tag = tag(prefix) ?: failWithInvalidValue(value)
        return tag + base58BytesCoder.encode(value, prefix)
    }

    override fun decode(value: ByteArray): String {
        val tag = tag(value) ?: failWithInvalidValue(value)
        return decode(value, tag.prefix)
    }

    override fun decode(value: ByteArray, prefix: Tezos.Prefix): String = decodeConsuming(value.toMutableList(), prefix)

    override fun decodeConsuming(value: MutableList<Byte>): String {
        val tag = tag(value) ?: failWithInvalidValue(value)
        return decodeConsuming(value, tag.prefix)
    }

    override fun decodeConsuming(value: MutableList<Byte>, prefix: Tezos.Prefix): String {
        val tag = tagConsuming(value)
        if (tag?.prefix != prefix) failWithInvalidValue(value)

        return base58BytesCoder.decode(value.consumeAt(0 until tag.prefix.dataLength).toByteArray(), prefix)
    }
}