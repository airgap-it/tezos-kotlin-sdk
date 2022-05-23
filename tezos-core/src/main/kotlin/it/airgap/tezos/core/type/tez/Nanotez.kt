package it.airgap.tezos.core.type.tez

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.type.Number
import it.airgap.tezos.core.internal.utils.toBigInt

@JvmInline
public value class Nanotez internal constructor(internal val bigInt: BigInt) : Number<Nanotez> {

    public val value: String
        get() = bigInt.toString(10)

    public constructor(value: UByte) : this(value.toBigInt())
    public constructor(value: UShort) : this(value.toBigInt())
    public constructor(value: UInt) : this(value.toBigInt())
    public constructor(value: ULong) : this(value.toBigInt())

    @InternalTezosSdkApi
    override fun Byte.toSelf(): Nanotez = Nanotez(toBigInt())

    @InternalTezosSdkApi
    override fun Short.toSelf(): Nanotez = Nanotez(toBigInt())

    @InternalTezosSdkApi
    override fun Int.toSelf(): Nanotez = Nanotez(toBigInt())

    @InternalTezosSdkApi
    override fun Long.toSelf(): Nanotez = Nanotez(toBigInt())

    // -- logical operations --

    @InternalTezosSdkApi
    public infix fun and(other: BigInt): Nanotez = Nanotez(bigInt and other)
    override fun and(other: Nanotez): Nanotez = Nanotez(bigInt and other.bigInt)

    @InternalTezosSdkApi
    public infix fun or(other: BigInt): Nanotez = Nanotez(bigInt or other)
    override fun or(other: Nanotez): Nanotez = Nanotez(bigInt or other.bigInt)

    @InternalTezosSdkApi
    public infix fun xor(other: BigInt): Nanotez = Nanotez(bigInt xor other)
    override fun xor(other: Nanotez): Nanotez = Nanotez(bigInt xor other.bigInt)

    override fun not(): Nanotez = Nanotez(bigInt.not())

    override fun shr(n: Int): Nanotez = Nanotez(bigInt shr n)
    override fun shl(n: Int): Nanotez = Nanotez(bigInt shl n)

    // -- arithmetic operations --

    @InternalTezosSdkApi
    public operator fun plus(other: BigInt): Nanotez = Nanotez(bigInt + other)
    override fun plus(other: Nanotez): Nanotez = Nanotez(bigInt + other.bigInt)

    @InternalTezosSdkApi
    public operator fun minus(other: BigInt): Nanotez = Nanotez(bigInt - other)
    override fun minus(other: Nanotez): Nanotez = Nanotez(bigInt - other.bigInt)

    @InternalTezosSdkApi
    public operator fun times(other: BigInt): Nanotez = Nanotez(bigInt * other)
    override fun times(other: Nanotez): Nanotez = Nanotez(bigInt * other.bigInt)

    @InternalTezosSdkApi
    public operator fun div(other: BigInt): Nanotez = Nanotez(bigInt / other)
    override fun div(other: Nanotez): Nanotez = Nanotez(bigInt / other.bigInt)

    @InternalTezosSdkApi
    public fun div(other: BigInt, roundingMode: Number.RoundingMode): Nanotez = Nanotez(bigInt.div(other, roundingMode))
    override fun div(other: Nanotez, roundingMode: Number.RoundingMode): Nanotez = Nanotez(bigInt.div(other.bigInt, roundingMode))

    @InternalTezosSdkApi
    public operator fun rem(other: BigInt): Nanotez = Nanotez(bigInt % other)
    override fun rem(other: Nanotez): Nanotez = Nanotez(bigInt % other.bigInt)

    override fun abs(): Nanotez = Nanotez(bigInt.abs())

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

    override fun compareTo(other: Nanotez): Int = bigInt.compareTo(other.bigInt)

    public companion object {
        public fun isValid(value: String): Boolean = value.matches(Regex("^[0-9]+$"))
    }
}

public fun Nanotez(value: String): Nanotez {
    require(Nanotez.isValid(value)) { "Invalid nanotez value." }
    return Nanotez(BigInt.valueOf(value))
}