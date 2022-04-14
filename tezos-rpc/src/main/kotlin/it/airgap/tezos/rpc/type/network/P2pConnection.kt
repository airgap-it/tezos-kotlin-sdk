package it.airgap.tezos.rpc.type.network

import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.type.RpcIPAddress
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator

// -- RpcConnection --

@Serializable
public data class RpcConnection(
    public val incoming: Boolean,
    @SerialName("peer_id") public val peerId: @Contextual CryptoboxPublicKeyHash,
    @SerialName("id_point") public val idPoint: RpcConnectionId,
    @SerialName("remote_socket_port") public val remoteSocketPort: UShort,
    @SerialName("announced_version") public val announcedVersion: RpcNetworkVersion,
    public val private: Boolean,
    @SerialName("local_metadata") public val localMetadata: RpcConnectionMetadata,
    @SerialName("remote_metadata") public val remoteMetadata: RpcConnectionMetadata,
)

// -- RpcConnectionId --

@Serializable
public data class RpcConnectionId(@SerialName("addr") public val address: RpcIPAddress, public val port: UShort? = null)

// -- RpcConnectionMetadata --

@Serializable
public data class RpcConnectionMetadata(@SerialName("disable_mempool") public val disableMempool: Boolean, @SerialName("private_node") public val privateNode: Boolean)

// -- RpcConnectionPointId --

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
@JsonClassDiscriminator(RpcConnectionPoolEvent.CLASS_DISCRIMINATOR)
public sealed class RpcConnectionPoolEvent {
    @Transient
    public open val point: RpcConnectionPointId? = null

    @Transient
    public open val peerId: @Contextual CryptoboxPublicKeyHash? = null

    @Transient
    public open val idPoint: RpcConnectionId? = null

    @Transient
    public open val identity: @Contextual Pair<RpcConnectionId, @Contextual CryptoboxPublicKeyHash>? = null

    @Transient
    public open val source: @Contextual CryptoboxPublicKeyHash? = null

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "event"
    }
}

@Serializable
@SerialName(RpcTooFewConnectionsEvent.KIND)
public object RpcTooFewConnectionsEvent : RpcConnectionPoolEvent() {
    internal const val KIND = "too_few_connections"
}

@Serializable
@SerialName(RpcTooManyConnectionsEvent.KIND)
public object RpcTooManyConnectionsEvent : RpcConnectionPoolEvent() {
    internal const val KIND = "too_many_connections"
}

@Serializable
@SerialName(RpcNewPointEvent.KIND)
public data class RpcNewPointEvent(override val point: RpcConnectionPointId) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "new_point"
    }
}

@Serializable
@SerialName(RpcNewPeerEvent.KIND)
public data class RpcNewPeerEvent(@SerialName("peer_id") override val peerId: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "new_peer"
    }
}

@Serializable
@SerialName(RpcIncomingConnectionEvent.KIND)
public data class RpcIncomingConnectionEvent(override val point: RpcConnectionPointId) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "incoming_connection"
    }
}

@Serializable
@SerialName(RpcOutgoingConnectionEvent.KIND)
public data class RpcOutgoingConnectionEvent(override val point: RpcConnectionPointId) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "outgoing_connection"
    }
}

@Serializable
@SerialName(RpcAuthenticationFailedEvent.KIND)
public data class RpcAuthenticationFailedEvent(override val point: RpcConnectionPointId) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "authentication_failed"
    }
}

@Serializable
@SerialName(RpcAcceptingRequestEvent.KIND)
public data class RpcAcceptingRequestEvent(
    override val point: RpcConnectionPointId,
    @SerialName("id_point") override val idPoint: RpcConnectionId,
    @SerialName("peer_id") override val peerId: @Contextual CryptoboxPublicKeyHash,
) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "accepting_request"
    }
}

@Serializable
@SerialName(RpcRejectingRequestEvent.KIND)
public data class RpcRejectingRequestEvent(
    override val point: RpcConnectionPointId,
    @SerialName("id_point") override val idPoint: RpcConnectionId,
    @SerialName("peer_id") override val peerId: @Contextual CryptoboxPublicKeyHash,
) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "rejecting_request"
    }
}

@Serializable
@SerialName(RpcRequestRejectedEvent.KIND)
public data class RpcRequestRejectedEvent(
    override val point: RpcConnectionPointId,
    override val identity: @Contextual Pair<RpcConnectionId, @Contextual CryptoboxPublicKeyHash>? = null,
) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "request_rejected"
    }
}

@Serializable
@SerialName(RpcConnectionEstablishedEvent.KIND)
public data class RpcConnectionEstablishedEvent(
    @SerialName("id_point") override val idPoint: RpcConnectionId,
    @SerialName("peer_id") override val peerId: @Contextual CryptoboxPublicKeyHash,
) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "connection_established"
    }
}

@Serializable
@SerialName(RpcDisconnectionEvent.KIND)
public data class RpcDisconnectionEvent(@SerialName("peer_id") override val peerId: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "disconnection"
    }
}

@Serializable
@SerialName(RpcExternalDisconnectionEvent.KIND)
public data class RpcExternalDisconnectionEvent(@SerialName("peer_id") override val peerId: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "external_disconnection"
    }
}

@Serializable
@SerialName(RpcGcPointsEvent.KIND)
public object RpcGcPointsEvent : RpcConnectionPoolEvent() {
    internal const val KIND = "gc_points"
}

@Serializable
@SerialName(RpcGcPeerIdsEvent.KIND)
public object RpcGcPeerIdsEvent : RpcConnectionPoolEvent() {
    internal const val KIND = "gc_peer_ids"
}

@Serializable
@SerialName(RpcSwapRequestReceivedEvent.KIND)
public data class RpcSwapRequestReceivedEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "swap_request_received"
    }
}

@Serializable
@SerialName(RpcSwapAckReceivedEvent.KIND)
public data class RpcSwapAckReceivedEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "swap_ack_received"
    }
}

@Serializable
@SerialName(RpcSwapRequestSentEvent.KIND)
public data class RpcSwapRequestSentEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "swap_request_sent"
    }
}

@Serializable
@SerialName(RpcSwapAckSentEvent.KIND)
public data class RpcSwapAckSentEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "swap_ack_sent"
    }
}

@Serializable
@SerialName(RpcSwapRequestIgnoredEvent.KIND)
public data class RpcSwapRequestIgnoredEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "swap_request_ignored"
    }
}

@Serializable
@SerialName(RpcSwapSuccessEvent.KIND)
public data class RpcSwapSuccessEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "swap_success"
    }
}

@Serializable
@SerialName(RpcSwapFailureEvent.KIND)
public data class RpcSwapFailureEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "swap_failure"
    }
}

@Serializable
@SerialName(RpcBootstrapSentEvent.KIND)
public data class RpcBootstrapSentEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "bootstrap_sent"
    }
}

@Serializable
@SerialName(RpcBootstrapReceivedEvent.KIND)
public data class RpcBootstrapReceivedEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {

    public companion object {
        internal const val KIND = "bootstrap_received"
    }
}

@Serializable
@SerialName(RpcAdvertiseSentEvent.KIND)
public data class RpcAdvertiseSentEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "advertise_sent"
    }
}

@Serializable
@SerialName(RpcAdvertiseReceivedEvent.KIND)
public data class RpcAdvertiseReceivedEvent(override val source: @Contextual CryptoboxPublicKeyHash) : RpcConnectionPoolEvent() {
    public companion object {
        internal const val KIND = "advertise_received"
    }
}