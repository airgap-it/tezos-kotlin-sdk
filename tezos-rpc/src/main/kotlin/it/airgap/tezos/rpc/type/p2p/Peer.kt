package it.airgap.tezos.rpc.type.p2p

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.rpc.type.RpcIPAddress
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcPeer --

@Serializable
public data class RpcPeer(
    public val score: Long,
    public val trusted: Boolean,
    @SerialName("conn_metadata") public val connectionMetadata: RpcConnectionMetadata? = null,
    @SerialName("peer_metadata") public val peerMetadata: RpcPeerMetadata,
    public val state: RpcPeerState,
    @SerialName("reachable_at") public val reachableAt: RpcConnectionId? = null,
    public val stat: RpcNetworkStat,
    @SerialName("last_failed_connection") public val lastFailedConnection: Pair<RpcConnectionId, @Contextual Timestamp>? = null,
    @SerialName("last_rejected_connection") public val lastRejectedConnection: Pair<RpcConnectionId, @Contextual Timestamp>? = null,
    @SerialName("last_established_connection") public val lastEstablishedConnection: Pair<RpcConnectionId, @Contextual Timestamp>? = null,
    @SerialName("last_disconnection") public val lastDisconnection: Pair<RpcConnectionId, @Contextual Timestamp>? = null,
    @SerialName("last_seen") public val lastMiss: Pair<RpcConnectionId, @Contextual Timestamp>? = null,
    @SerialName("last_miss") public val lastSeen: Pair<RpcConnectionId, @Contextual Timestamp>? = null,
)

// -- RpcPeerState --

@Serializable
public enum class RpcPeerState {
    @SerialName("running") Running,
    @SerialName("accepted") Accepted,
    @SerialName("disconnected") Disconnected,
}

@Serializable
public data class RpcPeerPoolEvent(
    public val kind: Kind,
    public val timestamp: @Contextual Timestamp,
    @SerialName("addr") public val address: RpcIPAddress,
    public val port: UShort? = null,
) {

    @Serializable
    public enum class Kind {
        @SerialName("rejecting_request") RejectingRequest,
        @SerialName("incoming_request") IncomingRequest,
        @SerialName("disconnection") Disconnection,
        @SerialName("external_disconnection") ExternalDisconnection,
        @SerialName("connection_established") ConnectionEstablished,
        @SerialName("request_rejected") RequestRejected,
    }
}
