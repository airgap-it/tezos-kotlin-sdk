package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/* edsk(54) */
/**
 * Base58 encoded Ed25519 blinded public key hash.
 *
 * @property base58 The encoded string: `edsk(54)`.
 */
@JvmInline
public value class Ed25519Seed(override val base58: String) : Seed, MetaSeed<Ed25519Seed, Ed25519Seed> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 seed." }
    }

    @InternalTezosSdkApi
    override val kind: MetaSeed.Kind<Ed25519Seed, Ed25519Seed>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaSeed<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Ed25519Seed
        get() = this

    public companion object : MetaSeed.Kind<Ed25519Seed, Ed25519Seed> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "edsk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(13, 15, 58, 7)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 54

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [Ed25519Seed] from the [base58] string if it's a valid base58 encoded Ed25519 seed value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Ed25519Seed? = if (isValid(base58)) Ed25519Seed(base58) else null
    }
}