package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos list of operation lists hash.
 *
 * @property base58 The encoded string: `LLr(53)`.
 */
@JvmInline
public value class OperationMetadataListListHash(override val base58: String) : Encoded, MetaEncoded<OperationMetadataListListHash, OperationMetadataListListHash> {

    init {
        require(isValid(base58)) { "Invalid operation metadata list list hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<OperationMetadataListListHash, OperationMetadataListListHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: OperationMetadataListListHash
        get() = this

    public companion object : MetaEncoded.Kind<OperationMetadataListListHash, OperationMetadataListListHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "LLr"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(29, (159).toByte(), (182).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 53

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [OperationMetadataListListHash] from the [base58] string if it's a valid base58 encoded list of operation metadata list hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): OperationMetadataListListHash? = if (isValid(base58)) OperationMetadataListListHash(base58) else null
    }
}