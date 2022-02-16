package it.airgap.tezos.rpc.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RpcUserActivatedProtocolOverride<ProtocolHash>(
    @SerialName("replaced_protocol") public val replacedProtocol: ProtocolHash,
    @SerialName("replacement_protocol") public val replacementProtocol: ProtocolHash,
)