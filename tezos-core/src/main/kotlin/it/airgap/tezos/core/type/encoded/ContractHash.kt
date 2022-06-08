package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/**
 * Base58 encoded Tezos contract hash.
 *
 * @property base58 The encoded string: `KT1(36)`.
 */
@JvmInline
public value class ContractHash(override val base58: String) : OriginatedAddress, MetaOriginatedAddress<ContractHash, ContractHash> {

    init {
        require(isValid(base58)) { "Invalid contract hash." }
    }

    @InternalTezosSdkApi
    override val kind: MetaOriginatedAddress.Kind<ContractHash, ContractHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaOriginatedAddress<*, *>
        get() = this

    @InternalTezosSdkApi
    override val encoded: ContractHash
        get() = this

    public companion object : MetaOriginatedAddress.Kind<ContractHash, ContractHash> {

        /**
         * Known prefix of the base58 encoded value.
         */
        override val base58Prefix: String = "KT1"

        /**
         * Bytes that the raw data should be prefixed with before base58 encoding.
         */
        override val base58Bytes: ByteArray = byteArrayOf(2, 90, 121)

        /**
         * Length of the base58 encoded value.
         */
        override val base58Length: Int = 36

        /**
         * Length of the non-encoded data.
         */
        override val bytesLength: Int = 20

        /**
         * Creates [ContractHash] from the [base58] string if it's a valid base58 encoded contract hash value
         * or returns `null` otherwise.
         */
        override fun createValueOrNull(base58: String): ContractHash? = if (isValid(base58)) ContractHash(base58) else null
    }
}