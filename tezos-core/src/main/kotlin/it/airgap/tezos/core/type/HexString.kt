package it.airgap.tezos.core.type

import it.airgap.tezos.core.internal.context.TezosCoreContext.isHex
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import it.airgap.tezos.core.internal.type.BigInt

@JvmInline
public value class HexString(private val value: String) {
    init {
        require(isValid(value)) { "Invalid hex string." }
    }

    public fun length(withPrefix: Boolean = false): Int = asString(withPrefix).length

    public fun asString(withPrefix: Boolean = false): String {
        val startsWithPrefix = value.startsWith(PREFIX)

        return when {
            withPrefix && !startsWithPrefix -> "$PREFIX${value}"
            !withPrefix && startsWithPrefix -> value.removePrefix(PREFIX)
            else -> value
        }
    }

    public fun toByteArray(): ByteArray = asString().chunked(2).map { it.toInt(16).toByte() }.toByteArray()

    public fun toByte(): Byte = asString().toUByte(16).toByte()
    public fun toShort(): Short = asString().toUShort(16).toShort()
    public fun toInt(): Int = asString().toUInt(16).toInt()
    public fun toLong(): Long = asString().toULong(16).toLong()
    public fun toBigInt(): BigInt = BigInt.valueOf(asString(), 16)

    public fun slice(indices: IntRange): HexString = HexString(value.slice(indices))
    public fun slice(startIndex: Int): HexString = HexString(value.substring(startIndex))

    public companion object {
        public const val PREFIX: String = "0x"

        public fun isValid(string: String): Boolean = string.isHex()
    }
}

public fun HexString(bytes: ByteArray): HexString = bytes.toHexString()