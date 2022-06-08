package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Cryptobox public key hash.
 *
 * @property base58 The encoded string: `id(30)`.
 */
@JvmInline
public value class CryptoboxPublicKeyHash(override val base58: String) : Encoded, MetaEncoded<CryptoboxPublicKeyHash, CryptoboxPublicKeyHash> {

    init {
        require(isValid(base58)) { "Invalid Cryptobox key hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<CryptoboxPublicKeyHash, CryptoboxPublicKeyHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: CryptoboxPublicKeyHash
        get() = this

    public companion object : MetaEncoded.Kind<CryptoboxPublicKeyHash, CryptoboxPublicKeyHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "id"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf((153).toByte(), (103).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 30

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 16

        /**
         * Creates [CryptoboxPublicKeyHash] from the [base58] string if it's a valid base58 encoded Cryptobox public key hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): CryptoboxPublicKeyHash? = if (isValid(base58)) CryptoboxPublicKeyHash(base58) else null
    }
}