package it.airgap.tezos.core.type.encoded

/* o(51) */

@JvmInline
public value class OperationHash(override val base58: String) : Encoded<OperationHash> {

    override val kind: Encoded.Kind<OperationHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid operation hash." }
    }

    public companion object : Encoded.Kind<OperationHash> {
        override val base58Prefix: String = "o"
        override val base58Bytes: ByteArray = byteArrayOf(5, 116)
        override val base58Length: Int = 51

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): OperationHash? = if (isValid(base58)) OperationHash(base58) else null
    }
}