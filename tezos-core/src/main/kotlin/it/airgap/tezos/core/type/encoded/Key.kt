package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Group of base58 encoded cryptographic keys, either secret or public.
 *
 * @see [SecretKey]
 * @see [PublicKey]
 */
public sealed interface Key : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaKey<*, *>

    public companion object {
        internal val kinds: List<MetaKey.Kind<*, *>>
            get() = SecretKey.kinds + PublicKey.kinds

        /**
         * Checks if the [string] is a valid Tezos [Key]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

/**
 * Group of base58 encoded secret keys.
 *
 * @see [Ed25519SecretKey]
 * @see [Secp256K1SecretKey]
 * @see [P256SecretKey]
 */
public sealed interface SecretKey : Key {
    @InternalTezosSdkApi
    override val meta: MetaSecretKey<*, *>

    public companion object {
        internal val kinds: List<MetaSecretKey.Kind<*, *>>
            get() = listOf(
                Ed25519SecretKey,
                Secp256K1SecretKey,
                P256SecretKey,
            )

        /**
         * Checks if the [string] is a valid Tezos [SecretKey]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

/**
 * Group of base58 encoded public keys.
 *
 * @see [Ed25519PublicKey]
 * @see [Secp256K1PublicKey]
 * @see [P256PublicKey]
 */
public sealed interface PublicKey : Key {
    @InternalTezosSdkApi
    override val meta: MetaPublicKey<*, *>

    public companion object {
        internal val kinds: List<MetaPublicKey.Kind<*, *>>
            get() = listOf(
                Ed25519PublicKey,
                Secp256K1PublicKey,
                P256PublicKey,
            )

        /**
         * Checks if the [string] is a valid Tezos [PublicKey]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

/**
 * Group of base58 encoded encrypted cryptographic keys.
 *
 * @see [EncryptedSecretKey]
 */
public sealed interface EncryptedKey : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaEncryptedKey<*, *>

    public companion object {
        internal val kinds: List<MetaEncryptedKey.Kind<*, *>>
            get() = EncryptedSecretKey.kinds

        /**
         * Checks if the [string] is a valid Tezos [EncryptedKey]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

/**
 * Group of base58 encoded encrypted secret keys.
 *
 * @see [Secp256K1EncryptedSecretKey]
 * @see [P256EncryptedSecretKey]
 */
public sealed interface EncryptedSecretKey : EncryptedKey {
    @InternalTezosSdkApi
    override val meta: MetaEncryptedSecretKey<*, *>

    public companion object {
        internal val kinds: List<MetaEncryptedSecretKey.Kind<*, *>>
            get() = listOf(
                Secp256K1EncryptedSecretKey,
                P256EncryptedSecretKey,
            )

        /**
         * Checks if the [string] is a valid Tezos [EncryptedSecretKey]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

// -- meta --

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