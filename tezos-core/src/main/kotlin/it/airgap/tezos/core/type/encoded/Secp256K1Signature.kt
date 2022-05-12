package it.airgap.tezos.core.type.encoded

/* spsig1(99) */

@JvmInline
public value class Secp256K1Signature(override val base58: String) : Signature, MetaSignature<Secp256K1Signature, Secp256K1Signature> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 signature." }
    }

    override val kind: MetaSignature.Kind<Secp256K1Signature, Secp256K1Signature>
        get() = Companion

    override val meta: MetaSignature<*, *>
        get() = this

    override val encoded: Secp256K1Signature
        get() = this

    public companion object : MetaSignature.Kind<Secp256K1Signature, Secp256K1Signature> {
        override val base58Prefix: String = "spsig1"
        override val base58Bytes: ByteArray = byteArrayOf(13, 115.toByte(), 101, 19, 63)
        override val base58Length: Int = 99

        override val bytesLength: Int = 64

        override fun createValueOrNull(base58: String): Secp256K1Signature? = if (isValid(base58)) Secp256K1Signature(base58) else null
    }
}