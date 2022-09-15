package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.PublicKeyHash
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.active.block.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

// https://tezos.gitlab.io/active/rpc.html
internal class ActiveSimplifiedRpcClient(private val chains: Chains) : ActiveSimplifiedRpc {

    // -- ../<block_id> --

    override suspend fun getBlock(
        chainId: String,
        blockId: String,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBlockResponse =
        chains(chainId).blocks(blockId).get(headers, requestTimeout, connectionTimeout)

    // -- ../<block_id>/context/big_maps --

    override suspend fun getBigMap(
        chainId: String,
        blockId: String,
        bigMapId: String,
        offset: UInt?,
        length: UInt?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBigMapResponse =
        chains(chainId).blocks(blockId).context.bigMaps(bigMapId).get(offset, length, headers, requestTimeout, connectionTimeout)

    override suspend fun getBigMapValue(
        chainId: String,
        blockId: String,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBigMapValueResponse =
        chains(chainId).blocks(blockId).context.bigMaps(bigMapId)(key).get(headers, requestTimeout, connectionTimeout)

    // -- ../<block_id>/context/constants --

    override suspend fun getConstants(
        chainId: String,
        blockId: String,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetConstantsResponse =
        chains(chainId).blocks(blockId).context.constants.get(headers, requestTimeout, connectionTimeout)

    // -- ../<block_id>/context/contracts --

    override suspend fun getContractDetails(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractDetailsResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).get(headers, requestTimeout, connectionTimeout)

    override suspend fun getBalance(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractBalanceResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).balance.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getCounter(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractCounterResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).counter.get(headers, requestTimeout, connectionTimeout,) // TODO: handle error when resource is missig

    override suspend fun getDelegate(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractDelegateResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).delegate.get(headers, requestTimeout, connectionTimeout,) // TODO: handle error when resource is missig

    override suspend fun getEntrypoints(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractEntrypointsResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).entrypoints.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getEntrypoint(
        chainId: String,
        blockId: String,
        contractId: Address,
        entrypoint: String,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractEntrypointResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).entrypoints(entrypoint).get(headers, requestTimeout, connectionTimeout)

    override suspend fun getManagerKey(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractManagerKeyResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).managerKey.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getScript(
        chainId: String,
        blockId: String,
        contractId: Address,
        unparsingMode: RpcScriptParsing,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractNormalizedScriptResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).script.normalized.post(unparsingMode, headers, requestTimeout, connectionTimeout,) // TODO: handle error when resource is missig

    override suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: String,
        contractId: Address,
        commitmentOffset: ULong?,
        nullifierOffset: ULong?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractSaplingStateDiffResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).singleSaplingGetDiff.get(commitmentOffset, nullifierOffset, headers, requestTimeout, connectionTimeout)

    override suspend fun getStorage(
        chainId: String,
        blockId: String,
        contractId: Address,
        unparsingMode: RpcScriptParsing,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractNormalizedStorageResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).storage.normalized.post(unparsingMode, headers, requestTimeout, connectionTimeout)

    // -- ../<block_id>/context/delegates --

    override suspend fun getDelegateDetails(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateDetailsResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).get(headers, requestTimeout, connectionTimeout)

    override suspend fun getCurrentFrozenDeposits(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateCurrentFrozenDepositsResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).currentFrozenDeposits.get(headers, requestTimeout, connectionTimeout)

    override suspend fun isDeactivated(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateDeactivatedStatusResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).deactivated.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getDelegatedBalance(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateDelegatedBalanceResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).delegatedBalance.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getDelegatedContracts(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateDelegatedContractsResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).delegatedContracts.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getFrozenDeposits(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateFrozenDepositsResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).frozenDeposits.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getFrozenDepositsLimit(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateFrozenDepositsLimitResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).frozenDepositsLimit.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getFullBalance(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateFullBalanceResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).fullBalance.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getGracePeriod(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateGracePeriodResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).gracePeriod.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getParticipation(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateParticipationResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).participation.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getStakingBalance(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateStakingBalanceResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).stakingBalance.get(headers, requestTimeout, connectionTimeout)

    override suspend fun getVotingPower(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateVotingPowerResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).votingPower.get(headers, requestTimeout, connectionTimeout)

    // -- ../<block_id>/context/sapling --

    override suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: String,
        stateId: String,
        commitmentOffset: ULong?,
        nullifierOffset: ULong?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetSaplingStateDiffResponse =
        chains(chainId).blocks(blockId).context.sapling(stateId).getDiff.get(commitmentOffset, nullifierOffset, headers, requestTimeout, connectionTimeout)

    // -- ../<block_id>/header --

    override suspend fun getBlockHeader(
        chainId: String,
        blockId: String,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBlockHeaderResponse =
        chains(chainId).blocks(blockId).header.get(headers, requestTimeout, connectionTimeout)

    // -- ../<block_id>/helpers --

    override suspend fun preapplyOperations(
        chainId: String,
        blockId: String,
        operations: List<RpcApplicableOperation>,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): PreapplyOperationsResponse =
        chains(chainId).blocks(blockId).helpers.preapply.operations.post(operations, headers, requestTimeout, connectionTimeout)

    override suspend fun runOperation(
        chainId: String,
        blockId: String,
        operation: RpcRunnableOperation,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): RunOperationResponse =
        chains(chainId).blocks(blockId).helpers.scripts.runOperation.post(operation, headers, requestTimeout, connectionTimeout)

    // -- ../<block_id>/operations --

    override suspend fun getOperations(
        chainId: String,
        blockId: String,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBlockOperationsResponse =
        chains(chainId).blocks(blockId).operations.get(headers, requestTimeout, connectionTimeout)
}