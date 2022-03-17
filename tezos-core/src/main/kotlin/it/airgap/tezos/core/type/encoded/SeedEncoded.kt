package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Seed --

public sealed interface SeedEncoded : Encoded {
    override val meta: MetaSeedEncoded<*>

    public companion object {}
}

// -- EncryptedSeed --

public sealed interface EncryptedSeedEncoded : Encoded {
    override val meta: MetaEncryptedSeedEncoded<*>

    public companion object {}
}

// -- MetaSeed --

@InternalTezosSdkApi
public sealed interface MetaSeedEncoded<out Self : MetaSeedEncoded<Self>> : MetaEncoded<Self> {
    override val encoded: SeedEncoded

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaSeedEncoded<E>> : MetaEncoded.Kind<E>
}

// -- MetaEncryptedSeed --

@InternalTezosSdkApi
public sealed interface MetaEncryptedSeedEncoded<out Self : MetaEncryptedSeedEncoded<Self>> : MetaEncoded<Self> {
    override val encoded: EncryptedSeedEncoded

    @InternalTezosSdkApi
    public sealed interface Kind<out E : MetaEncryptedSeedEncoded<E>> : MetaEncoded.Kind<E>
}