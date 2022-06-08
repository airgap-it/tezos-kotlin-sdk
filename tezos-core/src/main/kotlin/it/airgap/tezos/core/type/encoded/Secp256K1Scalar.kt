package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded secp256K1 scalar.
 *
 * @property base58 The encoded string: `SSp(93)`.
 */
@JvmInline
public value class Secp256K1Scalar(override val base58: String) : Scalar, MetaScalar<Secp256K1Scalar, Secp256K1Scalar> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 scalar." }
    }

    @InternalTezosSdkApi
    override val kind: MetaScalar.Kind<Secp256K1Scalar, Secp256K1Scalar>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaScalar<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Secp256K1Scalar
        get() = this

    public companion object : MetaScalar.Kind<Secp256K1Scalar, Secp256K1Scalar> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "SSp"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(38, (248).toByte(), (136).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 53

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [Secp256K1Scalar] from the [base58] string if it's a valid base58 encoded secp256K1 scalar value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Secp256K1Scalar? = if (isValid(base58)) Secp256K1Scalar(base58) else null
    }
}