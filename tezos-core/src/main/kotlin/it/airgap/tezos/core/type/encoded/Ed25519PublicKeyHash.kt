package it.airgap.tezos.core.type.encoded

/* tz1(36) */

@JvmInline
public value class Ed25519PublicKeyHash(override val base58: String) : PublicKeyHashEncoded, MetaPublicKeyHashEncoded<Ed25519PublicKeyHash> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 public key hash." }
    }

    override val kind: MetaPublicKeyHashEncoded.Kind<Ed25519PublicKeyHash>
        get() = Companion

    override val meta: MetaPublicKeyHashEncoded<*>
        get() = this

    override val encoded: PublicKeyHashEncoded
        get() = this

    public companion object : MetaPublicKeyHashEncoded.Kind<Ed25519PublicKeyHash> {
        override val base58Prefix: String = "tz1"
        override val base58Bytes: ByteArray = byteArrayOf(6, (161).toByte(), (159).toByte())
        override val base58Length: Int = 36

        override val bytesLength: Int = 20

        override fun createValueOrNull(base58: String): Ed25519PublicKeyHash? = if (isValid(base58)) Ed25519PublicKeyHash(base58) else null
    }
}