package it.airgap.tezos.rpc.type

import it.airgap.tezos.core.type.encoded.ProtocolHash
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcUserActivatedProtocolOverride --

@Serializable
public data class RpcUserActivatedProtocolOverride<ProtocolHash>(
    @SerialName("replaced_protocol") public val replacedProtocol: @Contextual ProtocolHash,
    @SerialName("replacement_protocol") public val replacementProtocol: @Contextual ProtocolHash,
)

// -- RpcUserActivatedUpgrade --

@Serializable
public data class RpcUserActivatedUpgrade(
    public val level: Int,
    @SerialName("replacement_protocol") public val replacementProtocol: @Contextual ProtocolHash,
)
