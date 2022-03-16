package it.airgap.tezos.rpc.type

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.BlockHash
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// -- RpcOperation --

@Serializable
public data class RpcOperation(
    public val branch: @Contextual BlockHash,
    public val data: @Contextual HexString,
)