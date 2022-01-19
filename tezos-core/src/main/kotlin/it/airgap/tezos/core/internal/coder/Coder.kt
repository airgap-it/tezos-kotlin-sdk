package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.type.ByteTag

public interface Coder<T, S> {
    public fun encode(value: T): S
    public fun decode(value: S): T
}

public interface BytesCoder<T> : Coder<T, ByteArray>
public interface PrefixedCoder<T> {
    public fun encode(value: T, prefix: Tezos.Prefix?): ByteArray
    public fun decode(value: ByteArray, prefix: Tezos.Prefix?): T
}

public abstract class StringPrefixedBytesCoder(
    protected val innerCoder: PrefixedCoder<String>,
) : BytesCoder<String>, PrefixedCoder<String> {
    public abstract fun tag(prefix: Tezos.Prefix?): ByteTag?
    public abstract fun failWithInvalidValue(value: String): Nothing

    override fun encode(value: String): ByteArray {
        val prefix = Tezos.Prefix.recognize(value) ?: failWithInvalidValue(value)
        return encode(value, prefix)
    }

    override fun encode(value: String, prefix: Tezos.Prefix?): ByteArray {
        if (prefix != null && !value.startsWith(prefix.value)) failWithInvalidValue(value)

        val prefix = prefix ?: Tezos.Prefix.recognize(value)
        val tag = tag(prefix) ?: failWithInvalidValue(value)
        return tag + innerCoder.encode(value, prefix)
    }
}