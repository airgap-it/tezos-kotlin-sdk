package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.PublicKeyHash
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.active.block.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

// https://tezos.gitlab.io/active/rpc.html
internal class ActiveSimplifiedRpcClient(private val chains: Chains) : ActiveSimplifiedRpc {

    // -- ../<block_id> --

    override suspend fun getBlock(chainId: String, blockId: String, headers: List<HttpHeader>): GetBlockResponse =
        chains(chainId).blocks(blockId).get(headers)

    // -- ../<block_id>/context/big_maps --

    override suspend fun getBigMap(
        chainId: String,
        blockId: String,
        bigMapId: String,
        offset: UInt?,
        length: UInt?,
        headers: List<HttpHeader>,
    ): GetBigMapResponse =
        chains(chainId).blocks(blockId).context.bigMaps(bigMapId).get(offset, length, headers)

    override suspend fun getBigMapValue(
        chainId: String,
        blockId: String,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader>,
    ): GetBigMapValueResponse =
        chains(chainId).blocks(blockId).context.bigMaps(bigMapId)(key).get(headers)

    // -- ../<block_id>/context/constants --

    override suspend fun getConstants(chainId: String, blockId: String, headers: List<HttpHeader>): GetConstantsResponse =
        chains(chainId).blocks(blockId).context.constants.get(headers)

    // -- ../<block_id>/context/contracts --

    override suspend fun getContractDetails(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractDetailsResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).get(headers)

    override suspend fun getBalance(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractBalanceResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).balance.get(headers)

    override suspend fun getCounter(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractCounterResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).counter.get(headers) // TODO: handle error when resource is missing

    override suspend fun getDelegate(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractDelegateResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).delegate.get(headers) // TODO: handle error when resource is missing

    override suspend fun getEntrypoints(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractEntrypointsResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).entrypoints.get(headers)

    override suspend fun getEntrypoint(
        chainId: String,
        blockId: String,
        contractId: Address,
        entrypoint: String,
        headers: List<HttpHeader>,
    ): GetContractEntrypointResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).entrypoints(entrypoint).get(headers)

    override suspend fun getManagerKey(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractManagerKeyResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).managerKey.get(headers)

    override suspend fun getScript(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractScriptResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).script.get(headers) // TODO: handle error when resource is missing

    override suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: String,
        contractId: Address,
        commitmentOffset: ULong?,
        nullifierOffset: ULong?,
        headers: List<HttpHeader>,
    ): GetContractSaplingStateDiffResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).singleSaplingGetDiff.get(commitmentOffset, nullifierOffset, headers)

    override suspend fun getStorage(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractStorageResponse =
        chains(chainId).blocks(blockId).context.contracts(contractId).storage.get(headers)

    // -- ../<block_id>/context/delegates --

    override suspend fun getDelegateDetails(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateDetailsResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).get(headers)

    override suspend fun getCurrentFrozenDeposits(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateCurrentFrozenDepositsResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).currentFrozenDeposits.get(headers)

    override suspend fun isDeactivated(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateDeactivatedStatusResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).deactivated.get(headers)

    override suspend fun getDelegatedBalance(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateDelegatedBalanceResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).delegatedBalance.get(headers)

    override suspend fun getDelegatedContracts(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateDelegatedContractsResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).delegatedContracts.get(headers)

    override suspend fun getFrozenDeposits(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateFrozenDepositsResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).frozenDeposits.get(headers)

    override suspend fun getFrozenDepositsLimit(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateFrozenDepositsLimitResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).frozenDepositsLimit.get(headers)

    override suspend fun getFullBalance(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateFullBalanceResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).fullBalance.get(headers)

    override suspend fun getGracePeriod(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateGracePeriodResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).gracePeriod.get(headers)

    override suspend fun getParticipation(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateParticipationResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).participation.get(headers)

    override suspend fun getStakingBalance(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateStakingBalanceResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).stakingBalance.get(headers)

    override suspend fun getVotingPower(
        chainId: String,
        blockId: String,
        delegateId: PublicKeyHash,
        headers: List<HttpHeader>,
    ): GetDelegateVotingPowerResponse =
        chains(chainId).blocks(blockId).context.delegates(delegateId).votingPower.get(headers)

    // -- ../<block_id>/context/sapling --

    override suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: String,
        stateId: String,
        commitmentOffset: ULong?,
        nullifierOffset: ULong?,
        headers: List<HttpHeader>,
    ): GetSaplingStateDiffResponse =
        chains(chainId).blocks(blockId).context.sapling(stateId).getDiff.get(commitmentOffset, nullifierOffset, headers)

    // -- ../<block_id>/header --

    override suspend fun getBlockHeader(chainId: String, blockId: String, headers: List<HttpHeader>): GetBlockHeaderResponse =
        chains(chainId).blocks(blockId).header.get(headers)

    // -- ../<block_id>/helpers --

    override suspend fun preapplyOperations(chainId: String, blockId: String, operations: List<RpcApplicableOperation>, headers: List<HttpHeader>): PreapplyOperationsResponse =
        chains(chainId).blocks(blockId).helpers.preapply.operations.post(operations, headers)

    override suspend fun runOperation(chainId: String, blockId: String, operation: RpcRunnableOperation, headers: List<HttpHeader>): RunOperationResponse =
        chains(chainId).blocks(blockId).helpers.scripts.runOperation.post(operation, headers)

    // -- ../<block_id>/operations --

    override suspend fun getOperations(chainId: String, blockId: String, headers: List<HttpHeader>): GetBlockOperationsResponse =
        chains(chainId).blocks(blockId).operations.get(headers)
}