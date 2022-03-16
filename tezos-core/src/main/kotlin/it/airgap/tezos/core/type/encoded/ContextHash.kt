package it.airgap.tezos.core.type.encoded

/* Co(52) */

@JvmInline
public value class ContextHash(override val base58: String) : Encoded, MetaEncoded<ContextHash> {

    override val kind: MetaEncoded.Kind<ContextHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid context hash." }
    }

    override fun toMetaEncoded(): MetaEncoded<*> = this
    override fun toEncoded(): Encoded = this

    public companion object : MetaEncoded.Kind<ContextHash> {
        override val base58Prefix: String = "Co"
        override val base58Bytes: ByteArray = byteArrayOf(79, (199).toByte())
        override val base58Length: Int = 52

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): ContextHash? = if (isValid(base58)) ContextHash(base58) else null
    }
}