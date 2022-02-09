package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.utils.startsWith
import it.airgap.tezos.core.type.encoded.Encoded

@InternalTezosSdkApi
public abstract class BytesToEncodedGroupedConverter<out E : Encoded<E>>(private val base58Check: Base58Check) : Converter<ByteArray, E> {
    protected abstract val kinds: List<Encoded.Kind<E>>
    protected abstract fun failWithInvalidValue(value: ByteArray): Nothing

    override fun convert(value: ByteArray): E {
        val kind = kinds.find { it.isValid(value) } ?: failWithInvalidValue(value)
        val base58 = base58Check.encode(value.prefixed(kind))

        return kind.createValue(base58)
    }

    private fun ByteArray.prefixed(kind: Encoded.Kind<E>): ByteArray =
        if (isPrefixed(kind)) this else kind.base58Bytes + this

    private fun ByteArray.isPrefixed(kind: Encoded.Kind<E>): Boolean =
        startsWith(kind.base58Bytes) && size == kind.base58Bytes.size + kind.bytesLength
}