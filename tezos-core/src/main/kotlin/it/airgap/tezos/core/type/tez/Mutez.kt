package it.airgap.tezos.core.type.tez

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.type.Number
import it.airgap.tezos.core.internal.utils.toBigInt

@JvmInline
public value class Mutez(public val bigInt: BigInt) : Number<Mutez> {

    public val value: String
        get() = bigInt.toString(10)

    public constructor(value: UByte) : this(value.toBigInt())
    public constructor(value: UShort) : this(value.toBigInt())
    public constructor(value: UInt) : this(value.toBigInt())
    public constructor(value: ULong) : this(value.toBigInt())

    override fun Byte.toSelf(): Mutez = Mutez(toBigInt())
    override fun Short.toSelf(): Mutez = Mutez(toBigInt())
    override fun Int.toSelf(): Mutez = Mutez(toBigInt())
    override fun Long.toSelf(): Mutez = Mutez(toBigInt())

    // -- logical operations --

    public infix fun and(other: BigInt): Mutez = Mutez(bigInt and other)
    override fun and(other: Mutez): Mutez = Mutez(bigInt and other.bigInt)

    public infix fun or(other: BigInt): Mutez = Mutez(bigInt or other)
    override fun or(other: Mutez): Mutez = Mutez(bigInt or other.bigInt)

    public infix fun xor(other: BigInt): Mutez = Mutez(bigInt xor other)
    override fun xor(other: Mutez): Mutez = Mutez(bigInt xor other.bigInt)

    override fun not(): Mutez = Mutez(bigInt.not())

    override fun shr(n: Int): Mutez = Mutez(bigInt shr n)
    override fun shl(n: Int): Mutez = Mutez(bigInt shl n)

    // -- arithmetic operations --

    public operator fun plus(other: BigInt): Mutez = Mutez(bigInt + other)
    override fun plus(other: Mutez): Mutez = Mutez(bigInt + other.bigInt)

    public operator fun minus(other: BigInt): Mutez = Mutez(bigInt - other)
    override fun minus(other: Mutez): Mutez = Mutez(bigInt - other.bigInt)

    public operator fun times(other: BigInt): Mutez = Mutez(bigInt * other)
    override fun times(other: Mutez): Mutez = Mutez(bigInt * other.bigInt)

    public operator fun div(other: BigInt): Mutez = Mutez(bigInt / other)
    override fun div(other: Mutez): Mutez = Mutez(bigInt / other.bigInt)

    public fun div(other: BigInt, roundingMode: Number.RoundingMode): Mutez = Mutez(bigInt.div(other, roundingMode))
    override fun div(other: Mutez, roundingMode: Number.RoundingMode): Mutez = Mutez(bigInt.div(other.bigInt, roundingMode))

    public operator fun rem(other: BigInt): Mutez = Mutez(bigInt % other)
    override fun rem(other: Mutez): Mutez = Mutez(bigInt % other.bigInt)

    override fun abs(): Mutez = Mutez(bigInt.abs())

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

    override fun compareTo(other: Mutez): Int = bigInt.compareTo(other.bigInt)

    public companion object {
        public fun isValid(value: String): Boolean = value.matches(Regex("^[0-9]+$"))
    }
}

public fun Mutez(value: String): Mutez {
    require(Mutez.isValid(value)) { "Invalid mutez value." }
    return Mutez(BigInt.valueOf(value))
}