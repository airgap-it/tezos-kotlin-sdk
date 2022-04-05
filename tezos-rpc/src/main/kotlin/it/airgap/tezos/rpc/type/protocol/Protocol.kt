package it.airgap.tezos.rpc.type.protocol

import kotlinx.serialization.Serializable

// -- RpcProtocolComponent --

@Serializable
public data class RpcProtocolComponent(
    public val name: String,
    public val implementation: String,
    public val `interface`: String? = null,
)