package it.airgap.tezos.core.type.encoded

/* KT1(36) */

@JvmInline
public value class ContractHash(override val base58: String) : OriginatedAddress<ContractHash> {

    override val kind: OriginatedAddress.Kind<ContractHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid contract hash." }
    }

    public companion object : OriginatedAddress.Kind<ContractHash> {
        override val base58Prefix: String = "KT1"
        override val base58Bytes: ByteArray = byteArrayOf(2, 90, 121)
        override val base58Length: Int = 36

        override val bytesLength: Int = 20

        override fun createValueOrNull(base58: String): ContractHash? = if (isValid(base58)) ContractHash(base58) else null
    }
}