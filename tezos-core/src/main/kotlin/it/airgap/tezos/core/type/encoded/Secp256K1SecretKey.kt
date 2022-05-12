package it.airgap.tezos.core.type.encoded

/* spsk(54) */

@JvmInline
public value class Secp256K1SecretKey(override val base58: String) : SecretKey, MetaSecretKey<Secp256K1SecretKey, Secp256K1SecretKey> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 secret key." }
    }

    override val kind: MetaSecretKey.Kind<Secp256K1SecretKey, Secp256K1SecretKey>
        get() = Companion

    override val meta: MetaSecretKey<*, *>
        get() = this

    override val encoded: Secp256K1SecretKey
        get() = this

    public companion object : MetaSecretKey.Kind<Secp256K1SecretKey, Secp256K1SecretKey> {
        override val base58Prefix: String = "spsk"
        override val base58Bytes: ByteArray = byteArrayOf(17, (162).toByte(), (224).toByte(), (201).toByte())
        override val base58Length: Int = 54

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): Secp256K1SecretKey? = if (isValid(base58)) Secp256K1SecretKey(base58) else null
    }
}