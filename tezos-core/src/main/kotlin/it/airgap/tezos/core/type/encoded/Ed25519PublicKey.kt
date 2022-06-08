package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Ed25519 public key.
 *
 * @property base58 The encoded string: `edpk(54)`.
 */
@JvmInline
public value class Ed25519PublicKey(override val base58: String) : PublicKey, MetaPublicKey<Ed25519PublicKey, Ed25519PublicKey> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 public key." }
    }

    @InternalTezosSdkApi
    override val kind: MetaPublicKey.Kind<Ed25519PublicKey, Ed25519PublicKey>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaPublicKey<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Ed25519PublicKey
        get() = this

    public companion object : MetaPublicKey.Kind<Ed25519PublicKey, Ed25519PublicKey> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "edpk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(13, 15, 37, (217).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 54

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [Ed25519PublicKey] from the [base58] string if it's a valid base58 encoded Ed25519 public key hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Ed25519PublicKey? = if (isValid(base58)) Ed25519PublicKey(base58) else null
    }
}