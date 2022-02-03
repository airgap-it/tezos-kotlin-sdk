package it.airgap.tezos.core.type.encoded

/* Co(52) */

@JvmInline
public value class ContextHash(override val base58: String) : Encoded<ContextHash> {

    override val kind: Encoded.Kind<ContextHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid context hash." }
    }

    public companion object : Encoded.Kind<ContextHash> {
        override val base58Prefix: String = "Co"
        override val base58Bytes: ByteArray = byteArrayOf(79, (199).toByte())
        override val base58Length: Int = 52

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): ContextHash? = if (isValid(base58)) ContextHash(base58) else null
    }
}