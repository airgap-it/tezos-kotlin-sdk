package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.active.block.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

// https://tezos.gitlab.io/active/rpc.html
public interface ActiveSimplifiedRpc {

    // -- ../<block_id> --

    public suspend fun getBlock(chainId: String = Constants.Chain.MAIN, blockId: String = Constants.Block.HEAD, headers: List<HttpHeader> = emptyList()): GetBlockResponse
    public suspend fun getBlock(chainId: ChainId, blockId: BlockHash, headers: List<HttpHeader> = emptyList()): GetBlockResponse =
        getBlock(chainId.base58, blockId.base58, headers)

    // -- ../<block_id>/context/big_maps --

    public suspend fun getBigMapValues(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        bigMapId: String,
        offset: UInt? = null,
        length: UInt? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValuesResponse
    public suspend fun getBigMapValues(
        chainId: ChainId,
        blockId: BlockHash,
        bigMapId: String,
        offset: UInt? = null,
        length: UInt? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValuesResponse = getBigMapValues(chainId.base58, blockId.base58, bigMapId, offset, length, headers)

    public suspend fun getBigMapValue(
        chainId: String = Constants.Chain.MAIN,
        blockId: String = Constants.Block.HEAD,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValueResponse
    public suspend fun getBigMapValue(
        chainId: ChainId,
        blockId: BlockHash,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValueResponse = getBigMapValue(chainId.base58, blockId.base58, bigMapId, key, headers)

    // -- ../<block_id>/context/constants --

    public suspend fun getConstants(chainId: String = Constants.Chain.MAIN, blockId: String = Constants.Block.HEAD, headers: List<HttpHeader> = emptyList()): GetConstantsResponse
    public suspend fun getConstants(chainId: ChainId, blockId: BlockHash, headers: List<HttpHeader> = emptyList()): GetConstantsResponse =
        getConstants(chainId.base58, blockId.base58, headers)

    // -- ../<block_id>/context/contracts --

    public suspend fun getContractDetails(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDetailsResponse

    public suspend fun getContractBalance(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractBalanceResponse

    public suspend fun getContractCounter(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractCounterResponse

    public suspend fun getContractDelegate(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDelegateResponse

    public suspend fun getContractEntrypoints(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointsResponse

    public suspend fun getContractEntrypointType(
        chainId: String,
        blockId: String,
        contractId: Address,
        entrypoint: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointTypeResponse

    public suspend fun getContractManager(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractManagerResponse

    public suspend fun getContractScript(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractScriptResponse

    public suspend fun getContractSaplingStateDiff(
        chainId: String,
        blockId: String,
        contractId: Address,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractSaplingStateDiffResponse

    public suspend fun getContractStorage(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractStorageResponse

    // -- ../<block_id>/context/delegates --

    public suspend fun getDelegateDetails(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDetailsResponse

    public suspend fun getDelegateCurrentFrozenDeposits(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateCurrentFrozenDepositsResponse

    public suspend fun isDelegateDeactivated(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDeactivatedStatusResponse

    public suspend fun getDelegateDelegatedBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedBalanceResponse

    public suspend fun getDelegateDelegatedContracts(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedContractsResponse

    public suspend fun getDelegateFrozenDeposits(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsResponse

    public suspend fun getDelegateFrozenDepositsLimit(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsLimitResponse

    public suspend fun getDelegateFullBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFullBalanceResponse

    public suspend fun getDelegateGracePeriod(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateGracePeriodResponse

    public suspend fun getDelegateParticipation(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateParticipationResponse

    public suspend fun getDelegateStakingBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateStakingBalanceResponse

    public suspend fun getDelegateVotingPower(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateVotingPowerResponse

    // -- ../<block_id>/context/sapling --

    public suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: String,
        stateId: String,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetSaplingStateDiffResponse

    // -- ../<block_id>/header --

    public suspend fun getBlockHeader(
        chainId: String,
        blockId: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockHeaderResponse

    // -- ../<block_id>/helpers --

    public suspend fun preapplyOperations(
        chainId: String,
        blockId: String,
        operations: List<RpcApplicableOperation>,
        headers: List<HttpHeader> = emptyList(),
    ): PreapplyOperationsResponse

    public suspend fun runOperation(
        chainId: String,
        blockId: String,
        operation: RpcRunnableOperation,
        headers: List<HttpHeader> = emptyList(),
    ): RunOperationResponse

    // -- ../<block_id>/operations --

    public suspend fun getOperations(
        chainId: String,
        blockId: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockOperationsResponse
}