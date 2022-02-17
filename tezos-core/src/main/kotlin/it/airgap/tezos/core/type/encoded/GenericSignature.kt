package it.airgap.tezos.core.type.encoded

/* sig(96) */

@JvmInline
public value class GenericSignature(override val base58: String) : SignatureEncoded<GenericSignature> {

    override val kind: SignatureEncoded.Kind<GenericSignature>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid generic signature." }
    }

    public companion object : SignatureEncoded.Kind<GenericSignature> {
        override val base58Prefix: String = "sig"
        override val base58Bytes: ByteArray = byteArrayOf(4, (130).toByte(), 43)
        override val base58Length: Int = 96

        override val bytesLength: Int = 64

        override fun createValueOrNull(base58: String): GenericSignature? = if (isValid(base58)) GenericSignature(base58) else null
    }
}