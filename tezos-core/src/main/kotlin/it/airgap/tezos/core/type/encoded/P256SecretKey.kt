package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded P-256 secret key.
 *
 * @property base58 The encoded string: `p2sk(54)`.
 */
@JvmInline
public value class P256SecretKey(override val base58: String) : SecretKey, MetaSecretKey<P256SecretKey, P256SecretKey> {

    init {
        require(isValid(base58)) { "Invalid P256 secret key." }
    }

    @InternalTezosSdkApi
    override val kind: MetaSecretKey.Kind<P256SecretKey, P256SecretKey>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaSecretKey<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: P256SecretKey
        get() = this

    public companion object : MetaSecretKey.Kind<P256SecretKey, P256SecretKey> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "p2sk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(16, 81, (238).toByte(), (189).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 54

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [P256SecretKey] from the [base58] string if it's a valid base58 encoded P-256 secret key value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): P256SecretKey? = if (isValid(base58)) P256SecretKey(base58) else null
    }
}