package it.airgap.tezos.core.type.encoded

/* GSp(54) */

@JvmInline
public value class Secp256K1Element(override val base58: String) : Encoded, MetaEncoded<Secp256K1Element, Secp256K1Element> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 element." }
    }

    override val kind: MetaEncoded.Kind<Secp256K1Element, Secp256K1Element>
        get() = Companion

    override val meta: MetaEncoded<*, *>
        get() = this

    override val encoded: Secp256K1Element
        get() = this

    public companion object : MetaEncoded.Kind<Secp256K1Element, Secp256K1Element> {
        override val base58Prefix: String = "GSp"
        override val base58Bytes: ByteArray = byteArrayOf(5, 92, 0)
        override val base58Length: Int = 54

        override val bytesLength: Int = 33

        override fun createValueOrNull(base58: String): Secp256K1Element? = if (isValid(base58)) Secp256K1Element(base58) else null
    }
}