package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.type.encoded.Encoded
import it.airgap.tezos.core.type.encoded.MetaEncoded

internal abstract class StringToEncodedGroupedConverter<out E : Encoded, out M : MetaEncoded<M, E>> : Converter<String, E> {
    protected abstract val kinds: List<MetaEncoded.Kind<M, E>>
    protected abstract fun failWithInvalidValue(value: String): Nothing

    override fun convert(value: String): E {
        val kind = kinds.find { it.isValid(value) } ?: failWithInvalidValue(value)
        return kind.createValue(value).encoded
    }
}