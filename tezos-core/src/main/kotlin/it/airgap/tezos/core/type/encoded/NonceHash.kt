package it.airgap.tezos.core.type.encoded

/* nce(53) */

@JvmInline
public value class NonceHash(override val base58: String) : Encoded, MetaEncoded<NonceHash, NonceHash> {

    init {
        require(isValid(base58)) { "Invalid nonce hash." }
    }

    override val kind: MetaEncoded.Kind<NonceHash, NonceHash>
        get() = Companion

    override val meta: MetaEncoded<*, *>
        get() = this

    override val encoded: NonceHash
        get() = this

    public companion object : MetaEncoded.Kind<NonceHash, NonceHash> {
        override val base58Prefix: String = "nce"
        override val base58Bytes: ByteArray = byteArrayOf(69, (220).toByte(), (169).toByte())
        override val base58Length: Int = 53

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): NonceHash? = if (isValid(base58)) NonceHash(base58) else null
    }
}