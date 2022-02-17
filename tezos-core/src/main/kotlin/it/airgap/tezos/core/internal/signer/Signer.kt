package it.airgap.tezos.core.internal.signer

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface Signer<in T, in SK, in PK, S> {
    public fun sign(message: T, key: SK): S
    public fun verify(message: T, signature: S, key: PK): Boolean
}