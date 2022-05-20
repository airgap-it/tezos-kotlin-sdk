package it.airgap.tezos.core.type.encoded

/* edsk(98) */

@JvmInline
public value class Ed25519SecretKey(override val base58: String) : SecretKey, MetaSecretKey<Ed25519SecretKey, Ed25519SecretKey> {

    init {
        require(isValid(base58)) { "Invalid Ed25519 secret key." }
    }

    override val kind: MetaSecretKey.Kind<Ed25519SecretKey, Ed25519SecretKey>
        get() = Companion

    override val meta: MetaSecretKey<*, *>
        get() = this

    override val encoded: Ed25519SecretKey
        get() = this

    public companion object : MetaSecretKey.Kind<Ed25519SecretKey, Ed25519SecretKey> {
        override val base58Prefix: String = "edsk"
        override val base58Bytes: ByteArray = byteArrayOf(43, (246).toByte(), 78, 7)
        override val base58Length: Int = 98

        override val bytesLength: Int = 64

        override fun createValueOrNull(base58: String): Ed25519SecretKey? = if (isValid(base58)) Ed25519SecretKey(base58) else null
    }
}