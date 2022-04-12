package it.airgap.tezos.rpc.type.contract

import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.Serializable

// -- RpcParameters --

@Serializable
public data class RpcParameters(
    public val entrypoint: String,
    public val value: MichelineNode,
)
