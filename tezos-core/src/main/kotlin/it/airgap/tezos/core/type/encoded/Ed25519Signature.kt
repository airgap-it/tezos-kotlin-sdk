package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Ed25519 signature.
 *
 * @property base58 The encoded string: `edsig(99)`.
 */
@JvmInline
public value class Ed25519Signature(override val base58: String) : Signature, MetaSignature<Ed25519Signature, Ed25519Signature> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 signature." }
    }

    @InternalTezosSdkApi
    override val kind: MetaSignature.Kind<Ed25519Signature, Ed25519Signature>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaSignature<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Ed25519Signature
        get() = this

    public companion object : MetaSignature.Kind<Ed25519Signature, Ed25519Signature> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "edsig"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(9, (245).toByte(), (205).toByte(), (134).toByte(), 18)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 99

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 64

        /**
         * Creates [Ed25519Signature] from the [base58] string if it's a valid base58 encoded Ed25519 signature value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Ed25519Signature? = if (isValid(base58)) Ed25519Signature(base58) else null
    }
}