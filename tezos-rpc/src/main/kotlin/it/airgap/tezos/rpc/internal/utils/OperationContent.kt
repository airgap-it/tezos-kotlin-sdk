package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.apply
import it.airgap.tezos.operation.coder.forgeToBytes
import it.airgap.tezos.operation.hasFee
import it.airgap.tezos.rpc.type.operation.RpcOperationContent

internal fun OperationContent.updateWith(rpcContent: RpcOperationContent, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): OperationContent =
    when {
        !hasFee && this is OperationContent.Manager -> updateWith(rpcContent, operationContentBytesCoder)
        else -> this
    }

internal fun OperationContent.Manager.updateWith(rpcContent: RpcOperationContent, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): OperationContent {
    if (!matches(rpcContent)) return this

    val metadataLimits = rpcContent.metadataLimits ?: return this
    val forged = forgeToBytes(operationContentBytesCoder)
    val size = forged.size + 32 + 64 /* content size + forged branch size + forged signature size */
    val fee = fee(size, metadataLimits)

    return apply(fee = fee, limits = metadataLimits)
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