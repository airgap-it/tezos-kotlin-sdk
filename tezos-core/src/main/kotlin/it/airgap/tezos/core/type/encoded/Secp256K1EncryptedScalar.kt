package it.airgap.tezos.core.type.encoded

/* seesk(93) */

@JvmInline
public value class Secp256K1EncryptedScalar(override val base58: String) : EncryptedScalarEncoded, MetaEncryptedScalarEncoded<Secp256K1EncryptedScalar> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 encrypted scalar." }
    }

    override val kind: MetaEncryptedScalarEncoded.Kind<Secp256K1EncryptedScalar>
        get() = Companion

    override val meta: MetaEncryptedScalarEncoded<*>
        get() = this

    override val encoded: EncryptedScalarEncoded
        get() = this

    public companion object : MetaEncryptedScalarEncoded.Kind<Secp256K1EncryptedScalar> {
        override val base58Prefix: String = "seesk"
        override val base58Bytes: ByteArray = byteArrayOf(1, (131).toByte(), 36, 86, (248).toByte())
        override val base58Length: Int = 93

        override val bytesLength: Int = 60

        override fun createValueOrNull(base58: String): Secp256K1EncryptedScalar? = if (isValid(base58)) Secp256K1EncryptedScalar(base58) else null
    }
}