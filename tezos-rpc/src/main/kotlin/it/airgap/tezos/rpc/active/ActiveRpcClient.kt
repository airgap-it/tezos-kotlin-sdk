package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.active.data.GetBigMapValueResponse
import it.airgap.tezos.rpc.active.data.GetBigMapValuesResponse
import it.airgap.tezos.rpc.active.data.GetBlockResponse
import it.airgap.tezos.rpc.active.data.GetConstants
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

    override suspend fun getConstants(chainId: String, blockId: String, headers: List<HttpHeader>): GetConstants =
        // TODO: caching?
        httpClient.get(nodeUrl, "/chains/$chainId/blocks/$blockId/context/constants", headers)
}