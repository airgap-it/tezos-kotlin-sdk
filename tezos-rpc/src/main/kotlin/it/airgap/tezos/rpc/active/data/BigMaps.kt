package it.airgap.tezos.rpc.active.data

import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.Serializable

// ==== ../<block_id>/context/big_maps ====

// -- /<big_map_id> --

@Serializable
@JvmInline
public value class GetBigMapValuesResponse(public val values: List<MichelineNode>)

// -- /<big_map_id>/<script_expr> --

@Serializable
@JvmInline
public value class GetBigMapValueResponse(public val value: MichelineNode)