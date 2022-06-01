package it.airgap.tezos.core.type.number

/**
 * Big integer represented as a [String].
 *
 * @property int The integer value.
 */
@JvmInline
public value class TezosInteger(public val int: String) {

    /**
     * Creates [TezosInteger] from [Byte][value].
     */
    public constructor(value: Byte) : this(value.toString())

    /**
     * Creates [TezosInteger] from [Short][value].
     */
    public constructor(value: Short) : this(value.toString())

    /**
     * Creates [TezosInteger] from [Int][value].
     */
    public constructor(value: Int) : this(value.toString())

    /**
     * Creates [TezosInteger] from [Long][value].
     */
    public constructor(value: Long) : this(value.toString())

    init {
        require(isValid(int)) { "Invalid Tezos integer." }
    }

    public fun toByte(): Byte = int.toByte()
    public fun toShort(): Short = int.toShort()
    public fun toInt(): Int = int.toInt()
    public fun toLong(): Long = int.toLong()

    public companion object {
        /**
         * Checks if [value] is a valid integer.
         */
        public fun isValid(value: String): Boolean = value.matches(Regex("^-?[0-9]+$"))
    }
}