package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos generic signature.
 *
 * @property base58 The encoded string: `sig(96)`.
 */
@JvmInline
public value class GenericSignature(override val base58: String) : Signature, MetaSignature<GenericSignature, GenericSignature> {

    init {
        require(isValid(base58)) { "Invalid generic signature." }
    }

    @InternalTezosSdkApi
    override val kind: MetaSignature.Kind<GenericSignature, GenericSignature>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaSignature<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: GenericSignature
        get() = this

    public companion object : MetaSignature.Kind<GenericSignature, GenericSignature> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "sig"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(4, (130).toByte(), 43)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 96

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 64

        /**
         * Creates [GenericSignature] from the [base58] string if it's a valid base58 encoded generic signature value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): GenericSignature? = if (isValid(base58)) GenericSignature(base58) else null
    }
}