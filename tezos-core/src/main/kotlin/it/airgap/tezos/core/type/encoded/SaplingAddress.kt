package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Sapling address.
 *
 * @property base58 The encoded string: `zet1(69)`.
 */
@JvmInline
public value class SaplingAddress(override val base58: String) : Encoded, MetaEncoded<SaplingAddress, SaplingAddress> {

    init {
        require(isValid(base58)) { "Invalid Sapling address." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<SaplingAddress, SaplingAddress>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: SaplingAddress
        get() = this

    public companion object : MetaEncoded.Kind<SaplingAddress, SaplingAddress> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "zet1"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(18, 71, 40, (223).toByte())

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 69

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 43

        /**
         * Creates [SaplingAddress] from the [base58] string if it's a valid base58 encoded Sapling address value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): SaplingAddress? = if (isValid(base58)) SaplingAddress(base58) else null
    }
}