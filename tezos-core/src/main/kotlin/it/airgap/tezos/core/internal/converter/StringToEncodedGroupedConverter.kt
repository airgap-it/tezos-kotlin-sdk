package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.type.encoded.MetaEncoded

@InternalTezosSdkApi
public abstract class StringToEncodedGroupedConverter<out E : MetaEncoded<E>> : Converter<String, E> {
    protected abstract val kinds: List<MetaEncoded.Kind<E>>
    protected abstract fun failWithInvalidValue(value: String): Nothing

    override fun convert(value: String): E {
        val kind = kinds.find { it.isValid(value) } ?: failWithInvalidValue(value)
        return kind.createValue(value)
    }
}