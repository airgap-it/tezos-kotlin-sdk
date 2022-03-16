package it.airgap.tezos.core.type.encoded

/* B(51) */

@JvmInline
public value class BlockHash(override val base58: String) : Encoded, MetaEncoded<BlockHash> {

    override val kind: MetaEncoded.Kind<BlockHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid block hash." }
    }

    override fun toMetaEncoded(): MetaEncoded<*> = this
    override fun toEncoded(): Encoded = this

    public companion object : MetaEncoded.Kind<BlockHash> {
        override val base58Prefix: String = "B"
        override val base58Bytes: ByteArray = byteArrayOf(1, 52)
        override val base58Length: Int = 51

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): BlockHash? = if (isValid(base58)) BlockHash(base58) else null
    }
}