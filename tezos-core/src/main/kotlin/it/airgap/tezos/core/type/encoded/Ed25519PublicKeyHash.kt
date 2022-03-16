package it.airgap.tezos.core.type.encoded

/* tz1(36) */

@JvmInline
public value class Ed25519PublicKeyHash(override val base58: String) : ImplicitAddress, MetaImplicitAddress<Ed25519PublicKeyHash> {

    override val kind: MetaImplicitAddress.Kind<Ed25519PublicKeyHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid Ed25519 public key hash." }
    }

    override fun toMetaEncoded(): MetaImplicitAddress<*> = this
    override fun toEncoded(): ImplicitAddress = this

    public companion object : MetaImplicitAddress.Kind<Ed25519PublicKeyHash> {
        override val base58Prefix: String = "tz1"
        override val base58Bytes: ByteArray = byteArrayOf(6, (161).toByte(), (159).toByte())
        override val base58Length: Int = 36

        override val bytesLength: Int = 20

        override fun createValueOrNull(base58: String): Ed25519PublicKeyHash? = if (isValid(base58)) Ed25519PublicKeyHash(base58) else null
    }
}