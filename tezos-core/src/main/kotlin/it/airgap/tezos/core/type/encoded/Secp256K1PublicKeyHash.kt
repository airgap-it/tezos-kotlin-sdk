package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded secp256K1 public key hash.
 *
 * @property base58 The encoded string: `tz2(36)`.
 */
@JvmInline
public value class Secp256K1PublicKeyHash(override val base58: String) : PublicKeyHash, MetaPublicKeyHash<Secp256K1PublicKeyHash, Secp256K1PublicKeyHash> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 public key hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaPublicKeyHash.Kind<Secp256K1PublicKeyHash, Secp256K1PublicKeyHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaPublicKeyHash<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Secp256K1PublicKeyHash
        get() = this

    public companion object : MetaPublicKeyHash.Kind<Secp256K1PublicKeyHash, Secp256K1PublicKeyHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "tz2"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(6, (161).toByte(), (161).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 36

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 20

        /**
         * Creates [Secp256K1PublicKeyHash] from the [base58] string if it's a valid base58 encoded secp256K1 public key hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Secp256K1PublicKeyHash? = if (isValid(base58)) Secp256K1PublicKeyHash(base58) else null
    }
}