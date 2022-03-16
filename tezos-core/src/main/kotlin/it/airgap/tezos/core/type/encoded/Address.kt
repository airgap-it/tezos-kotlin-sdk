package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Address --

public sealed interface Address : Encoded {
    override fun toMetaEncoded(): MetaAddress<*>

    public companion object {}
}

public sealed interface ImplicitAddress : Address {
    override fun toMetaEncoded(): MetaImplicitAddress<*>

    public companion object {}
}

public sealed interface OriginatedAddress : Address {
    override fun toMetaEncoded(): MetaOriginatedAddress<*>

    public companion object {}
}

// -- MetaAddress --

@InternalTezosSdkApi
public sealed interface MetaAddress<out Self : MetaAddress<Self>> : MetaEncoded<Self> {
    override fun toEncoded(): Address

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaAddress<E>> : MetaEncoded.Kind<E>
}

@InternalTezosSdkApi
public sealed interface MetaImplicitAddress<out Self : MetaImplicitAddress<Self>> : MetaAddress<Self> {
    override fun toEncoded(): ImplicitAddress

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaImplicitAddress<E>> : MetaAddress.Kind<E>
}

@InternalTezosSdkApi
public sealed interface MetaOriginatedAddress<out Self : MetaOriginatedAddress<Self>> : MetaAddress<Self> {
    override fun toEncoded(): OriginatedAddress

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaOriginatedAddress<E>> : MetaAddress.Kind<E>
}