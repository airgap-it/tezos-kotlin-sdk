package it.airgap.tezos.core.type.tez

/**
 * Micro tez (mutez) representation.
 *
 * @property value
 */
@JvmInline
public value class Mutez(public val value: Long) {
    public constructor(value: Byte) : this(value.toLong())
    public constructor(value: Short) : this(value.toLong())
    public constructor(value: Int) : this(value.toLong())

    init {
        require(isValid(value)) { "Invalid mutez value." }
    }

    public companion object {
        /**
         * Checks if [value] is a valid [Mutez] value.
         */
        public fun isValid(value: String): Boolean = value.matches(Regex("^[0-9]+$"))

        /**
         * Checks if [value] is a valid [Mutez] value.
         */
        public fun isValid(value: Long): Boolean =
            // Mutez (micro-Tez) are internally represented by 64-bit signed integers.
            // These are restricted to prevent creating a negative amount of mutez. (https://tezos.gitlab.io/michelson-reference/#type-mutez)
            value >= 0
    }
}

/**
 * Creates [Mutez] from [String][value].
 */
public fun Mutez(value: String): Mutez {
    require(Mutez.isValid(value)) { "Invalid mutez value." }
    return Mutez(value.toLong())
}
