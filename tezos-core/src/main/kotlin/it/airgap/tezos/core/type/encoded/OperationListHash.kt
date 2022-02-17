package it.airgap.tezos.core.type.encoded

/* Lo(52) */

@JvmInline
public value class OperationListHash(override val base58: String) : Encoded<OperationListHash> {

    override val kind: Encoded.Kind<OperationListHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid operation list hash." }
    }

    public companion object : Encoded.Kind<OperationListHash> {
        override val base58Prefix: String = "Lo"
        override val base58Bytes: ByteArray = byteArrayOf((133).toByte(), (233).toByte())
        override val base58Length: Int = 52

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): OperationListHash? = if (isValid(base58)) OperationListHash(base58) else null
    }
}