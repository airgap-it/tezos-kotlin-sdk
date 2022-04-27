package it.airgap.tezos.operation.type

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.toBigInt
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez

private const val FEE_BASE = 100U // mutez
private const val FEE_PER_GAS_UNIT = 100U // nanotez
private const val FEE_PER_STORAGE_BYTE = 1000U // nanotez
private const val FEE_SAFETY_MARGIN = 100U // mutez

// -- Fee --

public data class Fee internal constructor(
    internal val total: BigInt,
    public val limits: FeeOperationLimits,
    internal val aggregatedFrom: Int = 1,
) {
    public val value: String
        get() = total.toString(10)

    public val gasLimit: String
        get() = limits.gas.toString(10)

    public val storageLimit: String
        get() = limits.storage.toString(10)

    internal operator fun plus(other: Fee): Fee =
        Fee(
            total + other.total,
            limits + other.limits,
            aggregatedFrom + other.aggregatedFrom,
        )

    public companion object {
        internal val zero: Fee
            get() = Fee(BigInt.zero, FeeOperationLimits.zero, 0)

        internal fun zeroWithLimits(limits: FeeOperationLimits): Fee =
            Fee(BigInt.zero, limits)
    }
}

public fun Fee(operationSize: Int, limits: FeeOperationLimits): Fee {
    val gasFee = Nanotez(FEE_PER_GAS_UNIT.toInt()).toBigInt() * limits.gas
    val storageFee = Nanotez(FEE_PER_STORAGE_BYTE.toInt()).toBigInt() * operationSize
    val total = Mutez(FEE_BASE.toInt()).toBigInt() + gasFee + storageFee + Mutez(FEE_SAFETY_MARGIN.toInt()).toBigInt()

    return Fee(total, limits)
}
