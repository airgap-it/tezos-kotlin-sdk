package it.airgap.tezos.core.type.encoded

/* sask(241) */

@JvmInline
public value class SaplingSpendingKey(override val base58: String) : Encoded, MetaEncoded<SaplingSpendingKey> {

    override val kind: MetaEncoded.Kind<SaplingSpendingKey>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid Sapling spending key." }
    }

    override fun toMetaEncoded(): MetaEncoded<*> = this
    override fun toEncoded(): Encoded = this

    public companion object : MetaEncoded.Kind<SaplingSpendingKey> {
        override val base58Prefix: String = "sask"
        override val base58Bytes: ByteArray = byteArrayOf(11, (237).toByte(), 20, 92)
        override val base58Length: Int = 241

        override val bytesLength: Int = 169

        override fun createValueOrNull(base58: String): SaplingSpendingKey? = if (isValid(base58)) SaplingSpendingKey(base58) else null
    }
}