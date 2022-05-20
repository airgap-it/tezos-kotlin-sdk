package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Signature --

public sealed interface Signature : Encoded {
    override val meta: MetaSignature<*, *>

    public companion object {}
}

// -- MetaSignature --

@InternalTezosSdkApi
public sealed interface MetaSignature<out Self : MetaSignature<Self, S>, out S : Signature> : MetaEncoded<Self, S> {
    override val encoded: S

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaSignature<M, S>, out S : Signature> : MetaEncoded.Kind<M, S>
}
