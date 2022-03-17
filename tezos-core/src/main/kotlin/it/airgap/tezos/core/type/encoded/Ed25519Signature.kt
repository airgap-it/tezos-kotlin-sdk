package it.airgap.tezos.core.type.encoded

/* edsig(99) */

@JvmInline
public value class Ed25519Signature(override val base58: String) : SignatureEncoded, MetaSignatureEncoded<Ed25519Signature> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 signature." }
    }

    override val kind: MetaSignatureEncoded.Kind<Ed25519Signature>
        get() = Companion

    override val meta: MetaSignatureEncoded<*>
        get() = this

    override val encoded: SignatureEncoded
        get() = this

    public companion object : MetaSignatureEncoded.Kind<Ed25519Signature> {
        override val base58Prefix: String = "edsig"
        override val base58Bytes: ByteArray = byteArrayOf(9, (245).toByte(), (205).toByte(), (134).toByte(), 18)
        override val base58Length: Int = 99

        override val bytesLength: Int = 64

        override fun createValueOrNull(base58: String): Ed25519Signature? = if (isValid(base58)) Ed25519Signature(base58) else null
    }
}