package it.airgap.tezos.rpc.active.data

import it.airgap.tezos.rpc.type.block.RpcFullBlockHeader
import kotlinx.serialization.Serializable

// ==== ../<block_id>/header ====

// -- / --

@Serializable
@JvmInline
public value class GetBlockHeaderResponse(public val header: RpcFullBlockHeader)