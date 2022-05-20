package it.airgap.tezos.core.type.number

@JvmInline
public value class TezosInteger(public val int: String) {

    public constructor(value: Byte) : this(value.toString())
    public constructor(value: Short) : this(value.toString())
    public constructor(value: Int) : this(value.toString())
    public constructor(value: Long) : this(value.toString())

    init {
        require(isValid(int)) { "Invalid Tezos integer." }
    }

    public fun toByte(): Byte = int.toByte()
    public fun toShort(): Short = int.toShort()
    public fun toInt(): Int = int.toInt()
    public fun toLong(): Long = int.toLong()

    public companion object {
        public fun isValid(value: String): Boolean = value.matches(Regex("^-?[0-9]+$"))
    }
}