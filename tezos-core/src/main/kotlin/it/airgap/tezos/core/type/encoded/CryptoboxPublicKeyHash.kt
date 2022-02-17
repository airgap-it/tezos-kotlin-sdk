package it.airgap.tezos.core.type.encoded

/* id(30) */

@JvmInline
public value class CryptoboxPublicKeyHash(override val base58: String) : Encoded<CryptoboxPublicKeyHash> {

    override val kind: Encoded.Kind<CryptoboxPublicKeyHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid Cryptobox key hash." }
    }

    public companion object : Encoded.Kind<CryptoboxPublicKeyHash> {
        override val base58Prefix: String = "id"
        override val base58Bytes: ByteArray = byteArrayOf((153).toByte(), (103).toByte())
        override val base58Length: Int = 30

        override val bytesLength: Int = 16

        override fun createValueOrNull(base58: String): CryptoboxPublicKeyHash? = if (isValid(base58)) CryptoboxPublicKeyHash(base58) else null
    }
}