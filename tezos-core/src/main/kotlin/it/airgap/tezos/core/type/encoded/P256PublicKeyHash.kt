package it.airgap.tezos.core.type.encoded

/* tz3(36) */

@JvmInline
public value class P256PublicKeyHash(override val base58: String) : PublicKeyHash, MetaPublicKeyHash<P256PublicKeyHash, P256PublicKeyHash> {

    init {
        require(isValid(base58)) { "Invalid P256 public key hash." }
    }

    override val kind: MetaPublicKeyHash.Kind<P256PublicKeyHash, P256PublicKeyHash>
        get() = Companion

    override val meta: MetaPublicKeyHash<*, *>
        get() = this

    override val encoded: P256PublicKeyHash
        get() = this

    public companion object : MetaPublicKeyHash.Kind<P256PublicKeyHash, P256PublicKeyHash> {
        override val base58Prefix: String = "tz3"
        override val base58Bytes: ByteArray = byteArrayOf(6, (161).toByte(), (164).toByte())
        override val base58Length: Int = 36

        override val bytesLength: Int = 20

        override fun createValueOrNull(base58: String): P256PublicKeyHash? = if (isValid(base58)) P256PublicKeyHash(base58) else null
    }
}