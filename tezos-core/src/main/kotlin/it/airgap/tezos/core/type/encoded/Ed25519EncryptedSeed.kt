package it.airgap.tezos.core.type.encoded

/* edesk(88) */

@JvmInline
public value class Ed25519EncryptedSeed(override val base58: String) : EncryptedSeedEncoded, MetaEncryptedSeedEncoded<Ed25519EncryptedSeed> {

    override val kind: MetaEncryptedSeedEncoded.Kind<Ed25519EncryptedSeed>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid Ed25519 encrypted seed." }
    }

    override fun toMetaEncoded(): MetaEncryptedSeedEncoded<*> = this
    override fun toEncoded(): EncryptedSeedEncoded = this

    public companion object : MetaEncryptedSeedEncoded.Kind<Ed25519EncryptedSeed> {
        override val base58Prefix: String = "edesk"
        override val base58Bytes: ByteArray = byteArrayOf(7, 90, 60, (179).toByte(), 41)
        override val base58Length: Int = 88

        override val bytesLength: Int = 56

        override fun createValueOrNull(base58: String): Ed25519EncryptedSeed? = if (isValid(base58)) Ed25519EncryptedSeed(base58) else null
    }
}