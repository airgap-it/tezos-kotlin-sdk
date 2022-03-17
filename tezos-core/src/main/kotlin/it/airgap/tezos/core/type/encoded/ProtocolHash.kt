package it.airgap.tezos.core.type.encoded

/* P(51) */

@JvmInline
public value class ProtocolHash(override val base58: String) : Encoded, MetaEncoded<ProtocolHash> {

    init {
        require(isValid(base58)) { "Invalid protocol hash." }
    }

    override val kind: MetaEncoded.Kind<ProtocolHash>
        get() = Companion

    override val meta: MetaEncoded<*>
        get() = this

    override val encoded: Encoded
        get() = this

    public companion object : MetaEncoded.Kind<ProtocolHash> {
        override val base58Prefix: String = "P"
        override val base58Bytes: ByteArray = byteArrayOf(2, (170).toByte())
        override val base58Length: Int = 51

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): ProtocolHash? = if (isValid(base58)) ProtocolHash(base58) else null
    }
}