package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.PublicKeyHashEncoded
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.active.data.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

// https://tezos.gitlab.io/active/rpc.html
internal class ActiveRpcClient(private val chains: Chains) : ActiveRpc {

    // -- ../<block_id> --

    override suspend fun getBlock(chainId: String, blockId: String, headers: List<HttpHeader>): GetBlockResponse =
        chains.chainId(chainId).blocks.blockId(blockId).get(headers)

    // -- ../<block_id>/context/big_maps --

    override suspend fun getBigMapValues(
        chainId: String,
        blockId: String,
        bigMapId: String,
        offset: UInt?,
        length: UInt?,
        headers: List<HttpHeader>,
    ): GetBigMapValuesResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.bigMaps.bigMapId(bigMapId).get(offset, length, headers)

    override suspend fun getBigMapValue(
        chainId: String,
        blockId: String,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader>,
    ): GetBigMapValueResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.bigMaps.bigMapId(bigMapId).scriptExpr(key).get(headers)

    // -- ../<block_id>/context/constants --

    override suspend fun getConstants(chainId: String, blockId: String, headers: List<HttpHeader>): GetConstantsResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.constants.get(headers)

    // -- ../<block_id>/context/contracts --

    override suspend fun getContractDetails(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractDetailsResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.contracts.contractId(contractId).get(headers)

    override suspend fun getContractBalance(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractBalanceResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.contracts.contractId(contractId).balance.get(headers)

    override suspend fun getContractCounter(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractCounterResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.contracts.contractId(contractId).counter.get(headers) // TODO: handle error when resource is missing

    override suspend fun getContractDelegate(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractDelegateResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.contracts.contractId(contractId).delegate.get(headers) // TODO: handle error when resource is missing

    override suspend fun getContractEntrypoints(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractEntrypointsResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.contracts.contractId(contractId).entrypoints.get(headers)

    override suspend fun getContractEntrypointType(
        chainId: String,
        blockId: String,
        contractId: Address,
        entrypoint: String,
        headers: List<HttpHeader>,
    ): GetContractEntrypointTypeResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.contracts.contractId(contractId).entrypoints.string(entrypoint).get(headers)

    override suspend fun getContractManager(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractManagerResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.contracts.contractId(contractId).managerKey.get(headers)

    override suspend fun getContractScript(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractScriptResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.contracts.contractId(contractId).script.get(headers) // TODO: handle error when resource is missing

    override suspend fun getContractSaplingStateDiff(
        chainId: String,
        blockId: String,
        contractId: Address,
        commitmentOffset: ULong?,
        nullifierOffset: ULong?,
        headers: List<HttpHeader>,
    ): GetContractSaplingStateDiffResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.contracts.contractId(contractId).singleSaplingGetDiff.get(commitmentOffset, nullifierOffset, headers)

    override suspend fun getContractStorage(
        chainId: String,
        blockId: String,
        contractId: Address,
        headers: List<HttpHeader>,
    ): GetContractStorageResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.contracts.contractId(contractId).storage.get(headers)

    // -- ../<block_id>/context/delegates --

    override suspend fun getDelegateDetails(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateDetailsResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).get(headers)

    override suspend fun getDelegateCurrentFrozenDeposits(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateCurrentFrozenDepositsResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).currentFrozenDeposits.get(headers)

    override suspend fun isDelegateDeactivated(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateDeactivatedStatusResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).deactivated.get(headers)

    override suspend fun getDelegateDelegatedBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateDelegatedBalanceResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).delegatedBalance.get(headers)

    override suspend fun getDelegateDelegatedContracts(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateDelegatedContractsResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).delegatedContracts.get(headers)

    override suspend fun getDelegateFrozenDeposits(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateFrozenDepositsResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).frozenDeposits.get(headers)

    override suspend fun getDelegateFrozenDepositsLimit(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateFrozenDepositsLimitResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).frozenDepositsLimit.get(headers)

    override suspend fun getDelegateFullBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateFullBalanceResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).fullBalance.get(headers)

    override suspend fun getDelegateGracePeriod(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateGracePeriodResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).gracePeriod.get(headers)

    override suspend fun getDelegateParticipation(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateParticipationResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).participation.get(headers)

    override suspend fun getDelegateStakingBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateStakingBalanceResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).stakingBalance.get(headers)

    override suspend fun getDelegateVotingPower(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateVotingPowerResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.delegates.pkh(publicKeyHash).votingPower.get(headers)

    // -- ../<block_id>/context/sapling --

    override suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: String,
        stateId: String,
        commitmentOffset: ULong?,
        nullifierOffset: ULong?,
        headers: List<HttpHeader>,
    ): GetSaplingStateDiffResponse =
        chains.chainId(chainId).blocks.blockId(blockId).context.sapling.saplingStateId(stateId).getDiff.get(commitmentOffset, nullifierOffset, headers)

    // -- ../<block_id>/header --

    override suspend fun getBlockHeader(chainId: String, blockId: String, headers: List<HttpHeader>): GetBlockHeaderResponse =
        chains.chainId(chainId).blocks.blockId(blockId).header.get(headers)

    // -- ../<block_id>/helpers --

    override suspend fun preapplyOperations(chainId: String, blockId: String, operations: List<RpcApplicableOperation>, headers: List<HttpHeader>): PreapplyOperationsResponse =
        chains.chainId(chainId).blocks.blockId(blockId).helpers.preapply.operations.post(operations, headers)

    override suspend fun runOperation(chainId: String, blockId: String, operation: RpcRunnableOperation, headers: List<HttpHeader>): RunOperationResponse =
        chains.chainId(chainId).blocks.blockId(blockId).helpers.scripts.runOperation.post(operation, headers)

    // -- ../<block_id>/operations --

    override suspend fun getOperations(chainId: String, blockId: String, headers: List<HttpHeader>): GetBlockOperationsResponse =
        chains.chainId(chainId).blocks.blockId(blockId).operations.get(headers)
}