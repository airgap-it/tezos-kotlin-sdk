package it.airgap.tezos.core.type.encoded

/* LLr(53) */

@JvmInline
public value class OperationMetadataListListHash(override val base58: String) : Encoded, MetaEncoded<OperationMetadataListListHash> {

    init {
        require(isValid(base58)) { "Invalid operation metadata list list hash." }
    }

    override val kind: MetaEncoded.Kind<OperationMetadataListListHash>
        get() = Companion

    override val meta: MetaEncoded<*>
        get() = this

    override val encoded: Encoded
        get() = this

    public companion object : MetaEncoded.Kind<OperationMetadataListListHash> {
        override val base58Prefix: String = "LLr"
        override val base58Bytes: ByteArray = byteArrayOf(29, (159).toByte(), (182).toByte())
        override val base58Length: Int = 53

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): OperationMetadataListListHash? = if (isValid(base58)) OperationMetadataListListHash(base58) else null
    }
}