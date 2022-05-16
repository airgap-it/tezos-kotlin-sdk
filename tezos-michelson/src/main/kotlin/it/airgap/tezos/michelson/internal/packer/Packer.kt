package it.airgap.tezos.michelson.internal.packer

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.michelson.micheline.MichelineNode

@InternalTezosSdkApi
public interface Packer<T> {
    public fun pack(value: T, schema: MichelineNode?): ByteArray
    public fun unpack(bytes: ByteArray, schema: MichelineNode?): T
}