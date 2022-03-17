package it.airgap.tezos.core.type.encoded

/* Lo(52) */

@JvmInline
public value class OperationListHash(override val base58: String) : Encoded, MetaEncoded<OperationListHash> {

    init {
        require(isValid(base58)) { "Invalid operation list hash." }
    }

    override val kind: MetaEncoded.Kind<OperationListHash>
        get() = Companion

    override val meta: MetaEncoded<*>
        get() = this

    override val encoded: Encoded
        get() = this

    public companion object : MetaEncoded.Kind<OperationListHash> {
        override val base58Prefix: String = "Lo"
        override val base58Bytes: ByteArray = byteArrayOf((133).toByte(), (233).toByte())
        override val base58Length: Int = 52

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): OperationListHash? = if (isValid(base58)) OperationListHash(base58) else null
    }
}