package it.airgap.tezos.rpc.active.data

import it.airgap.tezos.rpc.type.constants.RpcConstants
import kotlinx.serialization.Serializable

// ==== ../<block_id>/context/constants ====

// -- / --

@Serializable
@JvmInline
public value class GetConstantsResponse(public val constants: RpcConstants)
