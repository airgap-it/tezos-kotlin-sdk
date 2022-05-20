package it.airgap.tezos.core.type.encoded

/* tz2(36) */

@JvmInline
public value class Secp256K1PublicKeyHash(override val base58: String) : PublicKeyHash, MetaPublicKeyHash<Secp256K1PublicKeyHash, Secp256K1PublicKeyHash> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 public key hash." }
    }

    override val kind: MetaPublicKeyHash.Kind<Secp256K1PublicKeyHash, Secp256K1PublicKeyHash>
        get() = Companion

    override val meta: MetaPublicKeyHash<*, *>
        get() = this

    override val encoded: Secp256K1PublicKeyHash
        get() = this

    public companion object : MetaPublicKeyHash.Kind<Secp256K1PublicKeyHash, Secp256K1PublicKeyHash> {
        override val base58Prefix: String = "tz2"
        override val base58Bytes: ByteArray = byteArrayOf(6, (161).toByte(), (161).toByte())
        override val base58Length: Int = 36

        override val bytesLength: Int = 20

        override fun createValueOrNull(base58: String): Secp256K1PublicKeyHash? = if (isValid(base58)) Secp256K1PublicKeyHash(base58) else null
    }
}