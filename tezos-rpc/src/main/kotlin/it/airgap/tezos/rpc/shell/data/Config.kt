package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.internal.serializer.RpcHistoryModeSerializer
import it.airgap.tezos.rpc.type.RpcProtocolHash
import it.airgap.tezos.rpc.type.RpcUnistring
import kotlinx.serialization.Contextual
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
public data class RpcUserActivatedProtocolOverride<T>(
    @SerialName("replaced_protocol") public val replacedProtocol: T,
    @SerialName("replacement_protocol") public val replacementProtocol: T,
)

@Serializable
@JvmInline
internal value class GetUserActivatedProtocolOverridesTransitionalResponse(val overrides: List<RpcUserActivatedProtocolOverride<RpcProtocolHash>>)

@Serializable
@JvmInline
public value class GetUserActivatedProtocolOverridesResponse(public val overrides: List<RpcUserActivatedProtocolOverride<@Contextual ProtocolHash>>)

// -- /network/user_activated_upgrades --

@Serializable
public data class RpcUserActivatedUpgrade<T>(
    public val level: Int,
    @SerialName("replacement_protocol") public val replacementProtocol: T,
)

@Serializable
@JvmInline
internal value class GetUserActivatedUpgradesTransitionalResponse(val upgrades: List<RpcUserActivatedUpgrade<RpcProtocolHash>>)

@Serializable
@JvmInline
public value class GetUserActivatedUpgradesResponse(public val upgrades: List<RpcUserActivatedUpgrade<@Contextual ProtocolHash>>)