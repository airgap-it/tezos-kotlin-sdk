package it.airgap.tezos.core.type.encoded

/* sppk(55) */

@JvmInline
public value class Secp256K1PublicKey(override val base58: String) : PublicKeyEncoded, MetaPublicKeyEncoded<Secp256K1PublicKey> {

    override val kind: MetaPublicKeyEncoded.Kind<Secp256K1PublicKey>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid secp256k1 public key." }
    }

    override fun toMetaEncoded(): MetaPublicKeyEncoded<*> = this
    override fun toEncoded(): PublicKeyEncoded = this

    public companion object : MetaPublicKeyEncoded.Kind<Secp256K1PublicKey> {
        override val base58Prefix: String = "sppk"
        override val base58Bytes: ByteArray = byteArrayOf(3, (254).toByte(), (226).toByte(), 86)
        override val base58Length: Int = 55

        override val bytesLength: Int = 33

        override fun createValueOrNull(base58: String): Secp256K1PublicKey? = if (isValid(base58)) Secp256K1PublicKey(base58) else null
    }
}