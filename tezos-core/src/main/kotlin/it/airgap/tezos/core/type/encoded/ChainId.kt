package it.airgap.tezos.core.type.encoded

/* Net(15) */

@JvmInline
public value class ChainId(override val base58: String) : Encoded<ChainId> {

    override val kind: Encoded.Kind<ChainId>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid chain ID." }
    }

    public companion object : Encoded.Kind<ChainId> {
        override val base58Prefix: String = "Net"
        override val base58Bytes: ByteArray = byteArrayOf(87, 82, 0)
        override val base58Length: Int = 15

        override val bytesLength: Int = 4

        override fun createValueOrNull(base58: String): ChainId? = if (isValid(base58)) ChainId(base58) else null
    }
}