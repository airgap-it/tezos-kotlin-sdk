package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos operation metadata hash.
 *
 * @property base58 The encoded string: `r(51)`.
 */
@JvmInline
public value class OperationMetadataHash(override val base58: String) : Encoded, MetaEncoded<OperationMetadataHash, OperationMetadataHash> {

    init {
        require(isValid(base58)) { "Invalid operation metadata hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<OperationMetadataHash, OperationMetadataHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: OperationMetadataHash
        get() = this

    public companion object : MetaEncoded.Kind<OperationMetadataHash, OperationMetadataHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "r"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(5, (183).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 51

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [OperationMetadataHash] from the [base58] string if it's a valid base58 encoded operation metadata hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): OperationMetadataHash? = if (isValid(base58)) OperationMetadataHash(base58) else null
    }
}