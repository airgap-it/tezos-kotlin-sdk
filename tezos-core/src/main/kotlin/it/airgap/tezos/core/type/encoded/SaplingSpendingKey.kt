package it.airgap.tezos.core.type.encoded

/* sask(241) */

@JvmInline
public value class SaplingSpendingKey(override val base58: String) : Encoded, MetaEncoded<SaplingSpendingKey, SaplingSpendingKey> {

    init {
        require(isValid(base58)) { "Invalid Sapling spending key." }
    }

    override val kind: MetaEncoded.Kind<SaplingSpendingKey, SaplingSpendingKey>
        get() = Companion

    override val meta: MetaEncoded<*, *>
        get() = this

    override val encoded: SaplingSpendingKey
        get() = this

    public companion object : MetaEncoded.Kind<SaplingSpendingKey, SaplingSpendingKey> {
        override val base58Prefix: String = "sask"
        override val base58Bytes: ByteArray = byteArrayOf(11, (237).toByte(), 20, 92)
        override val base58Length: Int = 241

        override val bytesLength: Int = 169

        override fun createValueOrNull(base58: String): SaplingSpendingKey? = if (isValid(base58)) SaplingSpendingKey(base58) else null
    }
}