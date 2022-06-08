package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded secp256K1 secret key.
 *
 * @property base58 The encoded string: `spsk(54)`.
 */
@JvmInline
public value class Secp256K1SecretKey(override val base58: String) : SecretKey, MetaSecretKey<Secp256K1SecretKey, Secp256K1SecretKey> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 secret key." }
    }

    @InternalTezosSdkApi
    override val kind: MetaSecretKey.Kind<Secp256K1SecretKey, Secp256K1SecretKey>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaSecretKey<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Secp256K1SecretKey
        get() = this

    public companion object : MetaSecretKey.Kind<Secp256K1SecretKey, Secp256K1SecretKey> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "spsk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(17, (162).toByte(), (224).toByte(), (201).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 54

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [Secp256K1SecretKey] from the [base58] string if it's a valid base58 encoded secp256K1 secret key value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Secp256K1SecretKey? = if (isValid(base58)) Secp256K1SecretKey(base58) else null
    }
}