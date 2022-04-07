package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.type.RpcAcl
import it.airgap.tezos.rpc.type.RpcIPAddress
import it.airgap.tezos.rpc.type.p2p.RpcConnection
import it.airgap.tezos.rpc.type.p2p.RpcConnectionPoolEvent
import it.airgap.tezos.rpc.type.p2p.RpcPeer
import it.airgap.tezos.rpc.type.p2p.RpcPeerPoolEvent
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ==== /network ====

// -- /connections --

@Serializable
@JvmInline
public value class GetConnectionsResponse(public val connections: List<RpcConnection>)

// -- /connections/<peer_id> --

@Serializable
@JvmInline
public value class GetConnectionResponse(public val connection: RpcConnection)

public typealias CloseConnectionResponse = Unit

// -- /greylist --

public typealias ClearGreylistResponse = Unit

// -- /greylist/ips --

@Serializable
public data class GetGreylistedIPsResponse(public val ips: List<RpcIPAddress>, @SerialName("not_reliable_since") val notReliableSince: Timestamp)

// -- /greylist/peers --

@Serializable
@JvmInline
public value class GetLastGreylistedPeersResponse(public val peers: List<@Contextual CryptoboxPublicKeyHash>)

// -- /log --

@Serializable
@JvmInline
public value class GetLogResponse(public val events: List<RpcConnectionPoolEvent>)

// -- /peers --

@Serializable
@JvmInline
public value class GetPeersResponse(public val peers: List<Pair<@Contextual CryptoboxPublicKeyHash, RpcPeer>>)

// -- /peers/<peer_id> --

@Serializable
@JvmInline
public value class GetPeerResponse(public val peer: RpcPeer)

@Serializable
@JvmInline
public value class ChangePeerPermissionRequest(public val acl: RpcAcl?)

@Serializable
@JvmInline
public value class ChangePeerPermissionResponse(public val peer: RpcPeer)

// -- /peers/<peer_id>/banned --

@Serializable
@JvmInline
public value class GetPeerBannedStatusResponse(public val isBanned: Boolean)

// -- /peers/<peer_id>/log

@Serializable
@JvmInline
public value class GetPeerEventsResponse(public val events: List<RpcPeerPoolEvent>)
