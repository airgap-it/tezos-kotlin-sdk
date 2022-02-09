package it.airgap.tezos.core.type.encoded

/* p2pk(55) */

@JvmInline
public value class P256PublicKey(override val base58: String) : PublicKeyEncoded<P256PublicKey> {

    override val kind: PublicKeyEncoded.Kind<P256PublicKey>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid P256 public key." }
    }

    public companion object : PublicKeyEncoded.Kind<P256PublicKey> {
        override val base58Prefix: String = "p2pk"
        override val base58Bytes: ByteArray = byteArrayOf(3, (178).toByte(), (139).toByte(), 127)
        override val base58Length: Int = 55

        override val bytesLength: Int = 33

        override fun createValueOrNull(base58: String): P256PublicKey? = if (isValid(base58)) P256PublicKey(base58) else null
    }
}