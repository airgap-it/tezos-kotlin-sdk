package it.airgap.tezos.rpc.type.p2p

import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.type.RpcIPAddress
import it.airgap.tezos.rpc.type.TransitionalRpcIPAddress
import it.airgap.tezos.rpc.type.Unistring
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator

// -- RpcConnection --

@Serializable
public data class GenericRpcConnection<CryptoboxPublicKeyHash, ConnectionId>(
    public val incoming: Boolean,
    @SerialName("peer_id") public val peerId: CryptoboxPublicKeyHash,
    @SerialName("id_point") public val idPoint: ConnectionId,
    @SerialName("remote_socket_port") public val remoteSocketPort: UShort,
    @SerialName("announced_version") public val announcedVersion: RpcNetworkVersion,
    public val private: Boolean,
    @SerialName("local_metadata") public val localMetadata: RpcConnectionMetadata,
    @SerialName("remote_metadata") public val remoteMetadata: RpcConnectionMetadata,
)
internal typealias TransitionalRpcConnection = GenericRpcConnection<TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcConnection = GenericRpcConnection<@Contextual CryptoboxPublicKeyHash, RpcConnectionId>

// -- RpcConnectionId --

@Serializable
public data class GenericRpcConnectionId<Address>(@SerialName("addr") public val address: Address, public val port: UShort? = null)
internal typealias TransitionalRpcConnectionId = GenericRpcConnectionId<TransitionalRpcIPAddress>
public typealias RpcConnectionId = GenericRpcConnectionId<RpcIPAddress>

// -- RpcConnectionMetadata --

@Serializable
public data class RpcConnectionMetadata(@SerialName("disable_mempool") public val disableMempool: Boolean, @SerialName("private_node") public val privateNode: Boolean)

// -- RpcConnectionPointId --

internal typealias TransitionalRpcConnectionPointId = Unistring

@Serializable
@JvmInline
public value class RpcConnectionPointId(public val point: String) {
    init {
        require(isValid(point)) { "Invalid PointId value." }
    }

    public companion object {
        public fun isValid(string: String): Boolean {
            val (ip, port) = string.split(":", limit = 2)
            return RpcIPAddress.isValid(ip) && port.toUShortOrNull(10)?.takeIf { it > 0U } != null
        }
    }
}

// -- RpcConnectionPoolEvent --

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(GenericRpcConnectionPoolEvent.CLASS_DISCRIMINATOR)
public sealed class GenericRpcConnectionPoolEvent<out PointId, out CryptoboxPublicKeyHash, out ConnectionId> {
    @Transient
    public open val point: PointId? = null

    @Transient
    public open val peerId: CryptoboxPublicKeyHash? = null

    @Transient
    public open val idPoint: ConnectionId? = null

    @Transient
    public open val identity: Pair<ConnectionId, CryptoboxPublicKeyHash>? = null

    @Transient
    public open val source: CryptoboxPublicKeyHash? = null

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "event"
    }
}
internal typealias TransitionalRpcConnectionPoolEvent = GenericRpcConnectionPoolEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcConnectionPoolEvent = GenericRpcConnectionPoolEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(RpcTooFewConnectionsEvent.KIND)
public object RpcTooFewConnectionsEvent : GenericRpcConnectionPoolEvent<Nothing, Nothing, Nothing>() {
    internal const val KIND = "too_few_connections"
}

@Serializable
@SerialName(RpcTooManyConnectionsEvent.KIND)
public object RpcTooManyConnectionsEvent : GenericRpcConnectionPoolEvent<Nothing, Nothing, Nothing>() {
    internal const val KIND = "too_many_connections"
}

