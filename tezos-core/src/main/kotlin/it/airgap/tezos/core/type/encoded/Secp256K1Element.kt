package it.airgap.tezos.core.type.encoded

/* GSp(54) */

@JvmInline
public value class Secp256K1Element(override val base58: String) : Encoded, MetaEncoded<Secp256K1Element> {

    override val kind: MetaEncoded.Kind<Secp256K1Element>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid secp256k1 element." }
    }

    override fun toMetaEncoded(): MetaEncoded<*> = this
    override fun toEncoded(): Encoded = this

    public companion object : MetaEncoded.Kind<Secp256K1Element> {
        override val base58Prefix: String = "GSp"
        override val base58Bytes: ByteArray = byteArrayOf(5, 92, 0)
        override val base58Length: Int = 54

        override val bytesLength: Int = 33

        override fun createValueOrNull(base58: String): Secp256K1Element? = if (isValid(base58)) Secp256K1Element(base58) else null
    }
}