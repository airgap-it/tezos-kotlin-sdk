package it.airgap.tezos.core.internal.converter

// TODO: write annotation processor for converters?
public interface Converter<T, S> {
    public fun convert(value: T): S
}