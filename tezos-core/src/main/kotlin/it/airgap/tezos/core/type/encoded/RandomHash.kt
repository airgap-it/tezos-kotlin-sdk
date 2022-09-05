package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded random hash.
 *
 * @property base58 The encoded string: `rng(53)`.
 */
@JvmInline
public value class RandomHash(override val base58: String) : Encoded, MetaEncoded<RandomHash, RandomHash> {

    init {
        require(isValid(base58)) { "Invalid random hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<RandomHash, RandomHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: RandomHash
        get() = this

    public companion object : MetaEncoded.Kind<RandomHash, RandomHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "rng"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(76, 64, (204).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 53

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 32

        /**
         * Creates [RandomHash] from the [base58] string if it's a valid base58 encoded random hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): RandomHash? = if (isValid(base58)) RandomHash(base58) else null
    }
}