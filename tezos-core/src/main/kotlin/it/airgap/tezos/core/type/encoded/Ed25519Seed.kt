package it.airgap.tezos.core.type.encoded

/* edsk(54) */

@JvmInline
public value class Ed25519Seed(override val base58: String) : SeedEncoded<Ed25519Seed> {

    override val kind: Encoded.Kind<Ed25519Seed>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid Ed25519 seed." }
    }

    public companion object : SeedEncoded.Kind<Ed25519Seed> {
        override val base58Prefix: String = "edsk"
        override val base58Bytes: ByteArray = byteArrayOf(13, 15, 58, 7)
        override val base58Length: Int = 54

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): Ed25519Seed? = if (isValid(base58)) Ed25519Seed(base58) else null
    }
}