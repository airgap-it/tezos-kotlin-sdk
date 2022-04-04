package it.airgap.tezos.rpc.active

import it.airgap.tezos.rpc.active.data.GetBlockResponse
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
}