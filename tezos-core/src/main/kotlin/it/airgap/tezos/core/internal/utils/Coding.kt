package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.HexString

// -- String <-> ByteArray

@InternalTezosSdkApi
public fun encodeStringToBytes(value: String): ByteArray = value.toByteArray(charset = Charsets.UTF_8)
@InternalTezosSdkApi
public fun decodeConsumingStringFromBytes(bytes: MutableList<Byte>, size: Int): String = bytes.consumeAt(0 until size).toByteArray().decodeToString()

@InternalTezosSdkApi
public fun encodeHexStringToBytes(value: HexString): ByteArray = value.toByteArray()
@InternalTezosSdkApi
public fun decodeConsumingHexStringFromBytes(bytes: MutableList<Byte>, size: Int): HexString = bytes.consumeAt(0 until size).toHexString()

// -- Number <-> ByteArray

@InternalTezosSdkApi
public fun encodeUInt8ToBytes(value: UByte): ByteArray = byteArrayOf(value.toByte())
@InternalTezosSdkApi
public fun decodeConsumingUInt8FromBytes(bytes: MutableList<Byte>): UByte? = bytes.consumeAt(0)?.toUByte()

@InternalTezosSdkApi
public fun encodeUInt16ToBytes(value: UShort): ByteArray = BigInt.valueOf(value.toLong()).toByteArray().asInt16Encoded()
@InternalTezosSdkApi
public fun decodeConsumingUInt16FromBytes(bytes: MutableList<Byte>): UShort = BigInt.valueOf(bytes.consumeAt(0 until 2).toByteArray()).toShortExact().toUShort()

@InternalTezosSdkApi
public fun encodeInt32ToBytes(value: Int): ByteArray = BigInt.valueOf(value).toByteArray().asInt32Encoded()
@InternalTezosSdkApi
public fun decodeConsumingInt32FromBytes(bytes: MutableList<Byte>): Int = BigInt.valueOf(bytes.consumeAt(0 until 4).toByteArray()).toIntExact()

@InternalTezosSdkApi
public fun encodeInt64ToBytes(value: Long): ByteArray = BigInt.valueOf(value).toByteArray().asInt64Encoded()
@InternalTezosSdkApi
public fun decodeConsumingInt64FromBytes(bytes: MutableList<Byte>): Long = BigInt.valueOf(bytes.consumeAt(0 until 8).toByteArray()).toLongExact()

// -- Boolean <-> ByteArray --

@InternalTezosSdkApi
public fun encodeBooleanToBytes(value: Boolean): ByteArray = if (value) byteArrayOf((255).toByte()) else byteArrayOf(0)
@InternalTezosSdkApi
public fun decodeConsumingBooleanFromBytes(bytes: MutableList<Byte>): Boolean? = when (bytes.consumeAt(0)?.toUByte()) {
    (255).toUByte() -> true
    (0).toUByte() -> false
    else -> null
}

// -- List <-> ByteArray --

@InternalTezosSdkApi
public fun <T> encodeListToBytes(list: List<T>, encoder: (T) -> ByteArray): ByteArray = list.fold(byteArrayOf()) { acc, t -> acc + encoder(t) }
@InternalTezosSdkApi
public tailrec fun <T> decodeConsumingListFromBytes(value: MutableList<Byte>, decoded: List<T> = emptyList(), decoder: (MutableList<Byte>) -> T): List<T> {
    if (value.isEmpty()) return decoded
    val item = decoder(value)

    return decodeConsumingListFromBytes(value, decoded + item, decoder)
}