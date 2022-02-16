package it.airgap.tezos.rpc.data.shell

import it.airgap.tezos.rpc.internal.serializer.RpcHistoryModeSerializer
import it.airgap.tezos.rpc.type.RpcUnistring
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ==== /config ====

// -- /history_mode --

@Serializable(with = RpcHistoryModeSerializer::class)
public sealed class RpcHistoryMode {
    public object Archive : RpcHistoryMode()

    @Serializable
    public data class Full(public val full: RpcAdditionalCycles? = null) : RpcHistoryMode()

    @Serializable
    public data class Rolling(public val rolling: RpcAdditionalCycles? = null) : RpcHistoryMode()
}

@Serializable
public data class RpcAdditionalCycles(@SerialName("additional_cycles") public val additionalCycles: UInt)

@Serializable
public data class GetHistoryModeResponse(@SerialName("history_mode") public val historyMode: RpcHistoryMode)

// -- /logging --

@Serializable
public data class SetLoggingRequest(@SerialName("active_sinks") public val activeSinks: RpcUnistring)

public typealias SetLoggingResponse = Unit

// -- /network/user_activated_protocol_overrides --

@Serializable
public data class RpcUserActivatedProtocolOverride(
    @SerialName("replaced_protocol") public val replacedProtocol: RpcUnistring,
    @SerialName("replacement_protocol") public val replacementProtocol: RpcUnistring,
)

@Serializable
public data class GetUserActivatedProtocolOverridesResponse(public val protocolOverrides: List<RpcUserActivatedProtocolOverride>)

// -- /network/user_activated_upgrades --

@Serializable
public data class RpcUserActivatedUpgrade(
    public val level: Int,
    @SerialName("replacement_protocol") public val replacementProtocol: RpcUnistring,
)

@Serializable
public data class GetUserActivatedUpgradesResponse(public val upgrades: List<RpcUserActivatedUpgrade>)