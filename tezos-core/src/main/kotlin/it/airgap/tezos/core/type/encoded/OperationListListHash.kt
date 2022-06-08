package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos list of operation lists hash.
 *
 * @property base58 The encoded string: `LLo(53)`.
 */
@JvmInline
public value class OperationListListHash(override val base58: String) : Encoded, MetaEncoded<OperationListListHash, OperationListListHash> {

    init {
        require(isValid(base58)) { "Invalid operation list list hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<OperationListListHash, OperationListListHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: OperationListListHash
        get() = this

    public companion object : MetaEncoded.Kind<OperationListListHash, OperationListListHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "LLo"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(29, (159).toByte(), 109)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 53

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [OperationListListHash] from the [base58] string if it's a valid base58 encoded list of operation list hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): OperationListListHash? = if (isValid(base58)) OperationListListHash(base58) else null
    }
}