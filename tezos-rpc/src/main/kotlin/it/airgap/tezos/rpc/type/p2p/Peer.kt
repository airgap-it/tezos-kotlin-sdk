package it.airgap.tezos.rpc.type.p2p

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.rpc.type.TransitionalRpcTimestamp
import it.airgap.tezos.rpc.type.Unistring
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcCryptoboxPublicKeyHash --

internal typealias TransitionalRpcCryptoboxPublicKeyHash = Unistring

// -- RpcPeer --

@Serializable
public data class GenericRpcPeer<PeerMetadata, ConnectionId, Timestamp>(
    public val score: Long,
    public val trusted: Boolean,
    @SerialName("conn_metadata") public val connectionMetadata: RpcConnectionMetadata? = null,
    @SerialName("peer_metadata") public val peerMetadata: PeerMetadata,
    public val state: RpcPeerState,
    @SerialName("reachable_at") public val reachableAt: ConnectionId? = null,
    public val stat: RpcNetworkStat,
    @SerialName("last_failed_connection") public val lastFailedConnection: Pair<ConnectionId, Timestamp>? = null,
    @SerialName("last_rejected_connection") public val lastRejectedConnection: Pair<ConnectionId, Timestamp>? = null,
    @SerialName("last_established_connection") public val lastEstablishedConnection: Pair<ConnectionId, Timestamp>? = null,
    @SerialName("last_disconnection") public val lastDisconnection: Pair<ConnectionId, Timestamp>? = null,
    @SerialName("last_miss") public val lastSeen: Pair<ConnectionId, Timestamp>? = null,
    @SerialName("last_seen") public val lastMiss: Pair<ConnectionId, Timestamp>? = null,
)
internal typealias TransitionalRpcPeer = GenericRpcPeer<TransitionalRpcPeerMetadata, TransitionalRpcConnectionId, TransitionalRpcTimestamp>
public typealias RpcPeer = GenericRpcPeer<RpcPeerMetadata, RpcConnectionId, @Contextual Timestamp>

// -- RpcPeerState --

@Serializable
public enum class RpcPeerState {
    @SerialName("running") Running,
    @SerialName("accepted") Accepted,
    @SerialName("disconnected") Disconnected,
}