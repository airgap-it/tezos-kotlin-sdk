package it.airgap.tezos.rpc.internal.estimator

import it.airgap.tezos.core.converter.tez.toMutez
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.toTezosNatural
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.coder.forgeToBytes
import it.airgap.tezos.rpc.converter.asOperation
import it.airgap.tezos.rpc.converter.asOperationContent
import it.airgap.tezos.rpc.converter.asRunnable
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.CachedMap
import it.airgap.tezos.rpc.internal.utils.*
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.type.limits.Limits
import it.airgap.tezos.rpc.type.limits.OperationLimits
import it.airgap.tezos.rpc.type.operation.RpcOperationContent

internal class OperationFeeEstimator(
    private val chains: Chains,
    private val operationContentBytesCoder: ConsumingBytesCoder<OperationContent>,
) : FeeEstimator<Operation> {
    private val chainIdCached: CachedMap<String, ChainId> = CachedMap { key, headers -> chains(key).chainId.get(headers).chainId }

    override suspend fun minFee(chainId: String, operation: Operation, limits: Limits, headers: List<HttpHeader>): Operation {
        val runnableOperation = operation
            .applyLimits(limits)
            .asRunnable(chainId.asChainId(headers))

        val runOperationResult = chains(chainId).blocks.head.helpers.scripts.runOperation.post(runnableOperation, headers)

        return runnableOperation
            .asOperation()
            .updateWith(runOperationResult.contents, operationContentBytesCoder)
    }

    private suspend fun String.asChainId(headers: List<HttpHeader> = emptyList()): ChainId =
        if (ChainId.isValid(this)) ChainId(this) else chainIdCached.get(this, headers)

    private fun Operation.applyLimits(limits: Limits): Operation {
        val maxLimits = maxLimits(limits)
        val updatedContents = contents.map { it.apply(limits = maxLimits) }

        return when (this) {
            is Operation.Unsigned -> copy(contents = updatedContents)
            is Operation.Signed -> copy(contents = updatedContents)
        }
    }

    private fun Operation.maxLimits(limits: Limits): OperationLimits {
        val availableGasLimitPerBlock = BigInt.max(limits.perBlock.gas - this.limits.gasBigInt, BigInt.zero)
        val requiresEstimation = contents.filterNot { it.hasFee }.size
        val maxGasLimitPerOperation = if (requiresEstimation > 0) availableGasLimitPerBlock / BigInt.valueOf(requiresEstimation) else BigInt.zero

        return OperationLimits(
            BigInt.min(limits.perOperation.gasBigInt, maxGasLimitPerOperation),
            limits.perOperation.storageBigInt,
        )
    }

    private fun OperationContent.apply(fee: Mutez = Mutez(0U), limits: OperationLimits = OperationLimits.zero): OperationContent =
        when (this) {
            is OperationContent.Reveal -> copy(
                fee = fee,
                gasLimit = limits.gasBigInt.toTezosNatural(),
                storageLimit = limits.storageBigInt.toTezosNatural()
            )
            is OperationContent.Transaction -> copy(
                fee = fee,
                gasLimit = limits.gasBigInt.toTezosNatural(),
                storageLimit = limits.storageBigInt.toTezosNatural(),
            )
            is OperationContent.Origination -> copy(
                fee = fee,
                gasLimit = limits.gasBigInt.toTezosNatural(),
                storageLimit = limits.storageBigInt.toTezosNatural(),
            )
            is OperationContent.Delegation -> copy(
                fee = fee,
                gasLimit = limits.gasBigInt.toTezosNatural(),
                storageLimit = limits.storageBigInt.toTezosNatural(),
            )
            is OperationContent.RegisterGlobalConstant -> copy(
                fee = fee,
                gasLimit = limits.gasBigInt.toTezosNatural(),
                storageLimit = limits.storageBigInt.toTezosNatural(),
            )
            is OperationContent.SetDepositsLimit -> copy(
                fee = fee,
                gasLimit = limits.gasBigInt.toTezosNatural(),
                storageLimit = limits.storageBigInt.toTezosNatural(),
            )
            else -> this
        }

    private fun Operation.updateWith(rpcContents: List<RpcOperationContent>, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): Operation {
        val groupedContents = rpcContents.groupMutableBy { it.asOperationContent().hashCode() }
        val updatedContents = contents.map {
            val rpcContent = groupedContents.next(it.hashCode()) ?: return@map it
            it.updateWith(rpcContent, operationContentBytesCoder)
        }

        return when (this) {
            is Operation.Unsigned -> copy(contents = updatedContents)
            is Operation.Signed -> copy(contents = updatedContents)
        }
    }

    private fun OperationContent.updateWith(rpcContent: RpcOperationContent, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): OperationContent =
        when {
            !hasFee && this is OperationContent.Manager -> updateWith(rpcContent, operationContentBytesCoder)
            else -> this
        }

    private fun OperationContent.Manager.updateWith(rpcContent: RpcOperationContent, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): OperationContent {
        if (!matches(rpcContent)) return this

        val metadataLimits = rpcContent.metadataLimits ?: return this
        val forged = forgeToBytes(operationContentBytesCoder)
        val size = forged.size + 32 + 64 /* content size + forged branch size + forged signature size */
        val fee = fee(size, metadataLimits)

        return apply(fee = fee, limits = metadataLimits)
    }

    private fun OperationContent.matches(rpcContent: RpcOperationContent): Boolean =
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

    private fun fee(operationSize: Int, limits: OperationLimits): Mutez {
        val gasFee = Nanotez(FEE_PER_GAS_UNIT_NANOTEZ) * limits.gasBigInt
        val storageFee = Nanotez(operationSize.toUInt()) * Nanotez(FEE_PER_STORAGE_BYTE_NANOTEZ)

        return Mutez(FEE_BASE_MUTEZ) + gasFee.toMutez() + storageFee.toMutez() + Mutez(FEE_SAFETY_MARGIN_MUTEZ)
    }

    companion object {
        private const val FEE_BASE_MUTEZ = 100U

        private const val FEE_PER_GAS_UNIT_NANOTEZ = 100U
        private const val FEE_PER_STORAGE_BYTE_NANOTEZ = 1000U

        private const val FEE_SAFETY_MARGIN_MUTEZ = 100U
    }
}