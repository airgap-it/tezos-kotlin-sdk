package it.airgap.tezos.rpc.active.data

import it.airgap.tezos.rpc.type.sapling.RpcSaplingStateDiff
import kotlinx.serialization.Serializable

// ==== ../<block_id>/context/sapling ====

// -- /<sapling_state_id>/get_diff

@Serializable
@JvmInline
public value class GetSaplingStateDiffResponse(public val stateDiff: RpcSaplingStateDiff)