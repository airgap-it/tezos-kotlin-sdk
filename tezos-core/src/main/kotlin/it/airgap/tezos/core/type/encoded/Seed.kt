package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// -- Seed --

public sealed interface Seed : Encoded {
    override val meta: MetaSeed<*, *>

    public companion object {
        internal val kinds: List<MetaSeed.Kind<*, *>>
            get() = listOf(
                Ed25519Seed,
            )

        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

// -- EncryptedSeed --

public sealed interface EncryptedSeed : Encoded {
    override val meta: MetaEncryptedSeed<*, *>

    public companion object {
        internal val kinds: List<MetaEncryptedSeed.Kind<*, *>>
            get() = listOf(
                Ed25519EncryptedSeed,
            )

        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

// -- MetaSeed --

@InternalTezosSdkApi
public sealed interface MetaSeed<out Self : MetaSeed<Self, S>, out S : Seed> : MetaEncoded<Self, S> {
    override val encoded: S

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaSeed<M, S>, out S : Seed> : MetaEncoded.Kind<M, S>
}

// -- MetaEncryptedSeed --

@InternalTezosSdkApi
public sealed interface MetaEncryptedSeed<out Self : MetaEncryptedSeed<Self, ES>, out ES : EncryptedSeed> : MetaEncoded<Self, ES> {
    override val encoded: ES

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaEncryptedSeed<M, ES>, out ES : EncryptedSeed> : MetaEncoded.Kind<M, ES>
}