package it.airgap.tezos.core.type.encoded

/* spsig1(99) */

@JvmInline
public value class Secp256K1Signature(override val base58: String) : SignatureEncoded, MetaSignatureEncoded<Secp256K1Signature> {

    override val kind: MetaSignatureEncoded.Kind<Secp256K1Signature>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid secp256k1 signature." }
    }

    override fun toMetaEncoded(): MetaSignatureEncoded<*> = this
    override fun toEncoded(): SignatureEncoded = this

    public companion object : MetaSignatureEncoded.Kind<Secp256K1Signature> {
        override val base58Prefix: String = "spsig1"
        override val base58Bytes: ByteArray = byteArrayOf(13, 115.toByte(), 101, 19, 63)
        override val base58Length: Int = 99

        override val bytesLength: Int = 64

        override fun createValueOrNull(base58: String): Secp256K1Signature? = if (isValid(base58)) Secp256K1Signature(base58) else null
    }
}