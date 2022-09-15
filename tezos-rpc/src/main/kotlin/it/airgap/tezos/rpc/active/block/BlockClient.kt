package it.airgap.tezos.rpc.active.block

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.PublicKeyHash
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

internal class BlockClient(parentUrl: String, blockId: String, private val httpClient: HttpClient) : Block {
    private val baseUrl: String = /* ../<block_id> */ "$parentUrl/$blockId"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBlockResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)

    override val context: Block.Context by lazy { BlockContextClient(baseUrl, httpClient) }
    override val header: Block.Header by lazy { BlockHeaderClient(baseUrl, httpClient) }
    override val helpers: Block.Helpers by lazy { BlockHelpersClient(baseUrl, httpClient) }
    override val operations: Block.Operations by lazy { BlockOperationsClient(baseUrl, httpClient) }
}

private class BlockContextClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context {
    private val baseUrl: String = /* ../<block_id>/context */ "$parentUrl/context"

    override val bigMaps: Block.Context.BigMaps by lazy { BlockContextBigMapsClient(baseUrl, httpClient) }
    override val constants: Block.Context.Constants by lazy { BlockContextConstantsClient(baseUrl, httpClient) }
    override val contracts: Block.Context.Contracts by lazy { BlockContextContractsClient(baseUrl, httpClient) }
    override val delegates: Block.Context.Delegates by lazy { BlockContextDelegatesClient(baseUrl, httpClient) }
    override val sapling: Block.Context.Sapling by lazy { BlockContextSapling(baseUrl, httpClient) }
}

private class BlockContextBigMapsClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.BigMaps {
    private val baseUrl: String = /* ../<block_id>/context/big_maps */ "$parentUrl/big_maps"

    override operator fun invoke(bigMapId: String): Block.Context.BigMaps.BigMap = BlockContextBigMapsBigMapClient(baseUrl, bigMapId, httpClient)
}

private class BlockContextBigMapsBigMapClient(parentUrl: String, bigMapId: String, private val httpClient: HttpClient) : Block.Context.BigMaps.BigMap {
    private val baseUrl: String = /* ../<block_id>/context/big_maps/<big_map_id> */ "$parentUrl/$bigMapId"

    override suspend fun get(
        offset: UInt?, length: UInt?, headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBigMapResponse =
        httpClient.get(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                offset?.let { add("offset" to it.toString()) }
                length?.let { add("length" to it.toString()) }
            },
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )

    override operator fun invoke(scriptExpr: ScriptExprHash): Block.Context.BigMaps.BigMap.Value = BlockContextBigMapsBigMapValueClient(baseUrl, scriptExpr, httpClient)
}

private class BlockContextBigMapsBigMapValueClient(parentUrl: String, scriptExpr: ScriptExprHash, private val httpClient: HttpClient) : Block.Context.BigMaps.BigMap.Value {
    private val baseUrl: String = /* ../<block_id>/context/big_maps/<big_map_id>/<script_expr> */ "$parentUrl/${scriptExpr.base58}"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBigMapValueResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextConstantsClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Constants {
    private val baseUrl: String = /* ../<block_id>/context/constants */ "$parentUrl/constants"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetConstantsResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout) // TODO: caching?
}

private class BlockContextContractsClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts {
    private val baseUrl: String = /* ../<block_id>/context/contracts */ "$parentUrl/contracts"

    override operator fun invoke(contractId: Address): Block.Context.Contracts.Contract = BlockContextContractsContractClient(baseUrl, contractId, httpClient)
}

