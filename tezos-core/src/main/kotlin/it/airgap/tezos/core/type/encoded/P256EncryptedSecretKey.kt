package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded P-256 encrypted secret key.
 *
 * @property base58 The encoded string: `p2esk(88)`.
 */
@JvmInline
public value class P256EncryptedSecretKey(override val base58: String) : EncryptedSecretKey, MetaEncryptedSecretKey<P256EncryptedSecretKey, P256EncryptedSecretKey> {

    init {
        require(isValid(base58)) { "Invalid P256 encrypted secret key." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncryptedSecretKey.Kind<P256EncryptedSecretKey, P256EncryptedSecretKey>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncryptedSecretKey<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: P256EncryptedSecretKey
        get() = this

    public companion object : MetaEncryptedSecretKey.Kind<P256EncryptedSecretKey, P256EncryptedSecretKey> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "p2esk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(9, 48, 57, 115, (171).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 88

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 56

        /**
         * Creates [P256EncryptedSecretKey] from the [base58] string if it's a valid base58 encoded P-256 encrypted secret key value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): P256EncryptedSecretKey? = if (isValid(base58)) P256EncryptedSecretKey(base58) else null
    }
}