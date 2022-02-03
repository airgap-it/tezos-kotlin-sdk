package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public fun ByteArray.asInt8Encoded(): ByteArray {
    require(size <= 1) { "ByteArray does not hold Int8 value." }
    return padStart(n = 1, fillValue = 0)
}

@InternalTezosSdkApi
public fun ByteArray.asInt16Encoded(): ByteArray {
    require(size <= 2) { "ByteArray does not hold Int16 value." }
    return padStart(n = 2, fillValue = 0)
}

@InternalTezosSdkApi
public fun ByteArray.asInt32Encoded(): ByteArray {
    require(size <= 4) { "ByteArray does not hold Int32 value." }
    return padStart(n = 4, fillValue = 0)
}

@InternalTezosSdkApi
public fun ByteArray.asInt64Encoded(): ByteArray {
    require(size <= 8) { "ByteArray does not hold Int64 value." }
    return padStart(n = 8, fillValue = 0)
}

@InternalTezosSdkApi
public fun ByteArray.padStart(n: Int, fillValue: Byte = 0): ByteArray =
    if (size >= n) this
    else ByteArray(n) { fillValue }.also {
        for (i in indices) {
            it[it.size - size + i] = this[i]
        }
    }

@InternalTezosSdkApi
public fun ByteArray.splitAt(
    firstInclusive: Boolean = false,
    calculateIndex: (ByteArray) -> Int,
): Pair<ByteArray, ByteArray> {
    val index = calculateIndex(this)
    return splitAt(index, firstInclusive)
}

@InternalTezosSdkApi
public fun ByteArray.splitAt(
    index: Int,
    firstInclusive: Boolean = false,
): Pair<ByteArray, ByteArray> {
    val splitIndex = if (firstInclusive) minOf(index + 1, size) else index

    val first = sliceArray(0 until splitIndex)
    val second = sliceArray(splitIndex until size)

    return Pair(first, second)
}

@InternalTezosSdkApi
public fun ByteArray.startsWith(bytes: ByteArray): Boolean =
    sliceArray(bytes.indices).contentEquals(bytes)