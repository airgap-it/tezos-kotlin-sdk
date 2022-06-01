package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded secp256K1 element.
 *
 * @property base58 The encoded string: `GSp(54)`.
 */
@JvmInline
public value class Secp256K1Element(override val base58: String) : Encoded, MetaEncoded<Secp256K1Element, Secp256K1Element> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 element." }
    }

    @InternalTezosSdkApi
    override val kind: MetaEncoded.Kind<Secp256K1Element, Secp256K1Element>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaEncoded<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: Secp256K1Element
        get() = this

    public companion object : MetaEncoded.Kind<Secp256K1Element, Secp256K1Element> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "GSp"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(5, 92, 0)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 54

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 33

        /**
         * Creates [Secp256K1Element] from the [base58] string if it's a valid base58 encoded secp256K1 element value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): Secp256K1Element? = if (isValid(base58)) Secp256K1Element(base58) else null
    }
}