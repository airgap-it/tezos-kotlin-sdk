package it.airgap.tezos.core.type.zarith

@JvmInline
public value class ZarithNatural(public val int: String) {

    public constructor(value: UByte) : this(value.toString())
    public constructor(value: UShort) : this(value.toString())
    public constructor(value: UInt) : this(value.toString())
    public constructor(value: ULong) : this(value.toString())

    init {
        require(isValid(int)) { "Invalid Zarith natural number." }
    }

    public fun toUByte(): UByte = int.toUByte()
    public fun toUShort(): UShort = int.toUShort()
    public fun toUInt(): UInt = int.toUInt()
    public fun toULong(): ULong = int.toULong()

    public companion object {
        public fun isValid(value: String): Boolean = value.matches(Regex("^[0-9]+$"))
    }
}