package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/* P(51) */
/**
 * Base58 encoded Tezos protocol hash.
 *
 * @property base58 The encoded string: `P(51)`.
 */
@JvmInline
public value class ProtocolHash(override val base58: String) : Encoded, MetaEncoded<ProtocolHash, ProtocolHash> {

    init {
        require(isValid(base58)) { "Invalid protocol hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<ProtocolHash, ProtocolHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: ProtocolHash
        get() = this

    public companion object : MetaEncoded.Kind<ProtocolHash, ProtocolHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "P"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(2, (170).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 51

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [ProtocolHash] from the [base58] string if it's a valid base58 encoded protocol hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): ProtocolHash? = if (isValid(base58)) ProtocolHash(base58) else null
    }
}