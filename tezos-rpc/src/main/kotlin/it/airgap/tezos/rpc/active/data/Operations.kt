package it.airgap.tezos.rpc.active.data

import it.airgap.tezos.rpc.type.operation.RpcOperation
import kotlinx.serialization.Serializable

// ==== ../<block_id>/operations ====

// -- / --

@Serializable
public data class GetBlockOperationsResponse(public val operations: List<List<RpcOperation>>)