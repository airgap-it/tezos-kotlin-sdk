package it.airgap.tezos.core.internal.type

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface Number<Self : Number<Self>> : Comparable<Self> {

    @InternalTezosSdkApi
    public fun Byte.toSelf(): Self

    @InternalTezosSdkApi
    public fun Short.toSelf(): Self

    @InternalTezosSdkApi
    public fun Int.toSelf(): Self

    @InternalTezosSdkApi
    public fun Long.toSelf(): Self

    // -- logical operations --

    public infix fun and(other: Byte): Self = this and other.toSelf()
    public infix fun and(other: Short): Self = this and other.toSelf()
    public infix fun and(other: Int): Self = this and other.toSelf()
    public infix fun and(other: Long): Self = this and other.toSelf()

    @InternalTezosSdkApi
    public infix fun and(other: Self): Self

    public infix fun or(other: Byte): Self = this or other.toSelf()
    public infix fun or(other: Short): Self = this or other.toSelf()
    public infix fun or(other: Int): Self = this or other.toSelf()
    public infix fun or(other: Long): Self = this or other.toSelf()

    @InternalTezosSdkApi
    public infix fun or(other: Self): Self

    public infix fun xor(other: Byte): Self = this xor other.toSelf()
    public infix fun xor(other: Short): Self = this xor other.toSelf()
    public infix fun xor(other: Int): Self = this xor other.toSelf()
    public infix fun xor(other: Long): Self = this xor other.toSelf()

    @InternalTezosSdkApi
    public infix fun xor(other: Self): Self

    public fun not(): Self

    public infix fun shr(n: Int): Self
    public infix fun shl(n: Int): Self

    // -- arithmetic operations --

    public operator fun plus(other: Byte): Self = this + other.toSelf()
    public operator fun plus(other: Short): Self = this + other.toSelf()
    public operator fun plus(other: Int): Self = this + other.toSelf()
    public operator fun plus(other: Long): Self = this + other.toSelf()

    @InternalTezosSdkApi
    public operator fun plus(other: Self): Self

    public operator fun minus(other: Byte): Self = this - other.toSelf()
    public operator fun minus(other: Short): Self = this - other.toSelf()
    public operator fun minus(other: Int): Self = this - other.toSelf()
    public operator fun minus(other: Long): Self = this - other.toSelf()

    @InternalTezosSdkApi
    public operator fun minus(other: Self): Self

    public operator fun times(other: Byte): Self = this * other.toSelf()
    public operator fun times(other: Short): Self = this * other.toSelf()
    public operator fun times(other: Int): Self = this * other.toSelf()
    public operator fun times(other: Long): Self = this * other.toSelf()

    @InternalTezosSdkApi
    public operator fun times(other: Self): Self

    public operator fun div(other: Byte): Self = this / other.toSelf()
    public operator fun div(other: Short): Self = this / other.toSelf()
    public operator fun div(other: Int): Self = this / other.toSelf()
    public operator fun div(other: Long): Self = this / other.toSelf()

    @InternalTezosSdkApi
    public operator fun div(other: Self): Self

    public fun div(other: Byte, roundingMode: RoundingMode): Self = this.div(other.toSelf(), roundingMode)
    public fun div(other: Short, roundingMode: RoundingMode): Self = this.div(other.toSelf(), roundingMode)
    public fun div(other: Int, roundingMode: RoundingMode): Self = this.div(other.toSelf(), roundingMode)
    public fun div(other: Long, roundingMode: RoundingMode): Self = this.div(other.toSelf(), roundingMode)

    @InternalTezosSdkApi
    public fun div(other: Self, roundingMode: RoundingMode): Self

    public operator fun rem(other: Byte): Self = this % other.toSelf()
    public operator fun rem(other: Short): Self = this % other.toSelf()
    public operator fun rem(other: Int): Self = this % other.toSelf()
    public operator fun rem(other: Long): Self = this % other.toSelf()

    @InternalTezosSdkApi
    public operator fun rem(other: Self): Self

    public fun abs(): Self

    // -- equality --

    override fun equals(other: Any?): Boolean

    // -- converters --

    public fun toByte(): Byte
    public fun toUByte(): UByte = toByte().toUByte()
    public fun toByteExact(): Byte
    public fun toUByteExact(): UByte = toByteExact().toUByte()

    public fun toShort(): Short
    public fun toUShort(): UShort = toShort().toUShort()
    public fun toShortExact(): Short
    public fun toUShortExact(): UShort = toShortExact().toUShort()

    public fun toInt(): Int
    public fun toUInt(): UInt = toInt().toUInt()
    public fun toIntExact(): Int
    public fun toUIntExact(): UInt = toIntExact().toUInt()

    public fun toLong(): Long
    public fun toULong(): ULong = toLong().toULong()
    public fun toLongExact(): Long
    public fun toULongExact(): ULong = toLongExact().toULong()

    public fun toByteArray(): ByteArray

    override fun toString(): String
    public fun toString(radix: Int): String

    public enum class RoundingMode {
        Up,
        Down,
        Ceiling,
        Floor;

        internal fun oneOf(vararg modes: RoundingMode): Boolean = modes.any { this == it }
    }
}