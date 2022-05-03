package it.airgap.tezos.operation

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.toBigInt
import it.airgap.tezos.core.internal.utils.toZarithNatural
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.operation.type.Limits
import it.airgap.tezos.operation.type.OperationLimits

// -- Operation --

public val Operation.fee: Mutez
    get() = contents.fold(Mutez(BigInt.zero)) { acc, content -> acc + content.fee }

public val Operation.limits: OperationLimits
    get() = contents.fold(OperationLimits.zero) { acc, content -> acc + content.limits }

public fun Operation.applyLimits(limits: Limits): Operation {
    val maxLimits = maxLimits(limits)
    val updatedContents = contents.map { it.apply(limits = maxLimits) }

    return when (this) {
        is Operation.Unsigned -> copy(contents = updatedContents)
        is Operation.Signed -> copy(contents = updatedContents)
    }
}

private fun Operation.maxLimits(limits: Limits): OperationLimits {
    val availableGasLimitPerBlock = BigInt.max(limits.perBlock.gas - this.limits.gas, BigInt.zero)
    val requiresEstimation = contents.filterNot { it.hasFee }.size
    val maxGasLimitPerOperation = if (requiresEstimation > 0) availableGasLimitPerBlock / BigInt.valueOf(requiresEstimation) else BigInt.zero

    return OperationLimits(
        BigInt.min(limits.perOperation.gas, maxGasLimitPerOperation),
        limits.perOperation.storage,
    )
}

// -- OperationContent --

public val OperationContent.fee: Mutez
    get() = when (this) {
        is OperationContent.Manager -> fee
        else -> Mutez(BigInt.zero)
    }

public val OperationContent.hasFee: Boolean
    get() = fee != Mutez(BigInt.zero)

public val OperationContent.limits: OperationLimits
    get() = when (this) {
        is OperationContent.Manager -> OperationLimits(
            gasLimit.toBigInt(),
            storageLimit.toBigInt(),
        )
        else -> OperationLimits.zero
    }

public fun OperationContent.apply(fee: Mutez = Mutez(BigInt.zero), limits: OperationLimits = OperationLimits.zero): OperationContent =
    when (this) {
        is OperationContent.Reveal -> copy(
            fee = fee,
            gasLimit = limits.gas.toZarithNatural(),
            storageLimit = limits.storage.toZarithNatural()
        )
        is OperationContent.Transaction -> copy(
            fee = fee,
            gasLimit = limits.gas.toZarithNatural(),
            storageLimit = limits.storage.toZarithNatural(),
        )
        is OperationContent.Origination -> copy(
            fee = fee,
            gasLimit = limits.gas.toZarithNatural(),
            storageLimit = limits.storage.toZarithNatural(),
        )
        is OperationContent.Delegation -> copy(
            fee = fee,
            gasLimit = limits.gas.toZarithNatural(),
            storageLimit = limits.storage.toZarithNatural(),
        )
        is OperationContent.RegisterGlobalConstant -> copy(
            fee = fee,
            gasLimit = limits.gas.toZarithNatural(),
            storageLimit = limits.storage.toZarithNatural(),
        )
        is OperationContent.SetDepositsLimit -> copy(
            fee = fee,
            gasLimit = limits.gas.toZarithNatural(),
            storageLimit = limits.storage.toZarithNatural(),
        )
        else -> this
    }

