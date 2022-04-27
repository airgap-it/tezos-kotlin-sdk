package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.type.encoded.SignatureEncoded
import it.airgap.tezos.operation.*
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.type.Fee
import it.airgap.tezos.rpc.asOperationContent
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

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

    val metadataFee = rpcContent.metadataFee ?: return this
    val forged = forgeToBytes(operationContentBytesCoder)
    val size = forged.size + 32 + 64 /* content size + forged branch size + forged signature size */
    val fee = Fee(size, metadataFee.limits)

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

internal val RpcOperationContent.metadataFee: Fee?
    get() = TODO()

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