package it.airgap.tezos.rpc.type

import kotlinx.serialization.Serializable

@Serializable
public data class RpcProtocolComponent(
    public val name: String,
    public val implementation: String,
    public val `interface`: String? = null,
)