package it.airgap.tezos.rpc.shell.network

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.type.RpcIPAddress
import it.airgap.tezos.rpc.type.network.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- /network/connections --

@Serializable
@JvmInline
public value class GetConnectionsResponse(public val connections: List<RpcConnection>)

// -- /network/connections/<peer_id> --

@Serializable
@JvmInline
public value class GetConnectionResponse(public val connection: RpcConnection)

public typealias CloseConnectionResponse = Unit

// -- /network/greylist --

public typealias ClearGreylistResponse = Unit

// -- /network/greylist/ips --

@Serializable
public data class GetGreylistedIPsResponse(public val ips: List<RpcIPAddress>, @SerialName("not_reliable_since") val notReliableSince: Timestamp)

// -- /network/greylist/peers --

@Serializable
@JvmInline
public value class GetLastGreylistedPeersResponse(public val peers: List<@Contextual CryptoboxPublicKeyHash>)

// -- /network/log --

@Serializable
@JvmInline
public value class GetLogResponse(public val events: List<RpcConnectionPoolEvent>)

// -- /network/peers --

@Serializable
@JvmInline
public value class GetPeersResponse(public val peers: List<Pair<@Contextual CryptoboxPublicKeyHash, RpcPeer>>)

// -- /network/peers/<peer_id> --

@Serializable
@JvmInline
public value class GetPeerResponse(public val peer: RpcPeer)

@Serializable
@JvmInline
public value class ChangePeerPermissionRequest(public val acl: RpcAcl?)

@Serializable
@JvmInline
public value class ChangePeerPermissionResponse(public val peer: RpcPeer)

// -- /network/peers/<peer_id>/banned --

@Serializable
@JvmInline
public value class GetPeerBannedStatusResponse(public val isBanned: Boolean)

// -- /network/peers/<peer_id>/log

@Serializable
@JvmInline
public value class GetPeerEventsResponse(public val events: List<RpcPeerPoolEvent>)
