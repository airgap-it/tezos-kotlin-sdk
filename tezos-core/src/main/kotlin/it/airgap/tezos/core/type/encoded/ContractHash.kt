package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

/* KT1(36) */

@JvmInline
public value class ContractHash(override val base58: String) : OriginatedAddress, MetaOriginatedAddress<ContractHash, ContractHash> {

    init {
        require(isValid(base58)) { "Invalid contract hash." }
    }

    override val kind: MetaOriginatedAddress.Kind<ContractHash, ContractHash>
        get() = Companion

    @InternalTezosSdkApi
    override val meta: MetaOriginatedAddress<*, *>
        get() = this

    override val encoded: ContractHash
        get() = this

    public companion object : MetaOriginatedAddress.Kind<ContractHash, ContractHash> {
        override val base58Prefix: String = "KT1"
        override val base58Bytes: ByteArray = byteArrayOf(2, 90, 121)
        override val base58Length: Int = 36

        override val bytesLength: Int = 20

        override fun createValueOrNull(base58: String): ContractHash? = if (isValid(base58)) ContractHash(base58) else null
    }
}