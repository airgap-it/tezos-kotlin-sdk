package it.airgap.tezos.rpc.type

import it.airgap.tezos.core.type.encoded.ProtocolHash
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcUserActivatedProtocolOverride --

@Serializable
public data class GenericRpcUserActivatedProtocolOverride<ProtocolHash>(
    @SerialName("replaced_protocol") public val replacedProtocol: ProtocolHash,
    @SerialName("replacement_protocol") public val replacementProtocol: ProtocolHash,
)
internal typealias TransitionalRpcUserActivatedProtocolOverride = GenericRpcUserActivatedProtocolOverride<Unistring>
public typealias RpcUserActivatedProtocolOverride = GenericRpcUserActivatedProtocolOverride<@Contextual ProtocolHash>

// -- RpcUserActivatedUpgrade --

@Serializable
public data class GenericRpcUserActivatedUpgrade<ProtocolHash>(
    public val level: Int,
    @SerialName("replacement_protocol") public val replacementProtocol: ProtocolHash,
)
internal typealias TransitionalRpcUserActivatedUpgrade = GenericRpcUserActivatedUpgrade<Unistring>
public typealias RpcUserActivatedUpgrade = GenericRpcUserActivatedUpgrade<@Contextual ProtocolHash>