private class BlockContextContractsContractClient(parentUrl: String, contractId: Address, private val httpClient: HttpClient) : Block.Context.Contracts.Contract {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id> */ "$parentUrl/${contractId.base58}"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractDetailsResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)

    override val balance: Block.Context.Contracts.Contract.Balance by lazy { BlockContextContractsContractBalanceClient(baseUrl, httpClient) }
    override val counter: Block.Context.Contracts.Contract.Counter by lazy { BlockContextContractsContractCounterClient(baseUrl, httpClient) }
    override val delegate: Block.Context.Contracts.Contract.Delegate by lazy { BlockContextContractsContractDelegateClient(baseUrl, httpClient) }
    override val entrypoints: Block.Context.Contracts.Contract.Entrypoints by lazy { BlockContextContractsContractEntrypointsClient(baseUrl, httpClient) }
    override val managerKey: Block.Context.Contracts.Contract.ManagerKey by lazy { BlockContextContractsContractManagerKeyClient(baseUrl, httpClient) }
    override val script: Block.Context.Contracts.Contract.Script by lazy { BlockContextContractsContractScriptClient(baseUrl, httpClient) }
    override val singleSaplingGetDiff: Block.Context.Contracts.Contract.SingleSaplingGetDiff by lazy { BlockContextContractsContractSingleSaplingGetDiffClient(baseUrl, httpClient) }
    override val storage: Block.Context.Contracts.Contract.Storage by lazy { BlockContextContractsContractStorageClient(baseUrl, httpClient) }
}

private class BlockContextContractsContractBalanceClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.Balance {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/balance */ "$parentUrl/balance"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractBalanceResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextContractsContractCounterClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.Counter {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/counter */ "$parentUrl/counter"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractCounterResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextContractsContractDelegateClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.Delegate {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/delegate */ "$parentUrl/delegate"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractDelegateResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextContractsContractEntrypointsClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.Entrypoints {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/entrypoints */ "$parentUrl/entrypoints"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractEntrypointsResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)

