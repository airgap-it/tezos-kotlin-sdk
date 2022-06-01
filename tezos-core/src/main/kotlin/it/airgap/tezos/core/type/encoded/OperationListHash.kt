package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos operation list hash.
 *
 * @property base58 The encoded string: `Lo(52)`.
 */
@JvmInline
public value class OperationListHash(override val base58: String) : Encoded, MetaEncoded<OperationListHash, OperationListHash> {

    init {
        require(isValid(base58)) { "Invalid operation list hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<OperationListHash, OperationListHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: OperationListHash
        get() = this

    public companion object : MetaEncoded.Kind<OperationListHash, OperationListHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "Lo"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf((133).toByte(), (233).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 52

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [OperationListHash] from the [base58] string if it's a valid base58 encoded operation list hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): OperationListHash? = if (isValid(base58)) OperationListHash(base58) else null
    }
}