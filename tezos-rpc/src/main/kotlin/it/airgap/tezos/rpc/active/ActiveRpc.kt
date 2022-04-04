package it.airgap.tezos.rpc.active

import it.airgap.tezos.rpc.active.data.GetBlockResponse
import it.airgap.tezos.rpc.http.HttpHeader

// https://tezos.gitlab.io/active/rpc.html
public interface ActiveRpc {

    // -- ../ --

    public suspend fun getBlock(chainId: String, blockId: String, headers: List<HttpHeader> = emptyList()): GetBlockResponse
}