package it.airgap.tezos.rpc.active.data

import it.airgap.tezos.rpc.type.block.RpcBlock
import kotlinx.serialization.Serializable

// ==== /<block_id> ====

@Serializable
@JvmInline
public value class GetBlockResponse(public val block: RpcBlock)