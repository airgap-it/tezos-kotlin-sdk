package it.airgap.tezos.core.type.encoded

/* Lr(52) */

@JvmInline
public value class OperationMetadataListHash(override val base58: String) : Encoded, MetaEncoded<OperationMetadataListHash> {

    override val kind: MetaEncoded.Kind<OperationMetadataListHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid operation metadata list hash." }
    }

    override fun toMetaEncoded(): MetaEncoded<*> = this
    override fun toEncoded(): Encoded = this

    public companion object : MetaEncoded.Kind<OperationMetadataListHash> {
        override val base58Prefix: String = "Lr"
        override val base58Bytes: ByteArray = byteArrayOf((134).toByte(), 39)
        override val base58Length: Int = 52

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): OperationMetadataListHash? = if (isValid(base58)) OperationMetadataListHash(base58) else null
    }
}