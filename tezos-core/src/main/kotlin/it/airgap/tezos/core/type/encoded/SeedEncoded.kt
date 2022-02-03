package it.airgap.tezos.core.type.encoded

// -- Seed --

public sealed interface SeedEncoded<out Self : SeedEncoded<Self>> : Encoded<Self> {
    public sealed interface Kind<out E : SeedEncoded<E>> : Encoded.Kind<E>

    public companion object {}
}

// -- EncryptedSeed --

public sealed interface EncryptedSeedEncoded<out Self : EncryptedSeedEncoded<Self>> : Encoded<Self> {
    public sealed interface Kind<out E : EncryptedSeedEncoded<E>> : Encoded.Kind<E>

    public companion object {}
}