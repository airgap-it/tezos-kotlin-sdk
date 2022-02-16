package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.type.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ==== /config ====

// -- /history_mode --

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
@JvmInline
internal value class GetUserActivatedProtocolOverridesTransitionalResponse(val overrides: List<RpcUserActivatedProtocolOverride<RpcProtocolHash>>)

@Serializable
@JvmInline
public value class GetUserActivatedProtocolOverridesResponse(public val overrides: List<RpcUserActivatedProtocolOverride<@Contextual ProtocolHash>>)

// -- /network/user_activated_upgrades --

@Serializable
@JvmInline
internal value class GetUserActivatedUpgradesTransitionalResponse(val upgrades: List<RpcUserActivatedUpgrade<RpcProtocolHash>>)

@Serializable
@JvmInline
public value class GetUserActivatedUpgradesResponse(public val upgrades: List<RpcUserActivatedUpgrade<@Contextual ProtocolHash>>)