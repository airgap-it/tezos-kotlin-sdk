package it.airgap.tezos.core.internal.type

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.type.HexString
import java.math.BigInteger

// For easy multiplatform conversion
@InternalTezosSdkApi
public interface BigInt : Comparable<BigInt> {

    // -- logical operations --

    public infix fun and(other: Byte): BigInt
    public infix fun and(other: Short): BigInt
    public infix fun and(other: Int): BigInt
    public infix fun and(other: Long): BigInt
    public infix fun and(other: BigInt): BigInt

    public infix fun or(other: Byte): BigInt
    public infix fun or(other: Short): BigInt
    public infix fun or(other: Int): BigInt
    public infix fun or(other: Long): BigInt
    public infix fun or(other: BigInt): BigInt

    public infix fun xor(other: Byte): BigInt
    public infix fun xor(other: Short): BigInt
    public infix fun xor(other: Int): BigInt
    public infix fun xor(other: Long): BigInt
    public infix fun xor(other: BigInt): BigInt

    public fun not(): BigInt

    public infix fun shr(n: Int): BigInt
    public infix fun shl(n: Int): BigInt

    // -- arithmetic operations --

    public operator fun plus(other: Byte): BigInt
    public operator fun plus(other: Short): BigInt
    public operator fun plus(other: Int): BigInt
    public operator fun plus(other: Long): BigInt
    public operator fun plus(other: BigInt): BigInt

    public operator fun minus(other: Byte): BigInt
    public operator fun minus(other: Short): BigInt
    public operator fun minus(other: Int): BigInt
    public operator fun minus(other: Long): BigInt
    public operator fun minus(other: BigInt): BigInt

    public operator fun times(other: Byte): BigInt
    public operator fun times(other: Short): BigInt
    public operator fun times(other: Int): BigInt
    public operator fun times(other: Long): BigInt
    public operator fun times(other: BigInt): BigInt

    public operator fun div(other: Byte): BigInt
    public operator fun div(other: Short): BigInt
    public operator fun div(other: Int): BigInt
    public operator fun div(other: Long): BigInt
    public operator fun div(other: BigInt): BigInt

    public operator fun rem(other: Byte): BigInt
    public operator fun rem(other: Short): BigInt
    public operator fun rem(other: Int): BigInt
    public operator fun rem(other: Long): BigInt
    public operator fun rem(other: BigInt): BigInt

    public fun abs(): BigInt

    // -- equality --

    override fun equals(other: Any?): Boolean

    // -- converters --

    public fun toByte(): Byte
    public fun toByteExact(): Byte

    public fun toShort(): Short
    public fun toShortExact(): Short

    public fun toInt(): Int
    public fun toIntExact(): Int

    public fun toLong(): Long
    public fun toLongExact(): Long

    public fun toByteArray(): ByteArray

    override fun toString(): String
    public fun toString(radix: Int): String

    public companion object {
        public fun valueOf(value: Byte): BigInt = valueOf(value.toLong())
        public fun valueOf(value: Short): BigInt = valueOf(value.toLong())
        public fun valueOf(value: Int): BigInt = valueOf(value.toLong())
        public fun valueOf(value: Long): BigInt = JvmBigInt(BigInteger.valueOf(value))

        public fun valueOf(value: ByteArray): BigInt = JvmBigInt(BigInteger(value))
        public fun valueOf(value: HexString): BigInt = valueOf(value.asString(withPrefix = false), 16)
        public fun valueOf(value: String, radix: Int = 10): BigInt = JvmBigInt(BigInteger(value, radix))
    }
}

internal class JvmBigInt(private val value: BigInteger) : BigInt {

    // -- logical operations --

    override fun and(other: Byte): BigInt = this and BigInteger.valueOf(other.toLong())
    override fun and(other: Short): BigInt = this and BigInteger.valueOf(other.toLong())
    override fun and(other: Int): BigInt = this and BigInteger.valueOf(other.toLong())
    override fun and(other: Long): BigInt = this and BigInteger.valueOf(other)
    infix fun and(other: JvmBigInt): BigInt = this and other.value
    infix fun and(other: BigInteger): BigInt = JvmBigInt(value and other)
    override fun and(other: BigInt): BigInt =
        if (other is JvmBigInt) this and other
        else this and BigInteger(other.toString(10), 10)

    override fun or(other: Byte): BigInt = this or BigInteger.valueOf(other.toLong())
    override fun or(other: Short): BigInt = this or BigInteger.valueOf(other.toLong())
    override fun or(other: Int): BigInt = this or BigInteger.valueOf(other.toLong())
    override fun or(other: Long): BigInt = this or BigInteger.valueOf(other)
    infix fun or(other: JvmBigInt): BigInt = this or other.value
    infix fun or(other: BigInteger): BigInt = JvmBigInt(value or other)
    override fun or(other: BigInt): BigInt =
        if (other is JvmBigInt) this or other
        else this or BigInteger(other.toString(10), 10)

    override fun xor(other: Byte): BigInt = this xor BigInteger.valueOf(other.toLong())
    override fun xor(other: Short): BigInt = this xor BigInteger.valueOf(other.toLong())
    override fun xor(other: Int): BigInt = this xor BigInteger.valueOf(other.toLong())
    override fun xor(other: Long): BigInt = this xor BigInteger.valueOf(other)
    infix fun xor(other: JvmBigInt): BigInt = this xor other.value
    infix fun xor(other: BigInteger): BigInt = JvmBigInt(value xor other)
    override fun xor(other: BigInt): BigInt =
        if (other is JvmBigInt) this xor other
        else this xor BigInteger(other.toString(10), 10)

