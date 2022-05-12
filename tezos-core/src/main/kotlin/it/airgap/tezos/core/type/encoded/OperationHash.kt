package it.airgap.tezos.core.type.encoded

/* o(51) */

@JvmInline
public value class OperationHash(override val base58: String) : Encoded, MetaEncoded<OperationHash, OperationHash> {

    init {
        require(isValid(base58)) { "Invalid operation hash." }
    }

    override val kind: MetaEncoded.Kind<OperationHash, OperationHash>
        get() = Companion

    override val meta: MetaEncoded<*, *>
        get() = this

    override val encoded: OperationHash
        get() = this

    public companion object : MetaEncoded.Kind<OperationHash, OperationHash> {
        override val base58Prefix: String = "o"
        override val base58Bytes: ByteArray = byteArrayOf(5, 116)
        override val base58Length: Int = 51

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): OperationHash? = if (isValid(base58)) OperationHash(base58) else null
    }
}