package it.airgap.tezos.operation.type

import it.airgap.tezos.core.internal.type.BigInt

// -- Fee --

public data class Fee internal constructor(
    internal val _value: BigInt,
    internal val _gasLimit: BigInt,
    internal val _storageLimit: BigInt,
    internal val _dividedInto: Int = 1,
) {
    public val value: String
        get() = _value.toString(10)

    public val gasLimit: String
        get() = _gasLimit.toString(10)

    public val storageLimit: String
        get() = _storageLimit.toString(10)

    internal operator fun plus(other: Fee): Fee =
        Fee(
            _value + other._value,
            _gasLimit + other._gasLimit,
            _storageLimit + other._storageLimit,
            _dividedInto + other._dividedInto,
        )

    public companion object {
        internal val zero: Fee
            get() = Fee(BigInt.zero, BigInt.zero, BigInt.zero, 0)
    }
}
