package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.PublicKeyHashEncoded
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.active.data.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.type.operation.RpcApplicableOperation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

internal class ActiveRpcClient(
    private val nodeUrl: String,
    private val httpClient: HttpClient,
) : ActiveRpc {

    // ==== RPC (https://tezos.gitlab.io/active/rpc.html) ====

    // -- ../ --

    override suspend fun getBlock(chainId: String, blockId: String, headers: List<HttpHeader>): GetBlockResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId", headers)

    // -- ../<block_id>/context/big_maps --

    override suspend fun getBigMapValues(
        chainId: String,
        blockId: String,
        bigMapId: String,
        offset: UInt?,
        length: UInt?,
        headers: List<HttpHeader>,
    ): GetBigMapValuesResponse =
        httpClient.get(
            nodeUrl,
            "/chains/$chainId/blocks/$blockId/context/big_maps/$bigMapId",
            headers,
            parameters = buildList {
                offset?.let { add("offset" to it.toString()) }
                length?.let { add("length" to it.toString()) }
            },
        )

    override suspend fun getBigMapValue(
        chainId: String,
        blockId: String,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader>,
    ): GetBigMapValueResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/big_maps/$bigMapId/${key.base58}", headers)

    // -- ../<block_id>/context/constants --

    override suspend fun getConstants(chainId: String, blockId: String, headers: List<HttpHeader>): GetConstantsResponse =
        // TODO: caching?
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/constants", headers)

    // -- ../<block_id>/context/contracts --

    override suspend fun getContractDetails(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader>,
    ): GetContractDetailsResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}", headers)

    override suspend fun getContractBalance(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader>,
    ): GetContractBalanceResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/balance", headers)

    override suspend fun getContractCounter(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader>,
    ): GetContractCounterResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/counter", headers) // TODO: handle error when resource is missing

    override suspend fun getContractDelegate(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader>,
    ): GetContractDelegateResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/delegate", headers) // TODO: handle error when resource is missing

    override suspend fun getContractEntrypoints(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader>,
    ): GetContractEntrypointsResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/entrypoints", headers)

    override suspend fun getContractEntrypointType(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        entrypoint: String,
        headers: List<HttpHeader>,
    ): GetContractEntrypointTypeResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/entrypoints/$entrypoint", headers)

    override suspend fun getContractManager(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader>,
    ): GetContractManagerResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/manager_key", headers)

    override suspend fun getContractScript(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader>,
    ): GetContractScriptResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/script", headers) // TODO: handle error when resource is missing

    override suspend fun getContractSaplingStateDiff(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        commitmentOffset: ULong?,
        nullifierOffset: ULong?,
        headers: List<HttpHeader>,
    ): GetContractSaplingStateDiffResponse =
        httpClient.get(
            nodeUrl,
            "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/single_sapling_get_diff",
            headers,
            parameters = buildList {
                commitmentOffset?.let { add("offset_commitment" to it.toString()) }
                nullifierOffset?.let { add("offset_nullifier" to it.toString()) }
            },
        )

    override suspend fun getContractStorage(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader>,
    ): GetContractStorageResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/storage", headers)

    // -- ../<block_id>/context/delegates --

    override suspend fun getDelegateDetails(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateDetailsResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}", headers)

    override suspend fun getDelegateCurrentFrozenDeposits(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateCurrentFrozenDepositsResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/current_frozen_deposits", headers)

    override suspend fun isDelegateDeactivated(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateDeactivatedStatusResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/deactivated", headers)

    override suspend fun getDelegateDelegatedBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateDelegatedBalanceResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/delegated_balance", headers)

    override suspend fun getDelegateDelegatedContracts(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateDelegatedContractsResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/delegated_contracts", headers)

    override suspend fun getDelegateFrozenDeposits(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateFrozenDepositsResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/frozen_deposits", headers)

    override suspend fun getDelegateFrozenDepositsLimit(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateFrozenDepositsLimitResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/frozen_deposits_limit", headers)

    override suspend fun getDelegateFullBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateFullBalanceResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/full_balance", headers)

    override suspend fun getDelegateGracePeriod(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateGracePeriodResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/grace_period", headers)

    override suspend fun getDelegateParticipation(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateParticipationResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/participation", headers)

    override suspend fun getDelegateStakingBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateStakingBalanceResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/staking_balance", headers)

    override suspend fun getDelegateVotingPower(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader>,
    ): GetDelegateVotingPowerResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${publicKeyHash.base58}/voting_power", headers)

    // -- ../<block_id>/context/sapling --

    override suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: String,
        stateId: String,
        commitmentOffset: ULong?,
        nullifierOffset: ULong?,
        headers: List<HttpHeader>,
    ): GetSaplingStateDiffResponse =
        httpClient.get(
            nodeUrl,
            "/chains/$chainId/blocks/$blockId/context/sapling/$stateId/get_diff",
            headers,
            parameters = buildList {
                commitmentOffset?.let { add("offset_commitment" to it.toString()) }
                nullifierOffset?.let { add("offset_nullifier" to it.toString()) }
            },
        )

    // -- ../<block_id>/header --

    override suspend fun getBlockHeader(
        chainId: String,
        blockId: String,
        headers: List<HttpHeader>,
    ): GetBlockHeaderResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/header", headers)

    // -- ../<block_id>/helpers --

    override suspend fun preapplyOperations(
        chainId: String,
        blockId: String,
        operations: List<RpcApplicableOperation>,
        headers: List<HttpHeader>,
    ): PreapplyOperationsResponse =
        httpClient.post(
            nodeUrl,
            "/chains/$chainId/blocks/$blockId/helpers/preapply/operations",
            headers,
            request = PreapplyOperationsRequest(operations),
        )

    override suspend fun runOperation(
        chainId: String,
        blockId: String,
        operation: RpcRunnableOperation,
        headers: List<HttpHeader>,
    ): RunOperationResponse =
        httpClient.post(
            nodeUrl,
            "/chains/$chainId/blocks/$blockId/helpers/scripts/run_operation",
            headers,
            request = RunOperationRequest(operation)
        )
}