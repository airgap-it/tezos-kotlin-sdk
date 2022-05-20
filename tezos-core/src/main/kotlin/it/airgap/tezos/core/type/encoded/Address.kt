package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Address --

public sealed interface Address : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaAddress<*, *>

    public companion object {}
}

public sealed interface ImplicitAddress : Address {
    @InternalTezosSdkApi
    override val meta: MetaImplicitAddress<*, *>

    public companion object {}
}

public sealed interface OriginatedAddress : Address {
    @InternalTezosSdkApi
    override val meta: MetaOriginatedAddress<*, *>

    public companion object {}
}

// -- MetaAddress --

@InternalTezosSdkApi
public sealed interface MetaAddress<out Self : MetaAddress<Self, A>, out A : Address> : MetaEncoded<Self, A> {
    override val encoded: A

    public sealed interface Kind<out M : MetaAddress<M, A>, out A : Address> : MetaEncoded.Kind<M, A>
}

@InternalTezosSdkApi
public sealed interface MetaImplicitAddress<out Self : MetaImplicitAddress<Self, IA>, out IA : ImplicitAddress> : MetaAddress<Self, IA> {
    override val encoded: IA

    public sealed interface Kind<out M : MetaImplicitAddress<M, IA>, out IA : ImplicitAddress> : MetaAddress.Kind<M, IA>
}

@InternalTezosSdkApi
public sealed interface MetaOriginatedAddress<out Self : MetaOriginatedAddress<Self, OA>, out OA : OriginatedAddress> : MetaAddress<Self, OA> {
    override val encoded: OA

    public sealed interface Kind<out M : MetaOriginatedAddress<M, OA>, out OA : OriginatedAddress> : MetaAddress.Kind<M, OA>
}