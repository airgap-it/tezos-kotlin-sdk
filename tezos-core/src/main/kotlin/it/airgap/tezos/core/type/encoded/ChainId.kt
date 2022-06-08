package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos chain identifier.
 *
 * @property base58 The encoded string: `Net(15)`.
 */
@JvmInline
public value class ChainId(override val base58: String) : Encoded, MetaEncoded<ChainId, ChainId> {

    init {
        require(isValid(base58)) { "Invalid chain ID." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<ChainId, ChainId>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: ChainId
        get() = this

    public companion object : MetaEncoded.Kind<ChainId, ChainId> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "Net"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(87, 82, 0)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 15

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 4

        /**
         * Creates [ChainId] from the [base58] string if it's a valid base58 encoded chain identifier value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): ChainId? = if (isValid(base58)) ChainId(base58) else null
    }
}