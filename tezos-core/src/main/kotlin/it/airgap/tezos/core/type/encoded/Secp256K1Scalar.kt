package it.airgap.tezos.core.type.encoded

/* SSp(53) */

@JvmInline
public value class Secp256K1Scalar(override val base58: String) : Scalar, MetaScalar<Secp256K1Scalar, Secp256K1Scalar> {

    init {
        require(isValid(base58)) { "Invalid secp256k1 scalar." }
    }

    override val kind: MetaScalar.Kind<Secp256K1Scalar, Secp256K1Scalar>
        get() = Companion

    override val meta: MetaScalar<*, *>
        get() = this

    override val encoded: Secp256K1Scalar
        get() = this

    public companion object : MetaScalar.Kind<Secp256K1Scalar, Secp256K1Scalar> {
        override val base58Prefix: String = "SSp"
        override val base58Bytes: ByteArray = byteArrayOf(38, (248).toByte(), (136).toByte())
        override val base58Length: Int = 53

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): Secp256K1Scalar? = if (isValid(base58)) Secp256K1Scalar(base58) else null
    }
}