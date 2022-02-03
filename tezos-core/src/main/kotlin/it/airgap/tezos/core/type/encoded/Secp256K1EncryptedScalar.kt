package it.airgap.tezos.core.type.encoded

/* seesk(93) */

@JvmInline
public value class Secp256K1EncryptedScalar(override val base58: String) : EncryptedScalarEncoded<Secp256K1EncryptedScalar> {

    override val kind: Encoded.Kind<Secp256K1EncryptedScalar>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid secp256k1 encrypted scalar." }
    }

    public companion object : EncryptedScalarEncoded.Kind<Secp256K1EncryptedScalar> {
        override val base58Prefix: String = "seesk"
        override val base58Bytes: ByteArray = byteArrayOf(1, (131).toByte(), 36, 86, (248).toByte())
        override val base58Length: Int = 93

        override val bytesLength: Int = 60

        override fun createValueOrNull(base58: String): Secp256K1EncryptedScalar? = if (isValid(base58)) Secp256K1EncryptedScalar(base58) else null
    }
}