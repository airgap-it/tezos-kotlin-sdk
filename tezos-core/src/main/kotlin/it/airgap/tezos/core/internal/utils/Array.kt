package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface ArrayUtilsContext {
    public fun ByteArray.asInt8Encoded(fillValue: UByte = 0U): ByteArray {
        require(size <= 1) { "ByteArray does not hold Int8 value." }
        return padStart(targetSize = 1, fillValue)
    }

    public fun ByteArray.asInt16Encoded(fillValue: UByte = 0U): ByteArray {
        require(size <= 2) { "ByteArray does not hold Int16 value." }
        return padStart(targetSize = 2, fillValue)
    }

    public fun ByteArray.asInt32Encoded(fillValue: UByte = 0U): ByteArray {
        require(size <= 4) { "ByteArray does not hold Int32 value." }
        return padStart(targetSize = 4, fillValue)
    }

    public fun ByteArray.asInt64Encoded(fillValue: UByte = 0U): ByteArray {
        require(size <= 8) { "ByteArray does not hold Int64 value." }
        return padStart(targetSize = 8, fillValue)
    }

    public fun ByteArray.padStart(targetSize: Int, fillValue: UByte = 0U): ByteArray =
        if (size >= targetSize) this
        else ByteArray(targetSize) { fillValue.toByte() }.also {
            for (i in indices) {
                it[it.size - size + i] = this[i]
            }
        }

    public fun ByteArray.padEnd(targetSize: Int, fillValue: UByte = 0U): ByteArray =
        if (size >= targetSize) this
        else this + ByteArray(targetSize - size) { fillValue.toByte() }

    public fun ByteArray.splitAt(
        firstInclusive: Boolean = false,
        calculateIndex: (ByteArray) -> Int,
    ): Pair<ByteArray, ByteArray> {
        val index = calculateIndex(this)
        return splitAt(index, firstInclusive)
    }

    public fun ByteArray.splitAt(
        index: Int,
        firstInclusive: Boolean = false,
    ): Pair<ByteArray, ByteArray> {
        val splitIndex = if (firstInclusive) minOf(index + 1, size) else index

        val first = sliceArray(0 until splitIndex)
        val second = sliceArray(splitIndex until size)

        return Pair(first, second)
    }

    public fun ByteArray.startsWith(bytes: ByteArray): Boolean =
        if (size < bytes.size) false
        else sliceArray(bytes.indices).contentEquals(bytes)
}