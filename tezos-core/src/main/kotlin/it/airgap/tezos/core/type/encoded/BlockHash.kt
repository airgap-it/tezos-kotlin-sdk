package it.airgap.tezos.core.type.encoded

/* B(51) */

@JvmInline
public value class BlockHash(override val base58: String) : Encoded<BlockHash> {

    override val kind: Encoded.Kind<BlockHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid block hash." }
    }

    public companion object : Encoded.Kind<BlockHash> {
        override val base58Prefix: String = "B"
        override val base58Bytes: ByteArray = byteArrayOf(1, 52)
        override val base58Length: Int = 51

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): BlockHash? = if (isValid(base58)) BlockHash(base58) else null
    }
}