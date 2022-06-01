package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded P-256 signature.
 *
 * @property base58 The encoded string: `p2sig(98)`.
 */
@JvmInline
public value class P256Signature(override val base58: String) : Signature, MetaSignature<P256Signature, P256Signature> {

    init {
        require(isValid(base58)) { "Invalid P256 signature." }
    }

    @InternalTezosSdkApi
    override val kind: MetaSignature.Kind<P256Signature, P256Signature>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaSignature<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: P256Signature
        get() = this

    public companion object : MetaSignature.Kind<P256Signature, P256Signature> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "p2sig"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(54, (240).toByte(), 44, 52)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 98

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 64

        /**
         * Creates [P256Signature] from the [base58] string if it's a valid base58 encoded P-256 signature value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): P256Signature? = if (isValid(base58)) P256Signature(base58) else null
    }
}