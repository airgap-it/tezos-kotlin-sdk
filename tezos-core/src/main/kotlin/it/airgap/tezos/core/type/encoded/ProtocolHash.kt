package it.airgap.tezos.core.type.encoded

/* P(51) */

@JvmInline
public value class ProtocolHash(override val base58: String) : Encoded<ProtocolHash> {

    override val kind: Encoded.Kind<ProtocolHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid protocol hash." }
    }

    public companion object : Encoded.Kind<ProtocolHash> {
        override val base58Prefix: String = "P"
        override val base58Bytes: ByteArray = byteArrayOf(2, (170).toByte())
        override val base58Length: Int = 51

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): ProtocolHash? = if (isValid(base58)) ProtocolHash(base58) else null
    }
}