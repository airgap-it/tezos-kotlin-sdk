package it.airgap.tezos.core.type.tez

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.type.Number
import it.airgap.tezos.core.internal.utils.toBigInt

@JvmInline
public value class Tez(public val bigInt: BigInt) : Number<Tez> {

    public val value: String
        get() = bigInt.toString(10)

    public constructor(value: UByte) : this(value.toBigInt())
    public constructor(value: UShort) : this(value.toBigInt())
    public constructor(value: UInt) : this(value.toBigInt())
    public constructor(value: ULong) : this(value.toBigInt())

    override fun Byte.toSelf(): Tez = Tez(toBigInt())
    override fun Short.toSelf(): Tez = Tez(toBigInt())
    override fun Int.toSelf(): Tez = Tez(toBigInt())
    override fun Long.toSelf(): Tez = Tez(toBigInt())

    // -- logical operations --

    public infix fun and(other: BigInt): Tez = Tez(bigInt and other)
    override fun and(other: Tez): Tez = Tez(bigInt and other.bigInt)

    public infix fun or(other: BigInt): Tez = Tez(bigInt or other)
    override fun or(other: Tez): Tez = Tez(bigInt or other.bigInt)

    public infix fun xor(other: BigInt): Tez = Tez(bigInt xor other)
    override fun xor(other: Tez): Tez = Tez(bigInt xor other.bigInt)

    override fun not(): Tez = Tez(bigInt.not())

    override fun shr(n: Int): Tez = Tez(bigInt shr n)
    override fun shl(n: Int): Tez = Tez(bigInt shl n)

    // -- arithmetic operations --

    public operator fun plus(other: BigInt): Tez = Tez(bigInt + other)
    override fun plus(other: Tez): Tez = Tez(bigInt + other.bigInt)

    public operator fun minus(other: BigInt): Tez = Tez(bigInt - other)
    override fun minus(other: Tez): Tez = Tez(bigInt - other.bigInt)

    public operator fun times(other: BigInt): Tez = Tez(bigInt * other)
    override fun times(other: Tez): Tez = Tez(bigInt * other.bigInt)

    public operator fun div(other: BigInt): Tez = Tez(bigInt / other)
    override fun div(other: Tez): Tez = Tez(bigInt / other.bigInt)

    public fun div(other: BigInt, roundingMode: Number.RoundingMode): Tez = Tez(bigInt.div(other, roundingMode))
    override fun div(other: Tez, roundingMode: Number.RoundingMode): Tez = Tez(bigInt.div(other.bigInt, roundingMode))

    public operator fun rem(other: BigInt): Tez = Tez(bigInt % other)
    override fun rem(other: Tez): Tez = Tez(bigInt % other.bigInt)

    override fun abs(): Tez = Tez(bigInt.abs())

    // -- converters --

    override fun toByte(): Byte = bigInt.toByte()
    override fun toByteExact(): Byte = bigInt.toByteExact()
    override fun toShort(): Short = bigInt.toShort()
    override fun toShortExact(): Short = bigInt.toShortExact()
    override fun toInt(): Int = bigInt.toInt()
    override fun toIntExact(): Int = bigInt.toIntExact()
    override fun toLong(): Long = bigInt.toLong()
    override fun toLongExact(): Long = bigInt.toLongExact()
    override fun toByteArray(): ByteArray = bigInt.toByteArray()
    override fun toString(radix: Int): String = bigInt.toString(radix)

    // -- Comparable --

    override fun compareTo(other: Tez): Int = bigInt.compareTo(other.bigInt)

    public companion object {
        public fun isValid(value: String): Boolean = value.matches(Regex("^[0-9]+$"))
    }
}

public fun Tez(value: String): Tez {
    require(Tez.isValid(value)) { "Invalid tez value." }
    return Tez(BigInt.valueOf(value))
}