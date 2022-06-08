package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded secp256K1 public key.
 *
 * @property base58 The encoded string: `sppk(55)`.
 */
@JvmInline
public value class Secp256K1PublicKey(override val base58: String) : PublicKey, MetaPublicKey<Secp256K1PublicKey, Secp256K1PublicKey> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 public key." }
    }

    @InternalTezosSdkApi
    override val kind: MetaPublicKey.Kind<Secp256K1PublicKey, Secp256K1PublicKey>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaPublicKey<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Secp256K1PublicKey
        get() = this

    public companion object : MetaPublicKey.Kind<Secp256K1PublicKey, Secp256K1PublicKey> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "sppk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(3, (254).toByte(), (226).toByte(), 86)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 55

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 33

        /**
         * Creates [Secp256K1PublicKey] from the [base58] string if it's a valid base58 encoded secp256K1 public key value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Secp256K1PublicKey? = if (isValid(base58)) Secp256K1PublicKey(base58) else null
    }
}