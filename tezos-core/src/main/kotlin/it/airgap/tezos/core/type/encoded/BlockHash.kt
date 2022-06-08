package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos block hash.
 *
 * @property base58 The encoded string: `B(51)`.
 */
@JvmInline
public value class BlockHash(override val base58: String) : Encoded, MetaEncoded<BlockHash, BlockHash> {

    init {
        require(isValid(base58)) { "Invalid block hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<BlockHash, BlockHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: BlockHash
        get() = this

    public companion object : MetaEncoded.Kind<BlockHash, BlockHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "B"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(1, 52)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 51

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [BlockHash] from the [base58] string if it's a valid base58 encoded block hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): BlockHash? = if (isValid(base58)) BlockHash(base58) else null
    }
}