package it.airgap.tezos.rpc.shell.config

import it.airgap.tezos.rpc.type.history.RpcHistoryMode
import it.airgap.tezos.rpc.type.protocol.RpcUserActivatedProtocolOverride
import it.airgap.tezos.rpc.type.protocol.RpcUserActivatedUpgrade
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- /config/history_mode --

/**
 * Response from [`GET /config/history_mod`](https://tezos.gitlab.io/shell/rpc.html#get-config-history-mode).
 */
@Serializable
public data class GetHistoryModeResponse(@SerialName("history_mode") public val historyMode: RpcHistoryMode)

// -- /config/logging --

/**
 * Request for [`PUT /config/logging`](https://tezos.gitlab.io/shell/rpc.html#put-config-logging).
 */
@Serializable
public data class SetLoggingRequest(@SerialName("active_sinks") public val activeSinks: List<String>)

/**
 * Response from [`PUT /config/logging`](https://tezos.gitlab.io/shell/rpc.html#put-config-logging).
 */
public typealias SetLoggingResponse = Unit

// -- /config/network/user_activated_protocol_overrides --

/**
 * Response from [`GET /config/network/user_activated_protocol_overrides`](https://tezos.gitlab.io/shell/rpc.html#get-config-network-user-activated-protocol-overrides).
 */
@Serializable
@JvmInline
public value class GetUserActivatedProtocolOverridesResponse(public val overrides: List<RpcUserActivatedProtocolOverride>)

// -- /config/network/user_activated_upgrades --

/**
 * Response from [`GET /config/network/user_activated_upgrades`](https://tezos.gitlab.io/shell/rpc.html#get-config-network-user-activated-upgrades).
 */
@Serializable
@JvmInline
public value class GetUserActivatedUpgradesResponse(public val upgrades: List<RpcUserActivatedUpgrade>)