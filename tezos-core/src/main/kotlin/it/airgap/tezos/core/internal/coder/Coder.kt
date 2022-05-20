package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Encoder --

@InternalTezosSdkApi
public interface ConfigurableEncoder<in T, out S, in C> {
    public fun encode(value: T, configuration: C): S
}

@InternalTezosSdkApi
public interface Encoder<in T, out S> : ConfigurableEncoder<T, S, Unit> {
    public fun encode(value: T): S

    override fun encode(value: T, configuration: Unit): S = encode(value)
}

// -- Decoder --

@InternalTezosSdkApi
public interface ConfigurableDecoder<out T, in S, in C> {
    public fun decode(value: S, configuration: C): T
}

@InternalTezosSdkApi
public interface Decoder<out T, in S> : ConfigurableDecoder<T, S, Unit> {
    public fun decode(value: S): T

    override fun decode(value: S, configuration: Unit): T = decode(value)
}

@InternalTezosSdkApi
public interface ConfigurableConsumingDecoder<out T, S, in C> {
    public fun decodeConsuming(value: MutableList<S>, configuration : C): T
}

@InternalTezosSdkApi
public interface ConsumingDecoder<out T, S> : ConfigurableConsumingDecoder<T, S, Unit> {
    public fun decodeConsuming(value: MutableList<S>): T

    override fun decodeConsuming(value: MutableList<S>, configuration: Unit): T = decodeConsuming(value)
}

// -- Coder --

@InternalTezosSdkApi
public interface ConfigurableCoder<T, S, in EC, in DC> : ConfigurableEncoder<T, S, EC>, ConfigurableDecoder<T, S, DC>

@InternalTezosSdkApi
public interface Coder<T, S> : Encoder<T, S>, Decoder<T, S>

@InternalTezosSdkApi
public interface ConfigurableBytesCoder<T, in EC, in DC> : ConfigurableCoder<T, ByteArray, EC, DC>

@InternalTezosSdkApi
public interface BytesCoder<T> : Coder<T, ByteArray>

@InternalTezosSdkApi
public interface ConfigurableConsumingBytesCoder<T, in EC, in DC> : ConfigurableBytesCoder<T, EC, DC>, ConfigurableConsumingDecoder<T, Byte, DC>

@InternalTezosSdkApi
public interface ConsumingBytesCoder<T> : BytesCoder<T>, ConsumingDecoder<T, Byte>
