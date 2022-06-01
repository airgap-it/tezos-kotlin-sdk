package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded secp256K1 encrypted scalar.
 *
 * @property base58 The encoded string: `seesk(93)`.
 */
@JvmInline
public value class Secp256K1EncryptedScalar(override val base58: String) : EncryptedScalar, MetaEncryptedScalar<Secp256K1EncryptedScalar, Secp256K1EncryptedScalar> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 encrypted scalar." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncryptedScalar.Kind<Secp256K1EncryptedScalar, Secp256K1EncryptedScalar>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncryptedScalar<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Secp256K1EncryptedScalar
        get() = this

    public companion object : MetaEncryptedScalar.Kind<Secp256K1EncryptedScalar, Secp256K1EncryptedScalar> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "seesk"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(1, (131).toByte(), 36, 86, (248).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 93

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 60

        /**
         * Creates [Secp256K1EncryptedScalar] from the [base58] string if it's a valid base58 encoded secp256K1 encrypted scalar value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Secp256K1EncryptedScalar? = if (isValid(base58)) Secp256K1EncryptedScalar(base58) else null
    }
}