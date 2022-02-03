package it.airgap.tezos.core.type.encoded

/* r(51) */

@JvmInline
public value class OperationMetadataHash(override val base58: String) : Encoded<OperationMetadataHash> {

    override val kind: Encoded.Kind<OperationMetadataHash>
        get() = Companion

    init {
        require(isValid(base58)) { "Invalid operation metadata hash." }
    }

    public companion object : Encoded.Kind<OperationMetadataHash> {
        override val base58Prefix: String = "r"
        override val base58Bytes: ByteArray = byteArrayOf(5, (183).toByte())
        override val base58Length: Int = 51

        override val bytesLength: Int = 32

        override fun createValueOrNull(base58: String): OperationMetadataHash? = if (isValid(base58)) OperationMetadataHash(base58) else null
    }
}