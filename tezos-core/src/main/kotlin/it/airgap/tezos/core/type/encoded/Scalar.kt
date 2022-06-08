package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Group of base58 encoded scalars.
 *
 * @see [Secp256K1Scalar]
 */
public sealed interface Scalar : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaScalar<*, *>

    public companion object {
        internal val kinds: List<MetaScalar.Kind<*, *>>
            get() = listOf(
                Secp256K1Scalar,
            )

        /**
         * Checks if [string] is a valid Tezos [Scalar]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

/**
 * Group of base58 encoded encrypted scalars.
 *
 * @see [Secp256K1EncryptedScalar]
 */
public sealed interface EncryptedScalar : Encoded {
    @InternalTezosSdkApi
    override val meta: MetaEncryptedScalar<*, *>

    public companion object {
        internal val kinds: List<MetaEncryptedScalar.Kind<*, *>>
            get() = listOf(
                Secp256K1EncryptedScalar,
            )

        /**
         * Checks if [string] is a valid Tezos [EncryptedScalar]
         */
        public fun isValid(string: String): Boolean = kinds.any { it.isValid(string) }
    }
}

// -- meta --

@InternalTezosSdkApi
public sealed interface MetaScalar<out Self : MetaScalar<Self, S>, out S : Scalar> : MetaEncoded<Self, S> {
    override val encoded: S

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaScalar<M, S>, out S : Scalar> : MetaEncoded.Kind<M, S>
}

@InternalTezosSdkApi
public sealed interface MetaEncryptedScalar<out Self : MetaEncryptedScalar<Self, ES>, out ES : EncryptedScalar> : MetaEncoded<Self, ES> {
    override val encoded: ES

    @InternalTezosSdkApi
    public sealed interface Kind<out M : MetaEncryptedScalar<M, ES>, out ES : EncryptedScalar> : MetaEncoded.Kind<M, ES>
}