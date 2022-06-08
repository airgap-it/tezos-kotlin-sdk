package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded secp256K1 encrypted secret key.
 *
 * @property base58 The encoded string: `spesk(88)`.
 */
@JvmInline
public value class Secp256K1EncryptedSecretKey(override val base58: String) : EncryptedSecretKey, MetaEncryptedSecretKey<Secp256K1EncryptedSecretKey, Secp256K1EncryptedSecretKey> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 encrypted secret key." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncryptedSecretKey.Kind<Secp256K1EncryptedSecretKey, Secp256K1EncryptedSecretKey>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncryptedSecretKey<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Secp256K1EncryptedSecretKey
        get() = this

    public companion object : MetaEncryptedSecretKey.Kind<Secp256K1EncryptedSecretKey, Secp256K1EncryptedSecretKey> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "spesk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(9, (237).toByte(), (241).toByte(), (174).toByte(), (150).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 88

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 56

        /**
         * Creates [Secp256K1EncryptedSecretKey] from the [base58] string if it's a valid base58 encoded secp256K1 encrypted secret key value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Secp256K1EncryptedSecretKey? = if (isValid(base58)) Secp256K1EncryptedSecretKey(base58) else null
    }
}