    override operator fun invoke(string: String): Block.Context.Contracts.Contract.Entrypoints.Entrypoint = BlockContextContractsContractEntrypointsEntrypointClient(baseUrl, string, httpClient)
}

private class BlockContextContractsContractEntrypointsEntrypointClient(parentUrl: String, string: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.Entrypoints.Entrypoint {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/entrypoints/<string> */ "$parentUrl/$string"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractEntrypointResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextContractsContractManagerKeyClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.ManagerKey {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/manager_key */ "$parentUrl/manager_key"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractManagerKeyResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextContractsContractScriptClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.Script {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/script */ "$parentUrl/script"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractScriptResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)

    override val normalized: Block.Context.Contracts.Contract.Script.Normalized by lazy { BlockContextContractsContractScriptNormalizedClient(baseUrl, httpClient) }
}

private class BlockContextContractsContractScriptNormalizedClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.Script.Normalized {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/script/normalized */ "$parentUrl/normalized"

    override suspend fun post(
        unparsingMode: RpcScriptParsing,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractNormalizedScriptResponse =
        httpClient.post(
            baseUrl,
            "/",
            headers,
            request = GetContractNormalizedScriptRequest(unparsingMode),
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}

private class BlockContextContractsContractSingleSaplingGetDiffClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.SingleSaplingGetDiff {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/single_sapling_get_diff */ "$parentUrl/single_sapling_get_diff"

    override suspend fun get(
        commitmentOffset: ULong?,
        nullifierOffset: ULong?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractSaplingStateDiffResponse =
        httpClient.get(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                commitmentOffset?.let { add("offset_commitment" to it.toString()) }
                nullifierOffset?.let { add("offset_nullifier" to it.toString()) }
            },
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}

private class BlockContextContractsContractStorageClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.Storage {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/storage */ "$parentUrl/storage"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractStorageResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)

    override val normalized: Block.Context.Contracts.Contract.Storage.Normalized by lazy { BlockContextContractsContractStorageNormalizedClient(baseUrl, httpClient) }
}

private class BlockContextContractsContractStorageNormalizedClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Contracts.Contract.Storage.Normalized {
    private val baseUrl: String = /* ../<block_id>/context/contracts/<contract_id>/storage/normalized */ "$parentUrl/normalized"

    override suspend fun post(
        unparsingMode: RpcScriptParsing,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetContractNormalizedStorageResponse =
        httpClient.post(
            baseUrl,
            "/",
            headers,
            request = GetContractNormalizedStorageRequest(unparsingMode),
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}

private class BlockContextDelegatesClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates {
    private val baseUrl: String = /* ../<block_id>/context/delegates */ "$parentUrl/delegates"

    override operator fun invoke(publicKeyHash: PublicKeyHash): Block.Context.Delegates.Delegate = BlockContextDelegatesDelegateClient(baseUrl, publicKeyHash, httpClient)
}

private class BlockContextDelegatesDelegateClient(parentUrl: String, publicKeyHash: PublicKeyHash, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh> */ "$parentUrl/${publicKeyHash.base58}"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateDetailsResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)

    override val currentFrozenDeposits: Block.Context.Delegates.Delegate.CurrentFrozenDeposits by lazy { BlockContextDelegatesDelegateCurrentFrozenDeposits(baseUrl, httpClient) }
    override val deactivated: Block.Context.Delegates.Delegate.Deactivated by lazy { BlockContextDelegatesDelegateDeactivated(baseUrl, httpClient) }
    override val delegatedBalance: Block.Context.Delegates.Delegate.DelegatedBalance by lazy { BlockContextDelegatesDelegateDelegatedBalance(baseUrl, httpClient) }
    override val delegatedContracts: Block.Context.Delegates.Delegate.DelegatedContracts by lazy { BlockContextDelegatesDelegateDelegatedContracts(baseUrl, httpClient) }
    override val frozenDeposits: Block.Context.Delegates.Delegate.FrozenDeposits by lazy { BlockContextDelegatesDelegateFrozenDeposits(baseUrl, httpClient) }
    override val frozenDepositsLimit: Block.Context.Delegates.Delegate.FrozenDepositsLimit by lazy { BlockContextDelegatesDelegateFrozenDepositsLimit(baseUrl, httpClient) }
    override val fullBalance: Block.Context.Delegates.Delegate.FullBalance by lazy { BlockContextDelegatesDelegateFullBalance(baseUrl, httpClient) }
    override val gracePeriod: Block.Context.Delegates.Delegate.GracePeriod by lazy { BlockContextDelegatesDelegateGracePeriod(baseUrl, httpClient) }
    override val participation: Block.Context.Delegates.Delegate.Participation by lazy { BlockContextDelegatesDelegateParticipation(baseUrl, httpClient) }
    override val stakingBalance: Block.Context.Delegates.Delegate.StakingBalance by lazy { BlockContextDelegatesDelegateStakingBalance(baseUrl, httpClient) }
    override val votingPower: Block.Context.Delegates.Delegate.VotingPower by lazy { BlockContextDelegatesDelegateVotingPower(baseUrl, httpClient) }
}

private class BlockContextDelegatesDelegateCurrentFrozenDeposits(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.CurrentFrozenDeposits {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/current_frozen_deposits */ "$parentUrl/current_frozen_deposits"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateCurrentFrozenDepositsResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextDelegatesDelegateDeactivated(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.Deactivated {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/deactivated */ "$parentUrl/deactivated"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateDeactivatedStatusResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextDelegatesDelegateDelegatedBalance(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.DelegatedBalance {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/delegated_balance */ "$parentUrl/delegated_balance"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateDelegatedBalanceResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextDelegatesDelegateDelegatedContracts(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.DelegatedContracts {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/delegated_contracts */ "$parentUrl/delegated_contracts"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateDelegatedContractsResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextDelegatesDelegateFrozenDeposits(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.FrozenDeposits {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/frozen_deposits */ "$parentUrl/frozen_deposits"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateFrozenDepositsResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextDelegatesDelegateFrozenDepositsLimit(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.FrozenDepositsLimit {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/frozen_deposits_limit */ "$parentUrl/frozen_deposits_limit"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateFrozenDepositsLimitResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextDelegatesDelegateFullBalance(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.FullBalance {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/full_balance */ "$parentUrl/full_balance"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateFullBalanceResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextDelegatesDelegateGracePeriod(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.GracePeriod {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/grace_period */ "$parentUrl/grace_period"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateGracePeriodResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextDelegatesDelegateParticipation(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.Participation {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/participation */ "$parentUrl/participation"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateParticipationResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextDelegatesDelegateStakingBalance(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.StakingBalance {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/staking_balance */ "$parentUrl/staking_balance"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateStakingBalanceResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockContextDelegatesDelegateVotingPower(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Delegates.Delegate.VotingPower {
    private val baseUrl: String = /* ../<block_id>/context/delegates/<pkh>/voting_power */ "$parentUrl/voting_power"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetDelegateVotingPowerResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}


private class BlockContextSapling(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Sapling {
    private val baseUrl: String = /* ../<block_id>/context/sapling */ "$parentUrl/sapling"

    override operator fun invoke(stateId: String): Block.Context.Sapling.State = BlockContextSaplingStateClient(baseUrl, stateId, httpClient)
}

private class BlockContextSaplingStateClient(parentUrl: String, stateId: String, private val httpClient: HttpClient) : Block.Context.Sapling.State {
    private val baseUrl: String = /* ../<block_id>/context/sapling/<sapling_state_id> */ "$parentUrl/$stateId"

    override val getDiff: Block.Context.Sapling.State.GetDiff by lazy { BlockContextSaplingStateGetDiffClient(baseUrl, httpClient) }
}

private class BlockContextSaplingStateGetDiffClient(parentUrl: String, private val httpClient: HttpClient) : Block.Context.Sapling.State.GetDiff {
    private val baseUrl: String = /* ../<block_id>/context/sapling/<sapling_state_id>/get_diff */ "$parentUrl/get_diff"

    override suspend fun get(
        commitmentOffset: ULong?,
        nullifierOffset: ULong?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetSaplingStateDiffResponse =
        httpClient.get(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                commitmentOffset?.let { add("offset_commitment" to it.toString()) }
                nullifierOffset?.let { add("offset_nullifier" to it.toString()) }
            },
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}

private class BlockHeaderClient(parentUrl: String, private val httpClient: HttpClient) : Block.Header {
    private val baseUrl: String = /* ../<block_id>/header */ "$parentUrl/header"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBlockHeaderResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class BlockHelpersClient(parentUrl: String, private val httpClient: HttpClient) : Block.Helpers {
    private val baseUrl: String = /* ../<block_id>/helpers */ "$parentUrl/helpers"

    override val preapply: Block.Helpers.Preapply by lazy { BlockHelpersPreapplyClient(baseUrl, httpClient) }
    override val scripts: Block.Helpers.Scripts by lazy { BlockHelpersScriptsClient(baseUrl, httpClient) }
}

private class BlockHelpersPreapplyClient(parentUrl: String, private val httpClient: HttpClient) : Block.Helpers.Preapply {
    private val baseUrl: String = /* ../<block_id>/helpers/preapply */ "$parentUrl/preapply"

    override val operations: Block.Helpers.Preapply.Operations by lazy { BlockHelpersPreapplyOperationsClient(baseUrl, httpClient) }

}

private class BlockHelpersPreapplyOperationsClient(parentUrl: String, private val httpClient: HttpClient) : Block.Helpers.Preapply.Operations {
    private val baseUrl: String = /* ../<block_id>/helpers/preapply/operations */ "$parentUrl/operations"

    override suspend fun post(
        operations: List<RpcApplicableOperation>,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): PreapplyOperationsResponse =
        httpClient.post(
            baseUrl,
            "/",
            headers,
            request = PreapplyOperationsRequest(operations),
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}

private class BlockHelpersScriptsClient(parentUrl: String, private val httpClient: HttpClient) : Block.Helpers.Scripts {
    private val baseUrl: String = /* ../<block_id>/helpers/scripts */ "$parentUrl/scripts"

    override val runOperation: Block.Helpers.Scripts.RunOperation by lazy { BlockHelpersScriptsRunOperationClient(baseUrl, httpClient) }
}

private class BlockHelpersScriptsRunOperationClient(parentUrl: String, private val httpClient: HttpClient) : Block.Helpers.Scripts.RunOperation {
    private val baseUrl: String = /* ../<block_id>/helpers/scripts/run_operation */ "$parentUrl/run_operation"

    override suspend fun post(
        operation: RpcRunnableOperation,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): RunOperationResponse =
        httpClient.post(
            baseUrl,
            "/",
            headers,
            request = RunOperationRequest(operation),
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}

private class BlockOperationsClient(parentUrl: String, private val httpClient: HttpClient) : Block.Operations {
    private val baseUrl: String = /* ../<block_id>/operations */ "$parentUrl/operations"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetBlockOperationsResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}