@Serializable
@SerialName(GenericRpcNewPointEvent.KIND)
public data class GenericRpcNewPointEvent<PointId, PeerId, ConnectionId>(override val point: PointId) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "new_point"
    }
}
internal typealias TransitionalRpcNewPointEvent = GenericRpcNewPointEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcNewPointEvent = GenericRpcNewPointEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcNewPeerEvent.KIND)
public data class GenericRpcNewPeerEvent<PointId, PeerId, ConnectionId>(@SerialName("peer_id") override val peerId: PeerId) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "new_peer"
    }
}
internal typealias TransitionalRpcNewPeerEvent = GenericRpcNewPeerEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcNewPeerEvent = GenericRpcNewPeerEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcIncomingConnectionEvent.KIND)
public data class GenericRpcIncomingConnectionEvent<PointId, PeerId, ConnectionId>(override val point: PointId) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "incoming_connection"
    }
}
internal typealias TransitionalRpcIncomingConnectionEvent = GenericRpcIncomingConnectionEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcIncomingConnectionEvent = GenericRpcIncomingConnectionEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcOutgoingConnectionEvent.KIND)
public data class GenericRpcOutgoingConnectionEvent<PointId, PeerId, ConnectionId>(override val point: PointId) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "outgoing_connection"
    }
}
internal typealias TransitionalRpcOutgoingConnectionEvent = GenericRpcOutgoingConnectionEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcOutgoingConnectionEvent = GenericRpcOutgoingConnectionEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcAuthenticationFailedEvent.KIND)
public data class GenericRpcAuthenticationFailedEvent<PointId, PeerId, ConnectionId>(override val point: PointId) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "authentication_failed"
    }
}
internal typealias TransitionalRpcAuthenticationFailedEvent = GenericRpcAuthenticationFailedEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcAuthenticationFailedEvent = GenericRpcAuthenticationFailedEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcAcceptingRequestEvent.KIND)
public data class GenericRpcAcceptingRequestEvent<PointId, PeerId, ConnectionId>(
    override val point: PointId,
    @SerialName("id_point") override val idPoint: ConnectionId,
    @SerialName("peer_id") override val peerId: PeerId,
) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "accepting_request"
    }
}
internal typealias TransitionalRpcAcceptingRequestEvent = GenericRpcAcceptingRequestEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcAcceptingRequestEvent = GenericRpcAcceptingRequestEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcRejectingRequestEvent.KIND)
public data class GenericRpcRejectingRequestEvent<PointId, PeerId, ConnectionId>(
    override val point: PointId,
    @SerialName("id_point") override val idPoint: ConnectionId,
    @SerialName("peer_id") override val peerId: PeerId,
) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "rejecting_request"
    }
}
internal typealias TransitionalRpcRejectingRequestEvent = GenericRpcRejectingRequestEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcRejectingRequestEvent = GenericRpcRejectingRequestEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcRequestRejectedEvent.KIND)
public data class GenericRpcRequestRejectedEvent<PointId, PeerId, ConnectionId>(
    override val point: PointId,
    override val identity: Pair<ConnectionId, PeerId>? = null,
) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "request_rejected"
    }
}
internal typealias TransitionalRpcRequestRejectedEvent = GenericRpcRequestRejectedEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcRequestRejectedEvent = GenericRpcRequestRejectedEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcConnectionEstablishedEvent.KIND)
public data class GenericRpcConnectionEstablishedEvent<PointId, PeerId, ConnectionId>(
    @SerialName("id_point") override val idPoint: ConnectionId,
    @SerialName("peer_id") override val peerId: PeerId,
) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "accepting_request"
    }
}
internal typealias TransitionalRpcConnectionEstablishedEvent = GenericRpcConnectionEstablishedEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcConnectionEstablishedEvent = GenericRpcConnectionEstablishedEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcDisconnectionEvent.KIND)
public data class GenericRpcDisconnectionEvent<PointId, PeerId, ConnectionId>(@SerialName("peer_id") override val peerId: PeerId) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "disconnection"
    }
}
internal typealias TransitionalRpcDisconnectionEvent = GenericRpcDisconnectionEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcDisconnectionEvent = GenericRpcDisconnectionEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcExternalDisconnectionEvent.KIND)
public data class GenericRpcExternalDisconnectionEvent<PointId, PeerId, ConnectionId>(@SerialName("peer_id") override val peerId: PeerId) : GenericRpcConnectionPoolEvent<PointId, PeerId, ConnectionId>() {
    public companion object {
        internal const val KIND = "external_disconnection"
    }
}
internal typealias TransitionalRpcExternalDisconnectionEvent = GenericRpcExternalDisconnectionEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcExternalDisconnectionEvent = GenericRpcExternalDisconnectionEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(RpcGcPointsEvent.KIND)
public object RpcGcPointsEvent : GenericRpcConnectionPoolEvent<Nothing, Nothing, Nothing>() {
    internal const val KIND = "gc_points"
}

@Serializable
@SerialName(RpcGcPeerIdsEvent.KIND)
public object RpcGcPeerIdsEvent : GenericRpcConnectionPoolEvent<Nothing, Nothing, Nothing>() {
    internal const val KIND = "gc_peer_ids"
}

