package it.airgap.tezos.rpc.type.contract

import kotlinx.serialization.Serializable

// -- RpcUnreachableEntrypoint --

@Serializable
public data class RpcUnreachableEntrypoint(public val path: String)
