package it.airgap.tezos.core.type.encoded

/* tz2(36) */

@JvmInline
public value class Secp256K1PublicKeyHash(override val base58: String) : ImplicitAddress<Secp256K1PublicKeyHash> {

    override val kind: Encoded.Kind<Secp256K1PublicKeyHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid secp256k1 public key hash." }
    }

    public companion object : ImplicitAddress.Kind<Secp256K1PublicKeyHash> {
        override val base58Prefix: String = "tz2"
        override val base58Bytes: ByteArray = byteArrayOf(6, (161).toByte(), (161).toByte())
        override val base58Length: Int = 36

        override val bytesLength: Int = 20

        override fun createValueOrNull(base58: String): Secp256K1PublicKeyHash? = if (isValid(base58)) Secp256K1PublicKeyHash(base58) else null
    }
}