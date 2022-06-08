package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Ed25519 blinded public key hash.
 *
 * @property base58 The encoded string: `btz1(37)`.
 */
@JvmInline
public value class Ed25519BlindedPublicKeyHash(override val base58: String) : BlindedPublicKeyHash, MetaBlindedPublicKeyHash<Ed25519BlindedPublicKeyHash, Ed25519BlindedPublicKeyHash> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 blinded public key hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<Ed25519BlindedPublicKeyHash, Ed25519BlindedPublicKeyHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaBlindedPublicKeyHash<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Ed25519BlindedPublicKeyHash
        get() = this

    public companion object : MetaBlindedPublicKeyHash.Kind<Ed25519BlindedPublicKeyHash, Ed25519BlindedPublicKeyHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "btz1"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(1, 2, 49, (223).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 37

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 20

        /**
         * Creates [Ed25519BlindedPublicKeyHash] from the [base58] string if it's a valid base58 encoded Ed25519 blinded public key hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Ed25519BlindedPublicKeyHash? = if (isValid(base58)) Ed25519BlindedPublicKeyHash(base58) else null
    }
}