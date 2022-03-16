package it.airgap.tezos.core.type.encoded

/* zet1(69) */

@JvmInline
public value class SaplingAddress(override val base58: String) : Encoded, MetaEncoded<SaplingAddress> {

    override val kind: MetaEncoded.Kind<SaplingAddress>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid Sapling address." }
    }

    override fun toMetaEncoded(): MetaEncoded<*> = this
    override fun toEncoded(): Encoded = this

    public companion object : MetaEncoded.Kind<SaplingAddress> {
        override val base58Prefix: String = "zet1"
        override val base58Bytes: ByteArray = byteArrayOf(18, 71, 40, (223).toByte())
        override val base58Length: Int = 69

        override val bytesLength: Int = 43

        override fun createValueOrNull(base58: String): SaplingAddress? = if (isValid(base58)) SaplingAddress(base58) else null
    }
}