package it.airgap.tezos.rpc.active

import it.airgap.tezos.rpc.http.HttpHeader

// https://tezos.gitlab.io/active/rpc.html
public interface ActiveRpc {
    public fun getBlock(blockId: String, headers: List<HttpHeader> = emptyList())
}