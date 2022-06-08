package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos context hash.
 *
 * @property base58 The encoded string: `Co(52)`.
 */
@JvmInline
public value class ContextHash(override val base58: String) : Encoded, MetaEncoded<ContextHash, ContextHash> {

    init {
        require(isValid(base58)) { "Invalid context hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<ContextHash, ContextHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: ContextHash
        get() = this

    public companion object : MetaEncoded.Kind<ContextHash, ContextHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "Co"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(79, (199).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 52

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [ContextHash] from the [base58] string if it's a valid base58 encoded context hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): ContextHash? = if (isValid(base58)) ContextHash(base58) else null
    }
}