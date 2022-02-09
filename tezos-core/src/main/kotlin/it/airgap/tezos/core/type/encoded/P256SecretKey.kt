package it.airgap.tezos.core.type.encoded

/* p2sk(54) */

@JvmInline
public value class P256SecretKey(override val base58: String) : SecretKeyEncoded<P256SecretKey> {

    override val kind: SecretKeyEncoded.Kind<P256SecretKey>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid P256 secret key." }
    }

    public companion object : SecretKeyEncoded.Kind<P256SecretKey> {
        override val base58Prefix: String = "p2sk"
        override val base58Bytes: ByteArray = byteArrayOf(16, 81, (238).toByte(), (189).toByte())
        override val base58Length: Int = 54

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): P256SecretKey? = if (isValid(base58)) P256SecretKey(base58) else null
    }
}