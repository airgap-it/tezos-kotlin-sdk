package it.airgap.tezos.core.type.encoded

/* vh(52) */

@JvmInline
public value class BlockPayloadHash(override val base58: String) : Encoded, MetaEncoded<BlockPayloadHash> {

    override val kind: MetaEncoded.Kind<BlockPayloadHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid block payload hash." }
    }

    override fun toMetaEncoded(): MetaEncoded<*> = this
    override fun toEncoded(): Encoded = this

    public companion object : MetaEncoded.Kind<BlockPayloadHash> {
        override val base58Prefix: String = "vh"
        override val base58Bytes: ByteArray = byteArrayOf(1, 106, (242).toByte())
        override val base58Length: Int = 52

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): BlockPayloadHash? = if (isValid(base58)) BlockPayloadHash(base58) else null
    }
}