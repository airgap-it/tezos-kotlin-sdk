package it.airgap.tezos.core.type.encoded

/* spesk(88) */

@JvmInline
public value class Secp256K1EncryptedSecretKey(override val base58: String) : EncryptedSecretKey, MetaEncryptedSecretKey<Secp256K1EncryptedSecretKey, Secp256K1EncryptedSecretKey> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 encrypted secret key." }
    }

    override val kind: MetaEncryptedSecretKey.Kind<Secp256K1EncryptedSecretKey, Secp256K1EncryptedSecretKey>
        get() = Companion

    override val meta: MetaEncryptedSecretKey<*, *>
        get() = this

    override val encoded: Secp256K1EncryptedSecretKey
        get() = this

    public companion object : MetaEncryptedSecretKey.Kind<Secp256K1EncryptedSecretKey, Secp256K1EncryptedSecretKey> {
        override val base58Prefix: String = "spesk"
        override val base58Bytes: ByteArray = byteArrayOf(9, (237).toByte(), (241).toByte(), (174).toByte(), (150).toByte())
        override val base58Length: Int = 88

        override val bytesLength: Int = 56

        override fun createValueOrNull(base58: String): Secp256K1EncryptedSecretKey? = if (isValid(base58)) Secp256K1EncryptedSecretKey(base58) else null
    }
}