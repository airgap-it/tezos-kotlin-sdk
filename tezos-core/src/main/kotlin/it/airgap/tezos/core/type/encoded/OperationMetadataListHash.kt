package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos operation metadata list hash.
 *
 * @property base58 The encoded string: `Lr(52)`.
 */
@JvmInline
public value class OperationMetadataListHash(override val base58: String) : Encoded, MetaEncoded<OperationMetadataListHash, OperationMetadataListHash> {

    init {
        require(isValid(base58)) { "Invalid operation metadata list hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<OperationMetadataListHash, OperationMetadataListHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: OperationMetadataListHash
        get() = this

    public companion object : MetaEncoded.Kind<OperationMetadataListHash, OperationMetadataListHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "Lr"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf((134).toByte(), 39)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 52

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [OperationMetadataListHash] from the [base58] string if it's a valid base58 encoded operation metadata list hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): OperationMetadataListHash? = if (isValid(base58)) OperationMetadataListHash(base58) else null
    }
}