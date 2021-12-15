package it.airgap.tezos.michelson.internal.converter

// TODO: write annotation processor for converters?
internal interface Converter<T, S> {
    fun convert(value: T): S
}