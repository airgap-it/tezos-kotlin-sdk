package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.encoded.SignatureEncoded
import it.airgap.tezos.operation.*
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.type.Fee
import it.airgap.tezos.operation.type.FeeOperationLimits
import it.airgap.tezos.rpc.asOperationContent
import it.airgap.tezos.rpc.type.RpcError
import it.airgap.tezos.rpc.type.operation.*

private const val GAS_SAFETY_MARGIN = 100U
private const val STORAGE_SAFETY_MARGIN = 100U

internal fun Operation.withFeeFrom(
    rpcContents: List<RpcOperationContent>,
    operationContentBytesCoder: OperationContentBytesCoder,
): Operation {
    val groupedContents = rpcContents.groupBy { it.asOperationContent().hashCode() }
    val contentsCursors = mutableMapOf<Int, Int>()
    val updatedContents = contents.map {
        val key = hashCode()
        val cursor = contentsCursors.next(key)
        val rpcContent = groupedContents[key]?.getOrNull(cursor) ?: return@map it

        it.withFeeFrom(rpcContent, operationContentBytesCoder)
    }

    return when (this) {
        is Operation.Unsigned -> copy(contents = updatedContents)
        is Operation.Signed -> copy(contents = updatedContents)
    }
}

internal fun OperationContent.withFeeFrom(rpcContent: RpcOperationContent, operationContentBytesCoder: OperationContentBytesCoder): OperationContent =
    when {
        hasFee && this is OperationContent.Manager -> withFeeFrom(rpcContent, operationContentBytesCoder)
        else -> this
    }

internal fun OperationContent.Manager.withFeeFrom(rpcContent: RpcOperationContent, operationContentBytesCoder: OperationContentBytesCoder): OperationContent {
    if (!matches(rpcContent)) return this

    val metadataLimits = rpcContent.metadataLimits ?: return this
    val forged = forgeToBytes(operationContentBytesCoder)
    val size = forged.size + 32 + 64 /* content size + forged branch size + forged signature size */
    val fee = Fee(size, metadataLimits)

    return applyFee(fee)
}

internal fun OperationContent.matches(rpcContent: RpcOperationContent): Boolean =
    when (this) {
        is OperationContent.ActivateAccount -> rpcContent is RpcOperationContent.ActivateAccount
        is OperationContent.Ballot -> rpcContent is RpcOperationContent.Ballot
        is OperationContent.Endorsement -> rpcContent is RpcOperationContent.Endorsement
        is OperationContent.Preendorsement -> rpcContent is RpcOperationContent.Preendorsement
        is OperationContent.DoubleBakingEvidence -> rpcContent is RpcOperationContent.DoubleBakingEvidence
        is OperationContent.DoubleEndorsementEvidence -> rpcContent is RpcOperationContent.DoubleEndorsementEvidence
        is OperationContent.DoublePreendorsementEvidence -> rpcContent is RpcOperationContent.DoublePreendorsementEvidence
        is OperationContent.FailingNoop -> rpcContent is RpcOperationContent.FailingNoop
        is OperationContent.Delegation -> rpcContent is RpcOperationContent.Delegation
        is OperationContent.Origination -> rpcContent is RpcOperationContent.Origination
        is OperationContent.RegisterGlobalConstant -> rpcContent is RpcOperationContent.RegisterGlobalConstant
        is OperationContent.Reveal -> rpcContent is RpcOperationContent.Reveal
        is OperationContent.SetDepositsLimit -> rpcContent is RpcOperationContent.SetDepositsLimit
        is OperationContent.Transaction -> rpcContent is RpcOperationContent.Transaction
        is OperationContent.Proposals -> rpcContent is RpcOperationContent.Proposals
        is OperationContent.SeedNonceRevelation -> rpcContent is RpcOperationContent.SeedNonceRevelation
    }

internal val RpcOperationContent.metadataLimits: FeeOperationLimits?
    get() = when (this) {
        is RpcOperationContent.Delegation -> metadata?.limits
        is RpcOperationContent.Origination -> metadata?.limits
        is RpcOperationContent.RegisterGlobalConstant -> metadata?.limits
        is RpcOperationContent.Reveal -> metadata?.limits
        is RpcOperationContent.SetDepositsLimit -> metadata?.limits
        is RpcOperationContent.Transaction -> metadata?.limits
        else -> null
    }

internal val RpcOperationMetadata.Delegation.limits: FeeOperationLimits
    get() = internalOperationResults.orEmpty().fold(operationResult.limits) { acc, internalResult -> acc + internalResult.result.limits }

