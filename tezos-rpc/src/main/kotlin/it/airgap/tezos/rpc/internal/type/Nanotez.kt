package it.airgap.tezos.rpc.internal.type

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.rpc.internal.context.TezosRpcContext.toBigInt

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
        fun isValid(value: BigInt): Boolean = value >= BigInt.zero
    }
}
