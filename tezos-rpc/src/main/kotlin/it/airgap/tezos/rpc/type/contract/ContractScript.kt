package it.airgap.tezos.rpc.type.contract

import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.Serializable

// -- RpcScript --

@Serializable
public data class RpcScript(
    public val code: MichelineNode,
    public val storage: MichelineNode,
)