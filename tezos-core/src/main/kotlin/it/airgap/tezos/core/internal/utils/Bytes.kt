package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.type.HexString

private val hexRegex: Regex = Regex("^(${HexString.PREFIX})?([0-9a-fA-F]{2})+$")

public fun String.isHex(): Boolean = this.matches(hexRegex)

public fun String.asHexStringOrNull(): HexString? = if (isHex()) HexString(this) else null
public fun String.asHexString(): HexString = asHexStringOrNull() ?: failWithInvalidHexString(this)

public fun ByteArray.toHexString(): HexString =
    joinToString("") { it.toHexString().asString() }.asHexString()

public fun List<Byte>.toHexString(): HexString =
    joinToString("") { it.toHexString().asString() }.asHexString()

public fun Byte.toHexString(): HexString =
    toUByte().toString(16).padStartEven('0').asHexString()

public fun Short.toHexString(): HexString =
    toUShort().toString(16).padStartEven('0').asHexString()

public fun Int.toHexString(): HexString =
    toUInt().toString(16).padStartEven('0').asHexString()

public fun Long.toHexString(): HexString =
    toULong().toString(16).padStartEven('0').asHexString()

private fun failWithInvalidHexString(string: String): Nothing =
    throw IllegalArgumentException("$string is not a valid hex string")
