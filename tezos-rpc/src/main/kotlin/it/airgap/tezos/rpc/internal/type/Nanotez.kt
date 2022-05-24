package it.airgap.tezos.rpc.internal.type

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.toBigInt

@JvmInline
internal value class Nanotez(val bigInt: BigInt) {
    constructor(value: Byte) : this(value.toBigInt())
    constructor(value: Short) : this(value.toBigInt())
    constructor(value: Int) : this(value.toBigInt())
    constructor(value: Long) : this(value.toBigInt())

    init {
        require(isValid(bigInt)) { "Invalid nanotez value." }
    }

    companion object {
        fun isValid(value: String): Boolean = value.matches(Regex("^[0-9]+$"))
        fun isValid(value: BigInt): Boolean = value >= BigInt.zero
    }
}

internal fun Nanotez(value: String): Nanotez {
    require(Nanotez.isValid(value)) { "Invalid nanotez value." }
    return Nanotez(BigInt.valueOf(value))
}