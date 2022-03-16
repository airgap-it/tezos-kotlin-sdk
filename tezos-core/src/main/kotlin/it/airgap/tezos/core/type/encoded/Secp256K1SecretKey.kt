package it.airgap.tezos.core.type.encoded

/* spsk(54) */

@JvmInline
public value class Secp256K1SecretKey(override val base58: String) : SecretKeyEncoded, MetaSecretKeyEncoded<Secp256K1SecretKey> {

    override val kind: MetaSecretKeyEncoded.Kind<Secp256K1SecretKey>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid secp256k1 secret key." }
    }

    override fun toMetaEncoded(): MetaSecretKeyEncoded<*> = this
    override fun toEncoded(): SecretKeyEncoded = this

    public companion object : MetaSecretKeyEncoded.Kind<Secp256K1SecretKey> {
        override val base58Prefix: String = "spsk"
        override val base58Bytes: ByteArray = byteArrayOf(17, (162).toByte(), (224).toByte(), (201).toByte())
        override val base58Length: Int = 54

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): Secp256K1SecretKey? = if (isValid(base58)) Secp256K1SecretKey(base58) else null
    }
}