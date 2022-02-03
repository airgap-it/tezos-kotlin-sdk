package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.type.encoded.Encoded

@InternalTezosSdkApi
public abstract class BytesToEncodedGroupedConverter<out E : Encoded<out E>>(private val base58Check: Base58Check) : Converter<ByteArray, E> {
    protected abstract val kinds: List<Encoded.Kind<out E>>
    protected abstract fun failWithInvalidValue(value: ByteArray): Nothing

    override fun convert(value: ByteArray): E {
        val kind = kinds.find { it.isValid(value) } ?: failWithInvalidValue(value)
        val base58 = base58Check.encode(kind.base58Bytes + value)

        return kind.createValue(base58)
    }
}