package it.airgap.tezos.operation

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.zarith.ZarithNatural
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

    val availableGasLimitPerBlock = BigInt.max(limits.perBlock.gas - totalFee._gasLimit, BigInt.zero)
    val requiresEstimation = contents.size - totalFee._dividedInto
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
            BigInt.valueOf(fee.int),
            BigInt.valueOf(gasLimit.int),
            BigInt.valueOf(storageLimit.int),
        )
        else -> Fee.zero
    }

private fun OperationContent.applyLimits(limits: FeeOperationLimits): OperationContent =
    when (this) {
        is OperationContent.Manager -> applyLimits(limits)
        else -> this
    }

private fun OperationContent.Manager.applyLimits(limits: FeeOperationLimits): OperationContent.Manager =
    when (this) {
        is OperationContent.Reveal -> copy(
            fee = ZarithNatural(0U),
            gasLimit = ZarithNatural(limits.gas.toString()),
            storageLimit = ZarithNatural(limits.storage.toString())
        )
        is OperationContent.Transaction -> copy(
            fee = ZarithNatural(0U),
            gasLimit = ZarithNatural(limits.gas.toString()),
            storageLimit = ZarithNatural(limits.storage.toString())
        )
        is OperationContent.Origination -> copy(
            fee = ZarithNatural(0U),
            gasLimit = ZarithNatural(limits.gas.toString()),
            storageLimit = ZarithNatural(limits.storage.toString())
        )
        is OperationContent.Delegation -> copy(
            fee = ZarithNatural(0U),
            gasLimit = ZarithNatural(limits.gas.toString()),
            storageLimit = ZarithNatural(limits.storage.toString())
        )
        is OperationContent.RegisterGlobalConstant -> copy(
            fee = ZarithNatural(0U),
            gasLimit = ZarithNatural(limits.gas.toString()),
            storageLimit = ZarithNatural(limits.storage.toString())
        )
        is OperationContent.SetDepositsLimit -> copy(
            fee = ZarithNatural(0U),
            gasLimit = ZarithNatural(limits.gas.toString()),
            storageLimit = ZarithNatural(limits.storage.toString())
        )
    }

