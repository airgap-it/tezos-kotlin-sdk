package it.airgap.tezos.core.type.encoded

/* tz3(36) */

@JvmInline
public value class P256PublicKeyHash(override val base58: String) : ImplicitAddress<P256PublicKeyHash> {

    override val kind: ImplicitAddress.Kind<P256PublicKeyHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid P256 public key hash." }
    }

    public companion object : ImplicitAddress.Kind<P256PublicKeyHash> {
        override val base58Prefix: String = "tz3"
        override val base58Bytes: ByteArray = byteArrayOf(6, (161).toByte(), (164).toByte())
        override val base58Length: Int = 36

        override val bytesLength: Int = 20

        override fun createValueOrNull(base58: String): P256PublicKeyHash? = if (isValid(base58)) P256PublicKeyHash(base58) else null
    }
}