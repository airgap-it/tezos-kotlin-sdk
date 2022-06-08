package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded P-256 public key hash.
 *
 * @property base58 The encoded string: `tz3(36)`.
 */
@JvmInline
public value class P256PublicKeyHash(override val base58: String) : PublicKeyHash, MetaPublicKeyHash<P256PublicKeyHash, P256PublicKeyHash> {

    init {
        require(isValid(base58)) { "Invalid P256 public key hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaPublicKeyHash.Kind<P256PublicKeyHash, P256PublicKeyHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaPublicKeyHash<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: P256PublicKeyHash
        get() = this

    public companion object : MetaPublicKeyHash.Kind<P256PublicKeyHash, P256PublicKeyHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "tz3"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(6, (161).toByte(), (164).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 36

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 20

        /**
         * Creates [P256PublicKeyHash] from the [base58] string if it's a valid base58 encoded P-256 public key hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): P256PublicKeyHash? = if (isValid(base58)) P256PublicKeyHash(base58) else null
    }
}