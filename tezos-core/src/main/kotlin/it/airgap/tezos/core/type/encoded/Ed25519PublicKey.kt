package it.airgap.tezos.core.type.encoded

/* edpk(54) */

@JvmInline
public value class Ed25519PublicKey(override val base58: String) : PublicKeyEncoded, MetaPublicKeyEncoded<Ed25519PublicKey> {

    override val kind: MetaPublicKeyEncoded.Kind<Ed25519PublicKey>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid Ed25519 public key." }
    }

    override fun toMetaEncoded(): MetaPublicKeyEncoded<*> = this
    override fun toEncoded(): PublicKeyEncoded = this

    public companion object : MetaPublicKeyEncoded.Kind<Ed25519PublicKey> {
        override val base58Prefix: String = "edpk"
        override val base58Bytes: ByteArray = byteArrayOf(13, 15, 37, (217).toByte())
        override val base58Length: Int = 54

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): Ed25519PublicKey? = if (isValid(base58)) Ed25519PublicKey(base58) else null
    }
}