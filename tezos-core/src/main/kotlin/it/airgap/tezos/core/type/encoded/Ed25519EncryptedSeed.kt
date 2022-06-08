package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Ed25519 encrypted seed.
 *
 * @property base58 The encoded string: `edesk(88)`.
 */
@JvmInline
public value class Ed25519EncryptedSeed(override val base58: String) : EncryptedSeed, MetaEncryptedSeed<Ed25519EncryptedSeed, Ed25519EncryptedSeed> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 encrypted seed." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncryptedSeed.Kind<Ed25519EncryptedSeed, Ed25519EncryptedSeed>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncryptedSeed<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Ed25519EncryptedSeed
        get() = this

    public companion object : MetaEncryptedSeed.Kind<Ed25519EncryptedSeed, Ed25519EncryptedSeed> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "edesk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(7, 90, 60, (179).toByte(), 41)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 88

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 56

        /**
         * Creates [Ed25519EncryptedSeed] from the [base58] string if it's a valid base58 encoded Ed25519 encrypted seed value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Ed25519EncryptedSeed? = if (isValid(base58)) Ed25519EncryptedSeed(base58) else null
    }
}