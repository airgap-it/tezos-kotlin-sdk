package it.airgap.tezos.core.type.encoded

/* p2sig(98) */

@JvmInline
public value class P256Signature(override val base58: String) : SignatureEncoded, MetaSignatureEncoded<P256Signature> {

    init {
        require(isValid(base58)) { "Invalid P256 signature." }
    }

    override val kind: MetaSignatureEncoded.Kind<P256Signature>
        get() = Companion

    override val meta: MetaSignatureEncoded<*>
        get() = this

    override val encoded: SignatureEncoded
        get() = this

    public companion object : MetaSignatureEncoded.Kind<P256Signature> {
        override val base58Prefix: String = "p2sig"
        override val base58Bytes: ByteArray = byteArrayOf(54, (240).toByte(), 44, 52)
        override val base58Length: Int = 98

        override val bytesLength: Int = 64

        override fun createValueOrNull(base58: String): P256Signature? = if (isValid(base58)) P256Signature(base58) else null
    }
}