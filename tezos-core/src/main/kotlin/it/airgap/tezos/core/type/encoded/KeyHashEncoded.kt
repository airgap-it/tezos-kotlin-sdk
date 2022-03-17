package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// --  KeyHash --

public sealed interface KeyHashEncoded : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaKeyHashEncoded<*>

    public companion object {}
}

public sealed interface PublicKeyHashEncoded : KeyHashEncoded, ImplicitAddress {
    @InternalTezosSdkApi
    override val meta: MetaPublicKeyHashEncoded<*>

    public companion object {}
}

// -- BlindedKeyHash --

public sealed interface BlindedKeyHashEncoded : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaBlindedKeyHashEncoded<*>

    public companion object {}
}

public sealed interface BlindedPublicKeyHashEncoded : BlindedKeyHashEncoded {
    @InternalTezosSdkApi
    override val meta: MetaBlindedPublicKeyHashEncoded<*>

    public companion object {}
}

// -- MetaKeyHash --

@InternalTezosSdkApi
public sealed interface MetaKeyHashEncoded<out Self : MetaKeyHashEncoded<Self>> : MetaEncoded<Self> {
    override val encoded: KeyHashEncoded

    public sealed interface Kind<out E : MetaKeyHashEncoded<E>> : MetaEncoded.Kind<E>
}

@InternalTezosSdkApi
public sealed interface MetaPublicKeyHashEncoded<out Self : MetaPublicKeyHashEncoded<Self>> : MetaKeyHashEncoded<Self>, MetaImplicitAddress<Self> {
    override val encoded: PublicKeyHashEncoded

    public sealed interface Kind<out E : MetaPublicKeyHashEncoded<E>> : MetaKeyHashEncoded.Kind<E>, MetaImplicitAddress.Kind<E>
}

// -- MetaBlindedKeyHash --

@InternalTezosSdkApi
public sealed interface MetaBlindedKeyHashEncoded<out Self: MetaBlindedKeyHashEncoded<Self>> : MetaEncoded<Self> {
    override val encoded: BlindedKeyHashEncoded

    public sealed interface Kind<out E : MetaBlindedKeyHashEncoded<E>> : MetaEncoded.Kind<E>
}

@InternalTezosSdkApi
public sealed interface MetaBlindedPublicKeyHashEncoded<out Self: MetaBlindedPublicKeyHashEncoded<Self>> : MetaBlindedKeyHashEncoded<Self> {
    override val encoded: BlindedPublicKeyHashEncoded

    public sealed interface Kind<out E: MetaBlindedPublicKeyHashEncoded<E>> : MetaBlindedKeyHashEncoded.Kind<E>
}