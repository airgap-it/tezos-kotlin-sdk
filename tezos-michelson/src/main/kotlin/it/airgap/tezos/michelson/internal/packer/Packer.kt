package it.airgap.tezos.michelson.internal.packer

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.michelson.micheline.Micheline

@InternalTezosSdkApi
public interface Packer<T> {
    public fun pack(value: T, schema: Micheline?): ByteArray
    public fun prePack(value: T, schema: Micheline?): ByteArray

    public fun unpack(bytes: ByteArray, schema: Micheline?): T
    public fun postUnpack(bytes: ByteArray, schema: Micheline?): T
}