package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.active.data.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient

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
            }
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
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/counter", headers)

    override suspend fun getContractDelegate(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader>,
    ): GetContractDelegateResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/delegate", headers)

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
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/script", headers)

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
            }
        )

    override suspend fun getContractStorage(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader>,
    ): GetContractStorageResponse =
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/contracts/${contractId.base58}/storage", headers)
}