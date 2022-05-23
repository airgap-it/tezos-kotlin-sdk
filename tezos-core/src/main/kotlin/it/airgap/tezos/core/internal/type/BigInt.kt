package it.airgap.tezos.core.internal.type

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.utils.toBigInt
import it.airgap.tezos.core.type.HexString
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

// For easy multiplatform conversion
@InternalTezosSdkApi
public interface BigInt : Number<BigInt> {

    override fun Byte.toSelf(): BigInt = toBigInt()
    override fun Short.toSelf(): BigInt = toBigInt()
    override fun Int.toSelf(): BigInt = toBigInt()
    override fun Long.toSelf(): BigInt = toBigInt()

    public companion object {
        public val zero: BigInt
            get() = valueOf(0)

        public fun valueOf(value: Byte): BigInt = valueOf(value.toLong())
        public fun valueOf(value: UByte): BigInt = valueOf(value.toLong())
        public fun valueOf(value: Short): BigInt = valueOf(value.toLong())
        public fun valueOf(value: UShort): BigInt = valueOf(value.toLong())
        public fun valueOf(value: Int): BigInt = valueOf(value.toLong())
        public fun valueOf(value: UInt): BigInt = valueOf(value.toLong())
        public fun valueOf(value: Long): BigInt = JvmBigInt(BigInteger.valueOf(value))
        public fun valueOf(value: ULong): BigInt = valueOf(value.toLong())

        public fun valueOf(value: ByteArray): BigInt = JvmBigInt(BigInteger(value))
        public fun valueOf(value: HexString): BigInt = valueOf(value.asString(withPrefix = false), 16)
        public fun valueOf(value: String, radix: Int = 10): BigInt = JvmBigInt(BigInteger(value, radix))

        public fun max(a: BigInt, b: BigInt): BigInt = if (a >= b) a else b
        public fun min(a: BigInt, b: BigInt): BigInt = if (a <= b) a else b
    }
}

internal class JvmBigInt(private val value: BigInteger) : BigInt {

    // -- logical operations --

    infix fun and(other: JvmBigInt): BigInt = this and other.value
    infix fun and(other: BigInteger): BigInt = JvmBigInt(value and other)
    override fun and(other: BigInt): BigInt =
        if (other is JvmBigInt) this and other
        else this and BigInteger(other.toString(10), 10)

    infix fun or(other: JvmBigInt): BigInt = this or other.value
    infix fun or(other: BigInteger): BigInt = JvmBigInt(value or other)
    override fun or(other: BigInt): BigInt =
        if (other is JvmBigInt) this or other
        else this or BigInteger(other.toString(10), 10)

    infix fun xor(other: JvmBigInt): BigInt = this xor other.value
    infix fun xor(other: BigInteger): BigInt = JvmBigInt(value xor other)
    override fun xor(other: BigInt): BigInt =
        if (other is JvmBigInt) this xor other
        else this xor BigInteger(other.toString(10), 10)

    override fun not(): BigInt = JvmBigInt(value.not())

    override fun shr(n: Int): BigInt = JvmBigInt(value shr n)
    override fun shl(n: Int): BigInt = JvmBigInt(value shl n)

    // -- arithmetic operations --

    operator fun plus(other: JvmBigInt): JvmBigInt = this + other.value
    operator fun plus(other: BigInteger): JvmBigInt = JvmBigInt(value + other)
    override fun plus(other: BigInt): BigInt =
        if (other is JvmBigInt) this + other
        else this + BigInteger(other.toString(10), 10)


    operator fun minus(other: JvmBigInt): JvmBigInt = this - other.value
    operator fun minus(other: BigInteger): JvmBigInt = JvmBigInt(value - other)
    override fun minus(other: BigInt): BigInt =
        if (other is JvmBigInt) this - other
        else this - BigInteger(other.toString(10), 10)


    operator fun times(other: JvmBigInt): JvmBigInt = this * other.value
    operator fun times(other: BigInteger): JvmBigInt = JvmBigInt(value * other)
    override fun times(other: BigInt): BigInt =
        if (other is JvmBigInt) this * other
        else this * BigInteger(other.toString(10), 10)


    operator fun div(other: JvmBigInt): JvmBigInt = this / other.value
    operator fun div(other: BigInteger): JvmBigInt = JvmBigInt(value / other)
    override fun div(other: BigInt): BigInt =
        if (other is JvmBigInt) this / other
        else this / BigInteger(other.toString(10), 10)

    fun div(other: JvmBigInt, roundingMode: Number.RoundingMode): BigInt = div(other.value, roundingMode)
    fun div(other: BigInteger, roundingMode: Number.RoundingMode): BigInt {
        val decimal = BigDecimal(value)
        val otherDecimal = BigDecimal(other)

        val decimalRoundingMode = when (roundingMode) {
            Number.RoundingMode.Up -> RoundingMode.UP
            Number.RoundingMode.Down -> RoundingMode.DOWN
            Number.RoundingMode.Ceiling -> RoundingMode.CEILING
            Number.RoundingMode.Floor -> RoundingMode.FLOOR
        }

        val result = decimal.divide(otherDecimal, decimalRoundingMode)
        return JvmBigInt(result.toBigInteger())
    }
    override fun div(other: BigInt, roundingMode: Number.RoundingMode): BigInt =
        if (other is JvmBigInt) div(other, roundingMode)
        else  div(BigInteger(other.toString(10), 10), roundingMode)


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