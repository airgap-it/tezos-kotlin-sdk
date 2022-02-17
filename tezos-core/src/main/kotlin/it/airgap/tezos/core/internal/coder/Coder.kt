package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface Encoder<in T, out S> {
    public fun encode(value: T): S
}

@InternalTezosSdkApi
public interface Decoder<out T, in S> {
    public fun decode(value: S): T
}

@InternalTezosSdkApi
public interface ConsumingDecoder<out T, S> {
    public fun decodeConsuming(value: MutableList<S>): T
}

@InternalTezosSdkApi
public interface Coder<T, S> : Encoder<T, S>, Decoder<T, S>

@InternalTezosSdkApi
public interface BytesCoder<T> : Coder<T, ByteArray>

@InternalTezosSdkApi
public interface ConsumingBytesCoder<T> : BytesCoder<T>, ConsumingDecoder<T, Byte>
