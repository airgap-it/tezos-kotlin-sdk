package it.airgap.tezos.operation

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.toBigInt
import it.airgap.tezos.core.internal.utils.toZarithNatural
import it.airgap.tezos.operation.type.Fee
import it.airgap.tezos.operation.type.FeeLimits
import it.airgap.tezos.operation.type.FeeOperationLimits

// -- Operation --

public val Operation.fee: Fee
    get() = contents.fold(Fee.zero) { acc, content -> acc + content.fee }

public fun Operation.applyLimits(limits: FeeLimits): Operation {
    val maxLimitsPerOperation = maxLimitsPerOperation(limits)
    val updatedContents = contents.map { it.applyLimits(maxLimitsPerOperation) }

    return when (this) {
        is Operation.Unsigned -> copy(contents = updatedContents)
        is Operation.Signed -> copy(contents = updatedContents)
    }
}

private fun Operation.maxLimitsPerOperation(limits: FeeLimits): FeeOperationLimits {
    val totalFee = fee

    val availableGasLimitPerBlock = BigInt.max(limits.perBlock.gas - totalFee.limits.gas, BigInt.zero)
    val requiresEstimation = contents.size - totalFee.aggregatedFrom
    val maxGasLimitPerOperation = if (requiresEstimation > 0) availableGasLimitPerBlock / BigInt.valueOf(requiresEstimation) else BigInt.zero

    return FeeOperationLimits(
        BigInt.min(limits.perOperation.gas, maxGasLimitPerOperation),
        limits.perOperation.storage,
    )
}

// -- OperationContent --

public val OperationContent.fee: Fee
    get() = when (this) {
        is OperationContent.Manager -> Fee(
            fee,
            FeeOperationLimits(
                gasLimit.toBigInt(),
                storageLimit.toBigInt(),
            ),
        )
        else -> Fee.zero
    }

public val OperationContent.hasFee: Boolean
    get() = fee != Fee.zero

public fun OperationContent.applyFee(fee: Fee): OperationContent =
    when (this) {
        is OperationContent.Manager -> applyFee(fee)
        else -> this
    }

private fun OperationContent.applyLimits(limits: FeeOperationLimits): OperationContent =
    when (this) {
        is OperationContent.Manager -> applyFee(Fee.zeroWithLimits(limits))
        else -> this
    }

private fun OperationContent.Manager.applyFee(fee: Fee): OperationContent.Manager =
    when (this) {
        is OperationContent.Reveal -> copy(
            fee = fee.value,
            gasLimit = fee.limits.gas.toZarithNatural(),
            storageLimit = fee.limits.storage.toZarithNatural()
        )
        is OperationContent.Transaction -> copy(
            fee = fee.value,
            gasLimit = fee.limits.gas.toZarithNatural(),
            storageLimit = fee.limits.storage.toZarithNatural(),
        )
        is OperationContent.Origination -> copy(
            fee = fee.value,
            gasLimit = fee.limits.gas.toZarithNatural(),
            storageLimit = fee.limits.storage.toZarithNatural(),
        )
        is OperationContent.Delegation -> copy(
            fee = fee.value,
            gasLimit = fee.limits.gas.toZarithNatural(),
            storageLimit = fee.limits.storage.toZarithNatural(),
        )
        is OperationContent.RegisterGlobalConstant -> copy(
            fee = fee.value,
            gasLimit = fee.limits.gas.toZarithNatural(),
            storageLimit = fee.limits.storage.toZarithNatural(),
        )
        is OperationContent.SetDepositsLimit -> copy(
            fee = fee.value,
            gasLimit = fee.limits.gas.toZarithNatural(),
            storageLimit = fee.limits.storage.toZarithNatural(),
        )
    }

