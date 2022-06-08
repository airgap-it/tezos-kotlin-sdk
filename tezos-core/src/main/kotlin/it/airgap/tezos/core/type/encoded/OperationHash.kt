package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos operation hash.
 *
 * @property base58 The encoded string: `o(51)`.
 */
@JvmInline
public value class OperationHash(override val base58: String) : Encoded, MetaEncoded<OperationHash, OperationHash> {

    init {
        require(isValid(base58)) { "Invalid operation hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<OperationHash, OperationHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: OperationHash
        get() = this

    public companion object : MetaEncoded.Kind<OperationHash, OperationHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "o"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(5, 116)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 51

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [OperationHash] from the [base58] string if it's a valid base58 encoded operation hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): OperationHash? = if (isValid(base58)) OperationHash(base58) else null
    }
}