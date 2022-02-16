package it.airgap.tezos.rpc.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RpcUserActivatedUpgrade<ProtocolHash>(
    public val level: Int,
    @SerialName("replacement_protocol") public val replacementProtocol: ProtocolHash,
)