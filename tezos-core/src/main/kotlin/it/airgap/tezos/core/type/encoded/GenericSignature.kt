package it.airgap.tezos.core.type.encoded

/* sig(96) */

@JvmInline
public value class GenericSignature(override val base58: String) : Signature, MetaSignature<GenericSignature, GenericSignature> {

    init {
        require(isValid(base58)) { "Invalid generic signature." }
    }

    override val kind: MetaSignature.Kind<GenericSignature, GenericSignature>
        get() = Companion

    override val meta: MetaSignature<*, *>
        get() = this

    override val encoded: GenericSignature
        get() = this

    public companion object : MetaSignature.Kind<GenericSignature, GenericSignature> {
        override val base58Prefix: String = "sig"
        override val base58Bytes: ByteArray = byteArrayOf(4, (130).toByte(), 43)
        override val base58Length: Int = 96

        override val bytesLength: Int = 64

        override fun createValueOrNull(base58: String): GenericSignature? = if (isValid(base58)) GenericSignature(base58) else null
    }
}