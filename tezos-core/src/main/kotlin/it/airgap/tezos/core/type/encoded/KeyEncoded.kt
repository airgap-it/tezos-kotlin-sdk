package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Key --

public sealed interface KeyEncoded : Encoded {
    override val meta: MetaKeyEncoded<*>

    public companion object {}
}

public sealed interface SecretKeyEncoded : KeyEncoded {
    override val meta: MetaSecretKeyEncoded<*>

    public companion object {}
}

public sealed interface PublicKeyEncoded : KeyEncoded {
    override val meta: MetaPublicKeyEncoded<*>

    public companion object {}
}

// -- EncryptedKey --

public sealed interface EncryptedKeyEncoded : Encoded {
    override val meta: MetaEncryptedKeyEncoded<*>

    public companion object {}
}

public sealed interface EncryptedSecretKeyEncoded : EncryptedKeyEncoded {
    override val meta: MetaEncryptedSecretKeyEncoded<*>

    public companion object {}
}

// -- MetaKey --

@InternalTezosSdkApi
public sealed interface MetaKeyEncoded<out Self : MetaKeyEncoded<Self>> : MetaEncoded<Self> {
    override val encoded: KeyEncoded

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaKeyEncoded<E>> : MetaEncoded.Kind<E>
}

@InternalTezosSdkApi
public sealed interface MetaSecretKeyEncoded<out Self : MetaSecretKeyEncoded<Self>> : MetaKeyEncoded<Self> {
    override val encoded: SecretKeyEncoded

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaSecretKeyEncoded<E>> : MetaKeyEncoded.Kind<E>
}

@InternalTezosSdkApi
public sealed interface MetaPublicKeyEncoded<out Self : MetaPublicKeyEncoded<Self>> : MetaKeyEncoded<Self> {
    override val encoded: PublicKeyEncoded

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaPublicKeyEncoded<E>> : MetaKeyEncoded.Kind<E>
}

// -- MetaEncryptedKey --

@InternalTezosSdkApi
public sealed interface MetaEncryptedKeyEncoded<out Self : MetaEncryptedKeyEncoded<Self>> : MetaEncoded<Self> {
    override val encoded: EncryptedKeyEncoded

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaEncryptedKeyEncoded<E>> : MetaEncoded.Kind<E>
}

@InternalTezosSdkApi
public sealed interface MetaEncryptedSecretKeyEncoded<out Self : MetaEncryptedKeyEncoded<Self>> : MetaEncryptedKeyEncoded<Self> {
    override val encoded: EncryptedSecretKeyEncoded

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaEncryptedSecretKeyEncoded<E>> : MetaEncryptedKeyEncoded.Kind<E>
}