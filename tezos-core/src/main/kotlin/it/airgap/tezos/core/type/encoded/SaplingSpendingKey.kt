package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Sapling spending key.
 *
 * @property base58 The encoded string: `sask(241)`.
 */
@JvmInline
public value class SaplingSpendingKey(override val base58: String) : Encoded, MetaEncoded<SaplingSpendingKey, SaplingSpendingKey> {

    init {
        require(isValid(base58)) { "Invalid Sapling spending key." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<SaplingSpendingKey, SaplingSpendingKey>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: SaplingSpendingKey
        get() = this

    public companion object : MetaEncoded.Kind<SaplingSpendingKey, SaplingSpendingKey> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "sask"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(11, (237).toByte(), 20, 92)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 241

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 169

        /**
         * Creates [SaplingSpendingKey] from the [base58] string if it's a valid base58 encoded Sapling spending key value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): SaplingSpendingKey? = if (isValid(base58)) SaplingSpendingKey(base58) else null
    }
}