package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Key --

public sealed interface Key : Encoded {
    override val meta: MetaKey<*, *>

    public companion object {}
}

public sealed interface SecretKey : Key {
    override val meta: MetaSecretKey<*, *>

    public companion object {}
}

public sealed interface PublicKey : Key {
    override val meta: MetaPublicKey<*, *>

    public companion object {}
}

// -- EncryptedKey --

public sealed interface EncryptedKey : Encoded {
    override val meta: MetaEncryptedKey<*, *>

    public companion object {}
}

public sealed interface EncryptedSecretKey : EncryptedKey {
    override val meta: MetaEncryptedSecretKey<*, *>

    public companion object {}
}

// -- MetaKey --

@InternalTezosSdkApi
public sealed interface MetaKey<out Self : MetaKey<Self, K>, out K : Key> : MetaEncoded<Self, K> {
    override val encoded: K

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaKey<M, K>, out K : Key> : MetaEncoded.Kind<M, K>
}

@InternalTezosSdkApi
public sealed interface MetaSecretKey<out Self : MetaSecretKey<Self, SK>, out SK : SecretKey> : MetaKey<Self, SK> {
    override val encoded: SK

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaSecretKey<M, SK>, out SK : SecretKey> : MetaKey.Kind<M, SK>
}

@InternalTezosSdkApi
public sealed interface MetaPublicKey<out Self : MetaPublicKey<Self, PK>, out PK : PublicKey> : MetaKey<Self, PK> {
    override val encoded: PK

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaPublicKey<M, PK>, out PK : PublicKey> : MetaKey.Kind<M, PK>
}

// -- MetaEncryptedKey --

@InternalTezosSdkApi
public sealed interface MetaEncryptedKey<out Self : MetaEncryptedKey<Self, EK>, out EK : EncryptedKey> : MetaEncoded<Self, EK> {
    override val encoded: EK

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaEncryptedKey<M, EK>, out EK : EncryptedKey> : MetaEncoded.Kind<M, EK>
}

@InternalTezosSdkApi
public sealed interface MetaEncryptedSecretKey<out Self : MetaEncryptedKey<Self, ESK>, out ESK: EncryptedSecretKey> : MetaEncryptedKey<Self, ESK> {
    override val encoded: ESK

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaEncryptedSecretKey<M, ESK>, out ESK: EncryptedSecretKey> : MetaEncryptedKey.Kind<M, ESK>
}