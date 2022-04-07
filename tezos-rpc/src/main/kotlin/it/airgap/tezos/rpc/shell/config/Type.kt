package it.airgap.tezos.rpc.shell.config

import it.airgap.tezos.rpc.type.RpcHistoryMode
import it.airgap.tezos.rpc.type.protocol.RpcUserActivatedProtocolOverride
import it.airgap.tezos.rpc.type.protocol.RpcUserActivatedUpgrade
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- /config/history_mode --

@Serializable
public data class GetHistoryModeResponse(@SerialName("history_mode") public val historyMode: RpcHistoryMode)

// -- /config/logging --

@Serializable
public data class SetLoggingRequest(@SerialName("active_sinks") public val activeSinks: String)

public typealias SetLoggingResponse = Unit

// -- /config/network/user_activated_protocol_overrides --

@Serializable
@JvmInline
public value class GetUserActivatedProtocolOverridesResponse(public val overrides: List<RpcUserActivatedProtocolOverride>)

// -- /config/network/user_activated_upgrades --

@Serializable
@JvmInline
public value class GetUserActivatedUpgradesResponse(public val upgrades: List<RpcUserActivatedUpgrade>)