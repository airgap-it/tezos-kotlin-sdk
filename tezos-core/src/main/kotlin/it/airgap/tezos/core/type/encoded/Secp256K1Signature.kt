package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded secp256K1 signature.
 *
 * @property base58 The encoded string: `spsig1(99)`.
 */
@JvmInline
public value class Secp256K1Signature(override val base58: String) : Signature, MetaSignature<Secp256K1Signature, Secp256K1Signature> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 signature." }
    }

    @InternalTezosSdkApi
    override val kind: MetaSignature.Kind<Secp256K1Signature, Secp256K1Signature>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaSignature<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Secp256K1Signature
        get() = this

    public companion object : MetaSignature.Kind<Secp256K1Signature, Secp256K1Signature> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "spsig1"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(13, 115.toByte(), 101, 19, 63)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 99

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 64

        /**
         * Creates [Secp256K1Signature] from the [base58] string if it's a valid base58 encoded secp256K1 signature value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Secp256K1Signature? = if (isValid(base58)) Secp256K1Signature(base58) else null
    }
}