package it.airgap.tezos.core.type.encoded

/* SSp(53) */

@JvmInline
public value class Secp256K1Scalar(override val base58: String) : ScalarEncoded<Secp256K1Scalar> {

    override val kind: Encoded.Kind<Secp256K1Scalar>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid secp256k1 scalar." }
    }

    public companion object : ScalarEncoded.Kind<Secp256K1Scalar> {
        override val base58Prefix: String = "SSp"
        override val base58Bytes: ByteArray = byteArrayOf(38, (248).toByte(), (136).toByte())
        override val base58Length: Int = 53

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): Secp256K1Scalar? = if (isValid(base58)) Secp256K1Scalar(base58) else null
    }
}