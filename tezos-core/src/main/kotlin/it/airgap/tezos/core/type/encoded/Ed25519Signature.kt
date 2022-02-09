package it.airgap.tezos.core.type.encoded

/* edsig(99) */

@JvmInline
public value class Ed25519Signature(override val base58: String) : SignatureEncoded<Ed25519Signature> {

    override val kind: SignatureEncoded.Kind<Ed25519Signature>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid Ed25519 signature." }
    }

    public companion object : SignatureEncoded.Kind<Ed25519Signature> {
        override val base58Prefix: String = "edsig"
        override val base58Bytes: ByteArray = byteArrayOf(9, (245).toByte(), (205).toByte(), (134).toByte(), 18)
        override val base58Length: Int = 99

        override val bytesLength: Int = 64

        override fun createValueOrNull(base58: String): Ed25519Signature? = if (isValid(base58)) Ed25519Signature(base58) else null
    }
}