package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Ed25519 public key hash.
 *
 * @property base58 The encoded string: `tz1(36)`.
 */
@JvmInline
public value class Ed25519PublicKeyHash(override val base58: String) : PublicKeyHash, MetaPublicKeyHash<Ed25519PublicKeyHash, Ed25519PublicKeyHash> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 public key hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaPublicKeyHash.Kind<Ed25519PublicKeyHash, Ed25519PublicKeyHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaPublicKeyHash<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Ed25519PublicKeyHash
        get() = this

    public companion object : MetaPublicKeyHash.Kind<Ed25519PublicKeyHash, Ed25519PublicKeyHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "tz1"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(6, (161).toByte(), (159).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 36

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 20

        /**
         * Creates [Ed25519PublicKeyHash] from the [base58] string if it's a valid base58 encoded Ed25519 encrypted public key hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Ed25519PublicKeyHash? = if (isValid(base58)) Ed25519PublicKeyHash(base58) else null
    }
}