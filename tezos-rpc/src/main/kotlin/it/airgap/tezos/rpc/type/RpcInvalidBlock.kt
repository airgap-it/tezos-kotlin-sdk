package it.airgap.tezos.rpc.type

import kotlinx.serialization.Serializable

@Serializable
public data class RpcInvalidBlock<BlockHash>(
    public val block: BlockHash,
    public val level: Int,
    public val errors: List<RpcError>,
)