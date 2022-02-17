package it.airgap.tezos.core.type.encoded

/* LLo(53) */

@JvmInline
public value class OperationListListHash(override val base58: String) : Encoded<OperationListListHash> {

    override val kind: Encoded.Kind<OperationListListHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid operation list list hash." }
    }

    public companion object : Encoded.Kind<OperationListListHash> {
        override val base58Prefix: String = "LLo"
        override val base58Bytes: ByteArray = byteArrayOf(29, (159).toByte(), 109)
        override val base58Length: Int = 53

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): OperationListListHash? = if (isValid(base58)) OperationListListHash(base58) else null
    }
}