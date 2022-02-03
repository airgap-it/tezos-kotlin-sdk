package it.airgap.tezos.core.type.encoded

// -- Key --

public sealed interface KeyEncoded<out Self : KeyEncoded<Self>> : Encoded<Self> {
    public sealed interface Kind<out E : KeyEncoded<E>> : Encoded.Kind<E>

    public companion object {}
}

public sealed interface SecretKeyEncoded<out Self : SecretKeyEncoded<Self>> : KeyEncoded<Self> {
    public sealed interface Kind<out E : SecretKeyEncoded<E>> : KeyEncoded.Kind<E>

    public companion object {}
}

public sealed interface PublicKeyEncoded<out Self : PublicKeyEncoded<Self>> : KeyEncoded<Self> {
    public sealed interface Kind<out E : PublicKeyEncoded<E>> : KeyEncoded.Kind<E>

    public companion object {}
}

// -- EncryptedKey --

public sealed interface EncryptedKeyEncoded<out Self : EncryptedKeyEncoded<Self>> : Encoded<Self> {
    public sealed interface Kind<out E : EncryptedKeyEncoded<E>> : Encoded.Kind<E>

    public companion object {}
}

public sealed interface EncryptedSecretKeyEncoded<out Self : EncryptedKeyEncoded<Self>> : EncryptedKeyEncoded<Self> {
    public sealed interface Kind<out E : EncryptedSecretKeyEncoded<E>> : EncryptedKeyEncoded.Kind<E>

    public companion object {}
}