package it.airgap.tezos.core.type

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.TezosCoreContext.isHex
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import it.airgap.tezos.core.internal.type.BigInt

/**
 * Hexadecimal [String] type-safe representation.
 *
 * @property value
 */
@JvmInline
public value class HexString(private val value: String) {
    init {
        require(isValid(value)) { "Invalid hex string." }
    }

    /**
     * Calculates the length of the underlying hexadecimal string [prefix included or not][withPrefix].
     */
    public fun length(withPrefix: Boolean = false): Int = asString(withPrefix).length

    /**
     * Returns underlying [String][value] [with or without the `0x` prefix][withPrefix].
     */
    public fun asString(withPrefix: Boolean = false): String {
        val startsWithPrefix = value.startsWith(PREFIX)

        return when {
            withPrefix && !startsWithPrefix -> "$PREFIX${value}"
            !withPrefix && startsWithPrefix -> value.removePrefix(PREFIX)
            else -> value
        }
    }

    /**
     * Converts this [HexString] to [bytes][ByteArray].
     */
    public fun toByteArray(): ByteArray = asString().chunked(2).map { it.toInt(16).toByte() }.toByteArray()

    /**
     * Converts this [HexString] to [Byte].
     */
    public fun toByte(): Byte = asString().toUByte(16).toByte()

    /**
     * Converts this [HexString] to [Short].
     */
    public fun toShort(): Short = asString().toUShort(16).toShort()

    /**
     * Converts this [HexString] to [Int].
     */
    public fun toInt(): Int = asString().toUInt(16).toInt()

    /**
     * Converts this [HexString] to [Long].
     */
    public fun toLong(): Long = asString().toULong(16).toLong()

    @InternalTezosSdkApi
    public fun toBigInt(): BigInt = BigInt.valueOf(asString(), 16)

    public companion object {
        public const val PREFIX: String = "0x"

        public fun isValid(string: String): Boolean = string.isHex()
    }
}

/**
 * Creates [HexString] from [ByteArray][bytes].
 */
public fun HexString(bytes: ByteArray): HexString = bytes.toHexString()