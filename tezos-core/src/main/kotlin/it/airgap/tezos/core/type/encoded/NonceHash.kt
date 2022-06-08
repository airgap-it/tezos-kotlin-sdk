package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos nonce hash.
 *
 * @property base58 The encoded string: `nce(53)`.
 */
@JvmInline
public value class NonceHash(override val base58: String) : Encoded, MetaEncoded<NonceHash, NonceHash> {

    init {
        require(isValid(base58)) { "Invalid nonce hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<NonceHash, NonceHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: NonceHash
        get() = this

    public companion object : MetaEncoded.Kind<NonceHash, NonceHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "nce"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(69, (220).toByte(), (169).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 53

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [NonceHash] from the [base58] string if it's a valid base58 encoded Ed25519 nonce hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): NonceHash? = if (isValid(base58)) NonceHash(base58) else null
    }
}