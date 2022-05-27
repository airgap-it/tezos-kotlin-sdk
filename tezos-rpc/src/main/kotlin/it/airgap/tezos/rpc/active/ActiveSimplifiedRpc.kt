package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.active.block.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

// https://tezos.gitlab.io/active/rpc.html
public interface ActiveSimplifiedRpc {

    // -- ../<block_id> --

    public suspend fun getBlock(chainId: String = Constants.Chain.MAIN, blockId: String = Constants.Block.HEAD, headers: List<HttpHeader> = emptyList()): GetBlockResponse
    public suspend fun getBlock(chainId: ChainId, blockId: Int, headers: List<HttpHeader> = emptyList()): GetBlockResponse = getBlock(chainId.base58, blockId.toString(), headers)
    public suspend fun getBlock(chainId: ChainId, blockId: BlockHash, headers: List<HttpHeader> = emptyList()): GetBlockResponse = getBlock(chainId.base58, blockId.base58, headers)

    // -- ../<block_id>/context/big_maps --

    public suspend fun getBigMap(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        bigMapId: String,
        offset: UInt? = null,
        length: UInt? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapResponse
    public suspend fun getBigMap(
        chainId: ChainId,
        blockId: Int,
        bigMapId: String,
        offset: UInt? = null,
        length: UInt? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapResponse = getBigMap(chainId.base58, blockId.toString(), bigMapId, offset, length, headers)
    public suspend fun getBigMap(
        chainId: ChainId,
        blockId: BlockHash,
        bigMapId: String,
        offset: UInt? = null,
        length: UInt? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapResponse = getBigMap(chainId.base58, blockId.base58, bigMapId, offset, length, headers)

    public suspend fun getBigMapValue(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValueResponse
    public suspend fun getBigMapValue(
        chainId: ChainId,
        blockId: Int,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValueResponse = getBigMapValue(chainId.base58, blockId.toString(), bigMapId, key, headers)
    public suspend fun getBigMapValue(
        chainId: ChainId,
        blockId: BlockHash,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValueResponse = getBigMapValue(chainId.base58, blockId.base58, bigMapId, key, headers)

    // -- ../<block_id>/context/constants --

    public suspend fun getConstants(chainId: String = Constants.Chain.MAIN, blockId: String = Constants.Block.HEAD, headers: List<HttpHeader> = emptyList()): GetConstantsResponse
    public suspend fun getConstants(chainId: ChainId, blockId: Int, headers: List<HttpHeader> = emptyList()): GetConstantsResponse = getConstants(chainId.base58, blockId.toString(), headers)
    public suspend fun getConstants(chainId: ChainId, blockId: BlockHash, headers: List<HttpHeader> = emptyList()): GetConstantsResponse = getConstants(chainId.base58, blockId.base58, headers)

    // -- ../<block_id>/context/contracts --

    public suspend fun getContractDetails(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDetailsResponse
    public suspend fun getContractDetails(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDetailsResponse = getContractDetails(chainId, blockId.toString(), contractId, headers)
    public suspend fun getContractDetails(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDetailsResponse = getContractDetails(chainId, blockId.base58, contractId, headers)

    public suspend fun getBalance(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractBalanceResponse
    public suspend fun getBalance(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractBalanceResponse = getBalance(chainId, blockId.toString(), contractId, headers)
    public suspend fun getBalance(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractBalanceResponse = getBalance(chainId, blockId.base58, contractId, headers)

    public suspend fun getCounter(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractCounterResponse
    public suspend fun getCounter(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractCounterResponse = getCounter(chainId, blockId.toString(), contractId, headers)
    public suspend fun getCounter(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractCounterResponse = getCounter(chainId, blockId.base58, contractId, headers)

    public suspend fun getDelegate(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDelegateResponse
    public suspend fun getDelegate(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDelegateResponse = getDelegate(chainId, blockId.toString(), contractId, headers)
    public suspend fun getDelegate(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDelegateResponse = getDelegate(chainId, blockId.base58, contractId, headers)

    public suspend fun getEntrypoints(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointsResponse
    public suspend fun getEntrypoints(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointsResponse = getEntrypoints(chainId, blockId.toString(), contractId, headers)
    public suspend fun getEntrypoints(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointsResponse = getEntrypoints(chainId, blockId.base58, contractId, headers)

    public suspend fun getEntrypoint(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        entrypoint: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointResponse
    public suspend fun getEntrypoint(
        chainId: String,
        blockId: Int,
        contractId: Address,
        entrypoint: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointResponse = getEntrypoint(chainId, blockId.toString(), contractId, entrypoint)
    public suspend fun getEntrypoint(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        entrypoint: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointResponse = getEntrypoint(chainId, blockId.base58, contractId, entrypoint)

    public suspend fun getManagerKey(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractManagerKeyResponse
    public suspend fun getManagerKey(
        chainId: String,
        blockId: Int,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractManagerKeyResponse = getManagerKey(chainId, blockId.toString(), contractId, headers)
    public suspend fun getManagerKey(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractManagerKeyResponse = getManagerKey(chainId, blockId.base58, contractId, headers)

    public suspend fun getScript(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedScriptResponse
    public suspend fun getScript(
        chainId: String,
        blockId: Int,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedScriptResponse = getScript(chainId, blockId.toString(), contractId, unparsingMode, headers)
    public suspend fun getScript(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedScriptResponse = getScript(chainId, blockId.base58, contractId, unparsingMode, headers)

    public suspend fun getSaplingStateDiff(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractSaplingStateDiffResponse
    public suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: Int,
        contractId: Address,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractSaplingStateDiffResponse = getSaplingStateDiff(chainId, blockId.toString(), contractId, commitmentOffset, nullifierOffset, headers)
    public suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractSaplingStateDiffResponse = getSaplingStateDiff(chainId, blockId.base58, contractId, commitmentOffset, nullifierOffset, headers)

    public suspend fun getStorage(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedStorageResponse
    public suspend fun getStorage(
        chainId: String,
        blockId: Int,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedStorageResponse = getStorage(chainId, blockId.toString(), contractId, unparsingMode, headers)
    public suspend fun getStorage(
        chainId: String,
        blockId: BlockHash,
        contractId: Address,
        unparsingMode: RpcScriptParsing = RpcScriptParsing.Readable,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractNormalizedStorageResponse = getStorage(chainId, blockId.base58, contractId, unparsingMode, headers)

    // -- ../<block_id>/context/delegates --

    public suspend fun getDelegateDetails(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDetailsResponse
    public suspend fun getDelegateDetails(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDetailsResponse = getDelegateDetails(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getDelegateDetails(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDetailsResponse = getDelegateDetails(chainId, blockId.base58, delegateId, headers)

    public suspend fun getCurrentFrozenDeposits(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateCurrentFrozenDepositsResponse
    public suspend fun getCurrentFrozenDeposits(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateCurrentFrozenDepositsResponse = getCurrentFrozenDeposits(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getCurrentFrozenDeposits(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateCurrentFrozenDepositsResponse = getCurrentFrozenDeposits(chainId, blockId.base58, delegateId, headers)

    public suspend fun isDeactivated(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDeactivatedStatusResponse
    public suspend fun isDeactivated(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDeactivatedStatusResponse = isDeactivated(chainId, blockId.toString(), delegateId, headers)
    public suspend fun isDeactivated(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDeactivatedStatusResponse = isDeactivated(chainId, blockId.base58, delegateId, headers)

    public suspend fun getDelegatedBalance(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedBalanceResponse
    public suspend fun getDelegatedBalance(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedBalanceResponse = getDelegatedBalance(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getDelegatedBalance(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedBalanceResponse = getDelegatedBalance(chainId, blockId.base58, delegateId, headers)

    public suspend fun getDelegatedContracts(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedContractsResponse
    public suspend fun getDelegatedContracts(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedContractsResponse = getDelegatedContracts(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getDelegatedContracts(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedContractsResponse = getDelegatedContracts(chainId, blockId.base58, delegateId, headers)

    public suspend fun getFrozenDeposits(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsResponse
    public suspend fun getFrozenDeposits(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsResponse = getFrozenDeposits(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getFrozenDeposits(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsResponse = getFrozenDeposits(chainId, blockId.base58, delegateId, headers)

    public suspend fun getFrozenDepositsLimit(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsLimitResponse
    public suspend fun getFrozenDepositsLimit(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsLimitResponse = getFrozenDepositsLimit(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getFrozenDepositsLimit(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsLimitResponse = getFrozenDepositsLimit(chainId, blockId.base58, delegateId, headers)

    public suspend fun getFullBalance(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFullBalanceResponse
    public suspend fun getFullBalance(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFullBalanceResponse = getFullBalance(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getFullBalance(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFullBalanceResponse = getFullBalance(chainId, blockId.base58, delegateId, headers)

    public suspend fun getGracePeriod(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateGracePeriodResponse
    public suspend fun getGracePeriod(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateGracePeriodResponse = getGracePeriod(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getGracePeriod(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateGracePeriodResponse = getGracePeriod(chainId, blockId.base58, delegateId, headers)

    public suspend fun getParticipation(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateParticipationResponse
    public suspend fun getParticipation(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateParticipationResponse = getParticipation(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getParticipation(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateParticipationResponse = getParticipation(chainId, blockId.base58, delegateId, headers)

    public suspend fun getStakingBalance(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateStakingBalanceResponse
    public suspend fun getStakingBalance(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateStakingBalanceResponse = getStakingBalance(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getStakingBalance(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateStakingBalanceResponse = getStakingBalance(chainId, blockId.base58, delegateId, headers)

    public suspend fun getVotingPower(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateVotingPowerResponse
    public suspend fun getVotingPower(
        chainId: String,
        blockId: Int,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateVotingPowerResponse = getVotingPower(chainId, blockId.toString(), delegateId, headers)
    public suspend fun getVotingPower(
        chainId: String,
        blockId: BlockHash,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateVotingPowerResponse = getVotingPower(chainId, blockId.base58, delegateId, headers)

    // -- ../<block_id>/context/sapling --

    public suspend fun getSaplingStateDiff(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        stateId: String,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetSaplingStateDiffResponse
    public suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: Int,
        stateId: String,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetSaplingStateDiffResponse = getSaplingStateDiff(chainId, blockId.toString(), stateId, commitmentOffset, nullifierOffset, headers)
    public suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: BlockHash,
        stateId: String,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetSaplingStateDiffResponse = getSaplingStateDiff(chainId, blockId.base58, stateId, commitmentOffset, nullifierOffset, headers)

    // -- ../<block_id>/header --

    public suspend fun getBlockHeader(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockHeaderResponse
    public suspend fun getBlockHeader(
        chainId: String,
        blockId: Int,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockHeaderResponse = getBlockHeader(chainId, blockId.toString(), headers)
    public suspend fun getBlockHeader(
        chainId: String,
        blockId: BlockHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockHeaderResponse = getBlockHeader(chainId, blockId.base58, headers)

    // -- ../<block_id>/helpers --

    public suspend fun preapplyOperations(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        operations: List<RpcApplicableOperation>,
        headers: List<HttpHeader> = emptyList(),
    ): PreapplyOperationsResponse
    public suspend fun preapplyOperations(
        chainId: String,
        blockId: Int,
        operations: List<RpcApplicableOperation>,
        headers: List<HttpHeader> = emptyList(),
    ): PreapplyOperationsResponse = preapplyOperations(chainId, blockId.toString(), operations, headers)
    public suspend fun preapplyOperations(
        chainId: String,
        blockId: BlockHash,
        operations: List<RpcApplicableOperation>,
        headers: List<HttpHeader> = emptyList(),
    ): PreapplyOperationsResponse = preapplyOperations(chainId, blockId.base58, operations, headers)

    public suspend fun runOperation(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        operation: RpcRunnableOperation,
        headers: List<HttpHeader> = emptyList(),
    ): RunOperationResponse
    public suspend fun runOperation(
        chainId: String,
        blockId: Int,
        operation: RpcRunnableOperation,
        headers: List<HttpHeader> = emptyList(),
    ): RunOperationResponse = runOperation(chainId, blockId.toString(), operation, headers)
    public suspend fun runOperation(
        chainId: String,
        blockId: BlockHash,
        operation: RpcRunnableOperation,
        headers: List<HttpHeader> = emptyList(),
    ): RunOperationResponse = runOperation(chainId, blockId.base58, operation, headers)

    // -- ../<block_id>/operations --

    public suspend fun getOperations(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockOperationsResponse
    public suspend fun getOperations(
        chainId: String,
        blockId: Int,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockOperationsResponse = getOperations(chainId, blockId.toString(), headers)
    public suspend fun getOperations(
        chainId: String,
        blockId: BlockHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockOperationsResponse = getOperations(chainId, blockId.base58, headers)

}