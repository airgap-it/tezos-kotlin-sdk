package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.active.data.GetBigMapValueResponse
import it.airgap.tezos.rpc.active.data.GetBigMapValuesResponse
import it.airgap.tezos.rpc.active.data.GetBlockResponse
import it.airgap.tezos.rpc.http.HttpHeader

// https://tezos.gitlab.io/active/rpc.html
public interface ActiveRpc {

    // -- ../<block_id> --

    public suspend fun getBlock(chainId: String, blockId: String, headers: List<HttpHeader> = emptyList()): GetBlockResponse

    // -- ../<block_id>/context/big_maps --

    public suspend fun getBigMapValues(
        chainId: String,
        blockId: String,
        bigMapId: String,
        offset: UInt? = null,
        length: UInt? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValuesResponse

    public suspend fun getBigMapValue(
        chainId: String,
        blockId: String,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValueResponse
}