package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Ed25519 secret key.
 *
 * @property base58 The encoded string: `edsk(98)`.
 */
@JvmInline
public value class Ed25519SecretKey(override val base58: String) : SecretKey, MetaSecretKey<Ed25519SecretKey, Ed25519SecretKey> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 secret key." }
    }

    @InternalTezosSdkApi
    override val kind: MetaSecretKey.Kind<Ed25519SecretKey, Ed25519SecretKey>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaSecretKey<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Ed25519SecretKey
        get() = this

    public companion object : MetaSecretKey.Kind<Ed25519SecretKey, Ed25519SecretKey> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "edsk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(43, (246).toByte(), 78, 7)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 98

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 64

        /**
         * Creates [Ed25519SecretKey] from the [base58] string if it's a valid base58 encoded Ed25519 secret key value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Ed25519SecretKey? = if (isValid(base58)) Ed25519SecretKey(base58) else null
    }
}