    override fun not(): BigInt = JvmBigInt(value.not())

    override fun shr(n: Int): BigInt = JvmBigInt(value shr n)
    override fun shl(n: Int): BigInt = JvmBigInt(value shl n)

    // -- arithmetic operations --

    override fun plus(other: Byte): BigInt = this + BigInteger.valueOf(other.toLong())
    override fun plus(other: Short): BigInt = this + BigInteger.valueOf(other.toLong())
    override fun plus(other: Int): BigInt = this + BigInteger.valueOf(other.toLong())
    override fun plus(other: Long): BigInt = this + BigInteger.valueOf(other)
    operator fun plus(other: JvmBigInt): JvmBigInt = this + other.value
    operator fun plus(other: BigInteger): JvmBigInt = JvmBigInt(value + other)
    override fun plus(other: BigInt): BigInt =
        if (other is JvmBigInt) this + other
        else this + BigInteger(other.toString(10), 10)


    override fun minus(other: Byte): BigInt = this - BigInteger.valueOf(other.toLong())
    override fun minus(other: Short): BigInt = this - BigInteger.valueOf(other.toLong())
    override fun minus(other: Int): BigInt = this - BigInteger.valueOf(other.toLong())
    override fun minus(other: Long): BigInt = this - BigInteger.valueOf(other)
    operator fun minus(other: JvmBigInt): JvmBigInt = this - other.value
    operator fun minus(other: BigInteger): JvmBigInt = JvmBigInt(value - other)
    override fun minus(other: BigInt): BigInt =
        if (other is JvmBigInt) this - other
        else this - BigInteger(other.toString(10), 10)


    override fun times(other: Byte): BigInt = this * BigInteger.valueOf(other.toLong())
    override fun times(other: Short): BigInt = this * BigInteger.valueOf(other.toLong())
    override fun times(other: Int): BigInt = this * BigInteger.valueOf(other.toLong())
    override fun times(other: Long): BigInt = this * BigInteger.valueOf(other)
    operator fun times(other: JvmBigInt): JvmBigInt = this * other.value
    operator fun times(other: BigInteger): JvmBigInt = JvmBigInt(value * other)
    override fun times(other: BigInt): BigInt =
        if (other is JvmBigInt) this * other
        else this * BigInteger(other.toString(10), 10)


    override fun div(other: Byte): BigInt = this / BigInteger.valueOf(other.toLong())
    override fun div(other: Short): BigInt = this / BigInteger.valueOf(other.toLong())
    override fun div(other: Int): BigInt = this / BigInteger.valueOf(other.toLong())
    override fun div(other: Long): BigInt = this / BigInteger.valueOf(other)
    operator fun div(other: JvmBigInt): JvmBigInt = this / other.value
    operator fun div(other: BigInteger): JvmBigInt = JvmBigInt(value / other)
    override fun div(other: BigInt): BigInt =
        if (other is JvmBigInt) this / other
        else this / BigInteger(other.toString(10), 10)


    override fun rem(other: Byte): BigInt = this % BigInteger.valueOf(other.toLong())
    override fun rem(other: Short): BigInt = this % BigInteger.valueOf(other.toLong())
    override fun rem(other: Int): BigInt = this % BigInteger.valueOf(other.toLong())
    override fun rem(other: Long): BigInt = this % BigInteger.valueOf(other)
    operator fun rem(other: JvmBigInt): JvmBigInt = this % other.value
    operator fun rem(other: BigInteger): JvmBigInt = JvmBigInt(value % other)
    override fun rem(other: BigInt): BigInt =
        if (other is JvmBigInt) this % other
        else this % BigInteger(other.toString(10), 10)

    override fun abs(): BigInt = JvmBigInt(value.abs())

    // -- equality --

    override fun equals(other: Any?): Boolean =
        when (other) {
            is JvmBigInt -> this.value == other.value
            is Byte -> this.value == BigInteger.valueOf(other.toLong())
            is Short -> this.value == BigInteger.valueOf(other.toLong())
            is Int -> this.value == BigInteger.valueOf(other.toLong())
            is Long -> this.value == BigInteger.valueOf(other)
            else -> false
        }

    override fun hashCode(): Int = value.hashCode()

    // -- converters --

    override fun toByte(): Byte = value.toByte()
    override fun toByteExact(): Byte = value.byteValueExact()

    override fun toShort(): Short = value.toShort()
    override fun toShortExact(): Short = value.shortValueExact()

    override fun toInt(): Int = value.toInt()
    override fun toIntExact(): Int = value.intValueExact()

    override fun toLong(): Long = value.toLong()
    override fun toLongExact(): Long = value.longValueExact()

    override fun toByteArray(): ByteArray = value.toByteArray()

    override fun toString(): String = value.toString()
    override fun toString(radix: Int): String = value.toString(radix)

    // -- Comparable --

    override fun compareTo(other: BigInt): Int =
        if (other is JvmBigInt) value.compareTo(other.value)
        else value.compareTo(BigInteger(other.toString(10), 10))
}