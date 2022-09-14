package it.airgap.tezos.michelson.internal.packer

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface Packer<in T, out S> {
    public fun pack(value: T, schema: T?): S
}

@InternalTezosSdkApi
public interface PrePacker<in T, out S> {
    public fun prePack(value: T, schema: T?): S
}

@InternalTezosSdkApi
public interface Unpacker<T, in S> {
    public fun unpack(bytes: S, schema: T?): T
}

@InternalTezosSdkApi
public interface PostUnpacker<T, in S> {
    public fun postUnpack(bytes: S, schema: T?): T
}

@InternalTezosSdkApi
public interface CombinedPacker<T, S> : Packer<T, S>, PrePacker<T, S>, Unpacker<T, S>, PostUnpacker<T, S>

@InternalTezosSdkApi
public interface BytesPacker<T> : CombinedPacker<T, ByteArray>