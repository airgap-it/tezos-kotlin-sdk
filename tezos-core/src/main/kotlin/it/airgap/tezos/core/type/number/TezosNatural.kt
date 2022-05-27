package it.airgap.tezos.core.type.number

@JvmInline
public value class TezosNatural(public val nat: String) {

    public constructor(value: UByte) : this(value.toString())
    public constructor(value: UShort) : this(value.toString())
    public constructor(value: UInt) : this(value.toString())
    public constructor(value: ULong) : this(value.toString())

    init {
        require(isValid(nat)) { "Invalid Tezos natural number." }
    }

    public fun toUByte(): UByte = nat.toUByte()
    public fun toUShort(): UShort = nat.toUShort()
    public fun toUInt(): UInt = nat.toUInt()
    public fun toULong(): ULong = nat.toULong()

    public companion object {
        public fun isValid(value: String): Boolean = value.matches(Regex("^[0-9]+$"))
    }
}