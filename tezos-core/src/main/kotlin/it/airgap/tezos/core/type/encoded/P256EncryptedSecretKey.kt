package it.airgap.tezos.core.type.encoded

/* p2esk(88) */

@JvmInline
public value class P256EncryptedSecretKey(override val base58: String) : EncryptedSecretKeyEncoded, MetaEncryptedSecretKeyEncoded<P256EncryptedSecretKey> {

    init {
        require(isValid(base58)) { "Invalid P256 encrypted secret key." }
    }

    override val kind: MetaEncryptedSecretKeyEncoded.Kind<P256EncryptedSecretKey>
        get() = Companion

    override val meta: MetaEncryptedSecretKeyEncoded<*>
        get() = this

    override val encoded: EncryptedSecretKeyEncoded
        get() = this

    public companion object : MetaEncryptedSecretKeyEncoded.Kind<P256EncryptedSecretKey> {
        override val base58Prefix: String = "p2esk"
        override val base58Bytes: ByteArray = byteArrayOf(9, 48, 57, 115, (171).toByte())
        override val base58Length: Int = 88

        override val bytesLength: Int = 56

        override fun createValueOrNull(base58: String): P256EncryptedSecretKey? = if (isValid(base58)) P256EncryptedSecretKey(base58) else null
    }
}