package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.toBigInt
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.rpc.type.limits.OperationLimits
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcOperationMetadata
import it.airgap.tezos.rpc.type.operation.RpcOperationResult

private const val STORAGE_CONTRACT_ALLOCATION = 257U

private const val GAS_SAFETY_MARGIN = 100U
private const val STORAGE_SAFETY_MARGIN = 100U

// -- Operation --

internal val Operation.limits: OperationLimits
    get() = contents.fold(OperationLimits.zero) { acc, content -> acc + content.limits }

// -- OperationContent --

internal val OperationContent.limits: OperationLimits
    get() = when (this) {
        is OperationContent.Manager -> OperationLimits(
            gasLimit.toBigInt(),
            storageLimit.toBigInt(),
        )
        else -> OperationLimits.zero
    }

internal val RpcOperationContent.metadataLimits: OperationLimits?
    get() = when (this) {
        is RpcOperationContent.Delegation -> metadata?.limits
        is RpcOperationContent.Origination -> metadata?.limits
        is RpcOperationContent.RegisterGlobalConstant -> metadata?.limits
        is RpcOperationContent.Reveal -> metadata?.limits
        is RpcOperationContent.SetDepositsLimit -> metadata?.limits
        is RpcOperationContent.Transaction -> metadata?.limits
        else -> null
    }

internal val RpcOperationMetadata.limits: OperationLimits
    get() = internalOperationResults.orEmpty().fold(operationResult?.limits ?: OperationLimits.zero) { acc, internalResult -> acc + internalResult.result.limits }

internal val RpcOperationResult.limits: OperationLimits
    get() {
        assertApplied()
        return OperationLimits(gasLimit, storageLimit)
    }

internal fun RpcOperationResult.assertApplied() {
    errors?.let { failWithRpcErrors(it) }
}

private val RpcOperationResult.gasLimit: BigInt
    get() {
        val consumedGas = consumedGas?.let { BigInt.valueOf(it) } ?: BigInt.zero
        return consumedGas + GAS_SAFETY_MARGIN.toInt()
    }

private val RpcOperationResult.storageLimit: BigInt
    get() {
        val paidStorageSizeDiff = paidStorageSizeDiff?.let { BigInt.valueOf(it) } ?: BigInt.zero
        return paidStorageSizeDiff + burnFee + STORAGE_SAFETY_MARGIN.toInt()
    }

private val RpcOperationResult.burnFee: BigInt
    get() {
        val originatedContractsFee = originatedContracts.orEmpty().size.toBigInt() * STORAGE_CONTRACT_ALLOCATION.toInt()
        val allocatedDestinationContractFee = if (allocatedDestinationContract == true) STORAGE_CONTRACT_ALLOCATION.toInt() else 0
        return originatedContractsFee + allocatedDestinationContractFee
    }
