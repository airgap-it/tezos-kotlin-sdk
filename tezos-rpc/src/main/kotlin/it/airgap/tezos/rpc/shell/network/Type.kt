package it.airgap.tezos.rpc.shell.network

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.type.network.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- /network/connections --

/**
 * Response from [`GET /network/connections`](https://tezos.gitlab.io/shell/rpc.html#get-network-connections).
 */
@Serializable
@JvmInline
public value class GetConnectionsResponse(public val connections: List<RpcConnection>)

// -- /network/connections/<peer_id> --

/**
 * Response from [`GET /network/connections/<peer_id>`](https://tezos.gitlab.io/shell/rpc.html#get-network-connections-peer-id).
 */
@Serializable
@JvmInline
public value class GetConnectionResponse(public val connection: RpcConnection)

/**
 * Response from [`DELETE /network/connections/<peer_id>?[wait]`](https://tezos.gitlab.io/shell/rpc.html#delete-network-connections-peer-id).
 */
public typealias CloseConnectionResponse = Unit

// -- /network/greylist --

/**
 * Response from [`DELETE /network/greylist`](https://tezos.gitlab.io/shell/rpc.html#delete-network-greylist).
 */
public typealias ClearGreylistResponse = Unit

// -- /network/greylist/ips --

/**
 * Response from [`GET /network/greylist/ips`](https://tezos.gitlab.io/shell/rpc.html#get-network-greylist-ips).
 */
@Serializable
public data class GetGreylistedIpsResponse(public val ips: List<RpcIPAddress>, @SerialName("not_reliable_since") val notReliableSince: @Contextual Timestamp)

// -- /network/greylist/peers --

/**
 * Response from [`GET /network/greylist/peers`](https://tezos.gitlab.io/shell/rpc.html#get-network-greylist-peers).
 */
@Serializable
@JvmInline
public value class GetGreylistedPeersResponse(public val peers: List<@Contextual CryptoboxPublicKeyHash>)

// -- /network/log --

/**
 * Response from [`GET /network/log`](https://tezos.gitlab.io/shell/rpc.html#get-network-log).
 */
@Serializable
@JvmInline
public value class GetLogResponse(public val event: RpcConnectionPoolEvent)

// -- /network/peers --

/**
 * Response from [`GET /network/peers?(filter=<p2p.point.state_filter>)`](https://tezos.gitlab.io/shell/rpc.html#get-network-peers).
 */
@Serializable
@JvmInline
public value class GetPeersResponse(public val peers: List<@Contextual Pair<@Contextual CryptoboxPublicKeyHash, RpcPeer>>)

// -- /network/peers/<peer_id> --

/**
 * Response from [`GET /network/peers/<peer_id>`](https://tezos.gitlab.io/shell/rpc.html#get-network-peers-peer-id).
 */
@Serializable
@JvmInline
public value class GetPeerResponse(public val peer: RpcPeer)

/**
 * Request for [`PATCH /network/peers/<peer_id>`](https://tezos.gitlab.io/shell/rpc.html#patch-network-peers-peer-id).
 */
@Serializable
public data class ChangePeerPermissionRequest(public val acl: RpcAcl?)

/**
 * Response from [`PATCH /network/peers/<peer_id>`](https://tezos.gitlab.io/shell/rpc.html#patch-network-peers-peer-id).
 */
@Serializable
@JvmInline
public value class ChangePeerPermissionResponse(public val peer: RpcPeer)

// -- /network/peers/<peer_id>/banned --

/**
 * Response from [`GET /network/peers/<peer_id>/banned`](https://tezos.gitlab.io/shell/rpc.html#get-network-peers-peer-id-banned).
 */
@Serializable
@JvmInline
public value class GetPeerBannedStatusResponse(public val isBanned: Boolean)

// -- /network/peers/<peer_id>/log

/**
 * Response from [`GET /network/peers/<peer_id>/log?[monitor]`](https://tezos.gitlab.io/shell/rpc.html#get-network-peers-peer-id-log).
 */
@Serializable
@JvmInline
public value class GetPeerEventsResponse(public val events: List<RpcPeerPoolEvent>)
