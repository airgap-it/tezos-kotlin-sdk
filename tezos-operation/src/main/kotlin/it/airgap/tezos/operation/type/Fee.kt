package it.airgap.tezos.operation.type

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.toMutez
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez

private const val FEE_BASE_MUTEZ = 100U

private const val FEE_PER_GAS_UNIT_NANOTEZ = 100U
private const val FEE_PER_STORAGE_BYTE_NANOTEZ = 1000U

private const val FEE_SAFETY_MARGIN_MUTEZ = 100U

// -- Fee --

public data class Fee internal constructor(
    public val value: Mutez,
    internal val limits: FeeOperationLimits,
    internal val aggregatedFrom: Int = 1,
) {
    internal constructor(
        value: BigInt,
        limits: FeeOperationLimits,
    ) : this(Mutez(value), limits)

    public val gasLimit: String
        get() = limits.gas.toString()

    public val storageLimit: String
        get() = limits.storage.toString()

    internal operator fun plus(other: Fee): Fee =
        Fee(
            value + other.value,
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

internal fun Fee(value: BigInt, limits: FeeOperationLimits, aggregatedFrom: Int): Fee =
    Fee(Mutez(value), limits, aggregatedFrom)

public fun Fee(operationSize: Int, limits: FeeOperationLimits): Fee {
    val gasFee = Nanotez(FEE_PER_GAS_UNIT_NANOTEZ).toMutez() * limits.gas
    val storageFee = Nanotez(FEE_PER_STORAGE_BYTE_NANOTEZ).toMutez() * operationSize
    val total = Mutez(FEE_BASE_MUTEZ) + gasFee + storageFee + Mutez(FEE_SAFETY_MARGIN_MUTEZ)

    return Fee(total, limits)
}