internal val RpcOperationMetadata.Origination.limits: FeeOperationLimits
    get() = internalOperationResults.orEmpty().fold(operationResult.limits) { acc, internalResult -> acc + internalResult.result.limits }

internal val RpcOperationMetadata.RegisterGlobalConstant.limits: FeeOperationLimits
    get() = internalOperationResults.orEmpty().fold(operationResult.limits) { acc, internalResult -> acc + internalResult.result.limits }

internal val RpcOperationMetadata.Reveal.limits: FeeOperationLimits
    get() = internalOperationResults.orEmpty().fold(operationResult.limits) { acc, internalResult -> acc + internalResult.result.limits }

internal val RpcOperationMetadata.SetDepositsLimit.limits: FeeOperationLimits
    get() = internalOperationResults.orEmpty().fold(operationResult.limits) { acc, internalResult -> acc + internalResult.result.limits }

internal val RpcOperationMetadata.Transaction.limits: FeeOperationLimits
    get() = internalOperationResults.orEmpty().fold(operationResult.limits) { acc, internalResult -> acc + internalResult.result.limits }

internal val RpcOperationResult.Delegation.limits: FeeOperationLimits
    get() = when (this) {
        is RpcOperationResult.Delegation.Applied -> FeeOperationLimits(
            gas = (consumedGas?.let { BigInt.valueOf(it) } ?: BigInt.zero) + GAS_SAFETY_MARGIN.toInt(),
            storage = BigInt.zero + STORAGE_SAFETY_MARGIN.toInt(),
        )
        else -> failWithRpcError(errors)
    }

internal val RpcOperationResult.Origination.limits: FeeOperationLimits
    get() = when (this) {
        is RpcOperationResult.Origination.Applied -> FeeOperationLimits(
            gas = (consumedGas?.let { BigInt.valueOf(it) } ?: BigInt.zero) + GAS_SAFETY_MARGIN.toInt(),
            storage = (paidStorageSizeDiff?.let { BigInt.valueOf(it) } ?: BigInt.zero) + STORAGE_SAFETY_MARGIN.toInt(),
        )
        else -> failWithRpcError(errors)
    }

internal val RpcOperationResult.RegisterGlobalConstant.limits: FeeOperationLimits
    get() = when (this) {
        is RpcOperationResult.RegisterGlobalConstant.Applied -> FeeOperationLimits(
            gas = BigInt.valueOf(consumedGas) + GAS_SAFETY_MARGIN.toInt(),
            storage = BigInt.zero,
        )
        else -> failWithRpcError(errors)
    }

internal val RpcOperationResult.Reveal.limits: FeeOperationLimits
    get() = when (this) {
        is RpcOperationResult.Reveal.Applied -> FeeOperationLimits(
            gas = (consumedGas?.let { BigInt.valueOf(it) } ?: BigInt.zero) + GAS_SAFETY_MARGIN.toInt(),
            storage = BigInt.zero,
        )
        else -> failWithRpcError(errors)
    }

internal val RpcOperationResult.SetDepositsLimit.limits: FeeOperationLimits
    get() = when (this) {
        is RpcOperationResult.SetDepositsLimit.Applied -> FeeOperationLimits(
            gas = (consumedGas?.let { BigInt.valueOf(it) } ?: BigInt.zero) + GAS_SAFETY_MARGIN.toInt(),
            storage = BigInt.zero,
        )
        else -> failWithRpcError(errors)
    }


internal val RpcOperationResult.Transaction.limits: FeeOperationLimits
    get() = when (this) {
        is RpcOperationResult.Transaction.Applied -> FeeOperationLimits(
            gas = (consumedGas?.let { BigInt.valueOf(it) } ?: BigInt.zero) + GAS_SAFETY_MARGIN.toInt(),
            storage = (paidStorageSizeDiff?.let { BigInt.valueOf(it) } ?: BigInt.zero) + STORAGE_SAFETY_MARGIN.toInt(),
        )
        else -> failWithRpcError(errors)
    }

internal val Operation.signatureOrPlaceholder: SignatureEncoded
    get() = when (this) {
        is Operation.Unsigned -> SignatureEncoded.placeholder
        is Operation.Signed -> signature
    }

internal val RpcRunnableOperation.isSigned: Boolean
    get() = !signature.isPlaceholder

private fun <K> MutableMap<K, Int>.next(key: K): Int {
    val next = getOrDefault(key, -1) + 1
    return next.also { put(key, it) }
}

private fun failWithRpcError(errors: List<RpcError>?): Nothing = TODO()