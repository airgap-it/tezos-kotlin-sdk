package it.airgap.tezos.core.type

public sealed interface Watermark {
    public val bytes: ByteArray

    public operator fun plus(other: ByteArray): ByteArray = bytes + other

    public data class BlockHeader(public val chainId: ByteArray) : Watermark {
        override val bytes: ByteArray = byteArrayOf(1) + chainId

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is BlockHeader -> chainId.contentEquals(other.chainId) && bytes.contentEquals(other.bytes)
                else -> false
            }

        override fun hashCode(): Int = listOf(chainId, bytes).fold(0) { acc, b -> (31 * acc) + b.contentHashCode() }
    }

    public data class Endorsement(public val chainId: ByteArray) : Watermark {
        override val bytes: ByteArray = byteArrayOf(2) + chainId

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is Endorsement -> chainId.contentEquals(other.chainId) && bytes.contentEquals(other.bytes)
                else -> false
            }

        override fun hashCode(): Int = listOf(chainId, bytes).fold(0) { acc, b -> (31 * acc) + b.contentHashCode() }
    }

    public object GenericOperation : Watermark {
        override val bytes: ByteArray = byteArrayOf(3)
    }

    public data class Custom(override val bytes: ByteArray) : Watermark {
        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is Custom -> bytes.contentEquals(other.bytes)
                else -> false
            }

        override fun hashCode(): Int = bytes.contentHashCode()
    }
}