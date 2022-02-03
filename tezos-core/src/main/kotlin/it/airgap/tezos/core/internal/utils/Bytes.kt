package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.HexString

private val hexRegex: Regex = Regex("^(${HexString.PREFIX})?([0-9a-fA-F]{2})*$")

@InternalTezosSdkApi
public fun String.isHex(): Boolean = this.matches(hexRegex)

@InternalTezosSdkApi
public fun String.asHexStringOrNull(): HexString? = if (isHex()) HexString(this) else null

@InternalTezosSdkApi
public fun String.asHexString(): HexString = asHexStringOrNull() ?: failWithInvalidHexString(this)

@InternalTezosSdkApi
public fun ByteArray.toHexString(): HexString =
    joinToString("") { it.toHexString().asString() }.asHexString()

@InternalTezosSdkApi
public fun List<Byte>.toHexString(): HexString =
    joinToString("") { it.toHexString().asString() }.asHexString()

@InternalTezosSdkApi
public fun Byte.toHexString(): HexString =
    toUByte().toString(16).padStartEven('0').asHexString()

@InternalTezosSdkApi
public fun Short.toHexString(): HexString =
    toUShort().toString(16).padStartEven('0').asHexString()

@InternalTezosSdkApi
public fun Int.toHexString(): HexString =
    toUInt().toString(16).padStartEven('0').asHexString()

@InternalTezosSdkApi
public fun Long.toHexString(): HexString =
    toULong().toString(16).padStartEven('0').asHexString()

@InternalTezosSdkApi
public fun BigInt.toHexString(): HexString =
    toHexStringOrNull() ?: failWithNegativeNumber(this)

@InternalTezosSdkApi
public fun BigInt.toHexStringOrNull(): HexString? =
    if (this >= BigInt.valueOf(0)) toString(16).padStartEven('0').asHexString()
    else null

private fun failWithInvalidHexString(string: String): Nothing =
    failWithIllegalArgument("$string is not a valid hex string")

private fun failWithNegativeNumber(number: BigInt): Nothing =
    failWithIllegalArgument("cannot create HexString from a negative number $number")
