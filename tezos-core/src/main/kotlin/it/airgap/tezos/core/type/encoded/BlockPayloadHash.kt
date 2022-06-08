package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos block payload hash.
 *
 * @property base58 The encoded string: `vh(52)`.
 */
@JvmInline
public value class BlockPayloadHash(override val base58: String) : Encoded, MetaEncoded<BlockPayloadHash, BlockPayloadHash> {

    init {
        require(isValid(base58)) { "Invalid block payload hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<BlockPayloadHash, BlockPayloadHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: BlockPayloadHash
        get() = this

    public companion object : MetaEncoded.Kind<BlockPayloadHash, BlockPayloadHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "vh"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(1, 106, (242).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 52

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [BlockPayloadHash] from the [base58] string if it's a valid base58 encoded block payload hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): BlockPayloadHash? = if (isValid(base58)) BlockPayloadHash(base58) else null
    }
}