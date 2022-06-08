package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos block metadata hash.
 *
 * @property base58 The encoded string: `bm(52)`.
 */
@JvmInline
public value class BlockMetadataHash(override val base58: String) : Encoded, MetaEncoded<BlockMetadataHash, BlockMetadataHash> {

    init {
        require(isValid(base58)) { "Invalid block metadata hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<BlockMetadataHash, BlockMetadataHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: BlockMetadataHash
        get() = this

    public companion object : MetaEncoded.Kind<BlockMetadataHash, BlockMetadataHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "bm"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf((234).toByte(), (249).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 52

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [BlockMetadataHash] from the [base58] string if it's a valid base58 encoded block metadata hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): BlockMetadataHash? = if (isValid(base58)) BlockMetadataHash(base58) else null
    }
}