@Serializable
@SerialName(GenericRpcSwapRequestReceivedEvent.KIND)
public data class GenericRpcSwapRequestReceivedEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {
    public companion object {
        internal const val KIND = "swap_request_received"
    }
}
internal typealias TransitionalRpcSwapRequestReceivedEvent = GenericRpcSwapRequestReceivedEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcSwapRequestReceivedEvent = GenericRpcSwapRequestReceivedEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcSwapAckReceivedEvent.KIND)
public data class GenericRpcSwapAckReceivedEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {
    public companion object {
        internal const val KIND = "swap_ack_received"
    }
}
internal typealias TransitionalRpcSwapAckReceivedEvent = GenericRpcSwapAckReceivedEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcSwapAckReceivedEvent = GenericRpcSwapAckReceivedEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcSwapRequestSentEvent.KIND)
public data class GenericRpcSwapRequestSentEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {
    public companion object {
        internal const val KIND = "swap_request_sent"
    }
}
internal typealias TransitionalRpcSwapRequestSentEvent = GenericRpcSwapRequestSentEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcSwapRequestSentEvent = GenericRpcSwapRequestSentEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcSwapAckSentEvent.KIND)
public data class GenericRpcSwapAckSentEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {
    public companion object {
        internal const val KIND = "swap_ack_sent"
    }
}
internal typealias TransitionalRpcSwapAckSentEvent = GenericRpcSwapAckSentEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcSwapAckSentEvent = GenericRpcSwapAckSentEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcSwapRequestIgnoredEvent.KIND)
public data class GenericRpcSwapRequestIgnoredEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {
    public companion object {
        internal const val KIND = "swap_request_ignored"
    }
}
internal typealias TransitionalRpcSwapRequestIgnoredEvent = GenericRpcSwapRequestIgnoredEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcSwapRequestIgnoredEvent = GenericRpcSwapRequestIgnoredEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcSwapSuccessEvent.KIND)
public data class GenericRpcSwapSuccessEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {
    public companion object {
        internal const val KIND = "swap_success"
    }
}
internal typealias TransitionalRpcSwapSuccessEvent = GenericRpcSwapSuccessEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcSwapSuccessEvent = GenericRpcSwapSuccessEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcSwapFailureEvent.KIND)
public data class GenericRpcSwapFailureEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {
    public companion object {
        internal const val KIND = "swap_failure"
    }
}
internal typealias TransitionalRpcSwapFailureEvent = GenericRpcSwapFailureEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcSwapFailureEvent = GenericRpcSwapFailureEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcBootstrapSentEvent.KIND)
public data class GenericRpcBootstrapSentEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {
    public companion object {
        internal const val KIND = "bootstrap_sent"
    }
}
internal typealias TransitionalRpcBootstrapSentEvent = GenericRpcBootstrapSentEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcBootstrapSentEvent = GenericRpcBootstrapSentEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcBootstrapReceivedEvent.KIND)
public data class GenericRpcBootstrapReceivedEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {

    public companion object {
        internal const val KIND = "bootstrap_received"
    }
}
internal typealias TransitionalRpcBootstrapReceivedEvent = GenericRpcBootstrapReceivedEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcBootstrapReceivedEvent = GenericRpcBootstrapReceivedEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcAdvertiseSentEvent.KIND)
public data class GenericRpcAdvertiseSentEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {
    public companion object {
        internal const val KIND = "advertise_sent"
    }
}
internal typealias TransitionalRpcAdvertiseSentEvent = GenericRpcAdvertiseSentEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcAdvertiseSentEvent = GenericRpcAdvertiseSentEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>

@Serializable
@SerialName(GenericRpcAdvertiseReceivedEvent.KIND)
public data class GenericRpcAdvertiseReceivedEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>(override val source: CryptoboxPublicKeyHash) : GenericRpcConnectionPoolEvent<PointId, CryptoboxPublicKeyHash, ConnectionId>() {
    public companion object {
        internal const val KIND = "advertise_received"
    }
}
internal typealias TransitionalRpcAdvertiseReceivedEvent = GenericRpcAdvertiseReceivedEvent<TransitionalRpcConnectionPointId, TransitionalRpcCryptoboxPublicKeyHash, TransitionalRpcConnectionId>
public typealias RpcAdvertiseReceivedEvent = GenericRpcAdvertiseReceivedEvent<RpcConnectionPointId, @Contextual CryptoboxPublicKeyHash, RpcConnectionId>