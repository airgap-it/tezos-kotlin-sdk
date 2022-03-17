package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Address --

public sealed interface Address : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaAddress<*>

    public companion object {}
}

public sealed interface ImplicitAddress : Address {
    @InternalTezosSdkApi
    override val meta: MetaImplicitAddress<*>

    public companion object {}
}

public sealed interface OriginatedAddress : Address {
    @InternalTezosSdkApi
    override val meta: MetaOriginatedAddress<*>

    public companion object {}
}

// -- MetaAddress --

@InternalTezosSdkApi
public sealed interface MetaAddress<out Self : MetaAddress<Self>> : MetaEncoded<Self> {
    override val encoded: Address

    public sealed interface Kind<out E : MetaAddress<E>> : MetaEncoded.Kind<E>
}

@InternalTezosSdkApi
public sealed interface MetaImplicitAddress<out Self : MetaImplicitAddress<Self>> : MetaAddress<Self> {
    override val encoded: ImplicitAddress

    public sealed interface Kind<out E : MetaImplicitAddress<E>> : MetaAddress.Kind<E>
}

@InternalTezosSdkApi
public sealed interface MetaOriginatedAddress<out Self : MetaOriginatedAddress<Self>> : MetaAddress<Self> {
    override val encoded: OriginatedAddress

    public sealed interface Kind<out E : MetaOriginatedAddress<E>> : MetaAddress.Kind<E>
}