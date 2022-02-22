package it.airgap.tezos.rpc.type

import kotlinx.serialization.Serializable

// -- RpcProtocolHash --

internal typealias TransitionalRpcProtocolHash = Unistring

// -- RpcProtocolComponent --

@Serializable
public data class RpcProtocolComponent(
    public val name: String,
    public val implementation: String,
    public val `interface`: String? = null,
)