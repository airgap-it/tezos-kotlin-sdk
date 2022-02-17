package it.airgap.tezos.core.type.encoded

/* spesk(88) */

@JvmInline
public value class Secp256K1EncryptedSecretKey(override val base58: String) : EncryptedSecretKeyEncoded<Secp256K1EncryptedSecretKey> {

    override val kind: EncryptedSecretKeyEncoded.Kind<Secp256K1EncryptedSecretKey>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid secp256k1 encrypted secret key." }
    }

    public companion object : EncryptedSecretKeyEncoded.Kind<Secp256K1EncryptedSecretKey> {
        override val base58Prefix: String = "spesk"
        override val base58Bytes: ByteArray = byteArrayOf(9, (237).toByte(), (241).toByte(), (174).toByte(), (150).toByte())
        override val base58Length: Int = 88

        override val bytesLength: Int = 56

        override fun createValueOrNull(base58: String): Secp256K1EncryptedSecretKey? = if (isValid(base58)) Secp256K1EncryptedSecretKey(base58) else null
    }
}