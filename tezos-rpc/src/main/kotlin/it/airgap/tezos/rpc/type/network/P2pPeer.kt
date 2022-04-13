package it.airgap.tezos.rpc.type.network

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.rpc.internal.serializer.RpcPeerMetadataEntrySerializer
import it.airgap.tezos.rpc.type.RpcIPAddress
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
    @SerialName("last_failed_connection") public val lastFailedConnection: @Contextual Pair<RpcConnectionId, @Contextual Timestamp>? = null,
    @SerialName("last_rejected_connection") public val lastRejectedConnection: @Contextual Pair<RpcConnectionId, @Contextual Timestamp>? = null,
    @SerialName("last_established_connection") public val lastEstablishedConnection: @Contextual Pair<RpcConnectionId, @Contextual Timestamp>? = null,
    @SerialName("last_disconnection") public val lastDisconnection: @Contextual Pair<RpcConnectionId, @Contextual Timestamp>? = null,
    @SerialName("last_seen") public val lastMiss: @Contextual Pair<RpcConnectionId, @Contextual Timestamp>? = null,
    @SerialName("last_miss") public val lastSeen: @Contextual Pair<RpcConnectionId, @Contextual Timestamp>? = null,
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

// -- RpcPeerMetadata --

@Serializable
public data class RpcPeerMetadata(
    public val responses: RpcPeerMetadataResponses,
    public val requests: RpcPeerMetadataRequests,
    @SerialName("valid_blocks") public val validBlocks: String,
    @SerialName("old_heads") public val oldHeads: String,
    @SerialName("prevalidator_results") public val prevalidatorResults: RpcPrevalidatorResults,
    @SerialName("unactivated_chains") public val unactivatedChains: String,
    @SerialName("inactive_chains") public val inactiveChains: String,
    @SerialName("future_blocks_advertised") public val futureBlocksAdvertised: String,
    public val unadvertised: RpcUnadvertised,
    public val advertisements: RpcAdvertisements,
)

// -- RpcPeerMetadataResponses --

@Serializable
public data class RpcPeerMetadataResponses(
    public val sent: RpcPeerMetadataEntry,
    public val failed: RpcPeerMetadataEntry,
    public val received: RpcPeerMetadataEntry,
    public val unexpected: String,
    public val outdated: String,
)

// -- RpcPeerMetadataRequests --

@Serializable
public data class RpcPeerMetadataRequests(
    public val sent: RpcPeerMetadataEntry,
    public val received: RpcPeerMetadataEntry,
    public val failed: RpcPeerMetadataEntry,
    public val scheduled: RpcPeerMetadataEntry,
)

// -- RpcPeerMetadataEntry --

@Serializable(with = RpcPeerMetadataEntrySerializer::class)
public sealed class RpcPeerMetadataEntry {
    public abstract val branch: String
    public abstract val head: String
    public abstract val blockHeader: String
    public abstract val operations: String
    public abstract val protocols: String
    public abstract val operationHashesForBlock: String
    public abstract val operationsForBlock: String
    public abstract val other: String

    @Transient
    public open val checkpoint: String? = null

    @Transient
    public open val protocolBranch: String? = null

    @Transient
    public open val predecessorHeader: String? = null

    @Serializable
    public data class V0(
        override val branch: String,
        override val head: String,
        @SerialName("block_header") override val blockHeader: String,
        override val operations: String,
        override val protocols: String,
        @SerialName("operation_hashes_for_block") override val operationHashesForBlock: String,
        @SerialName("operations_for_block") override val operationsForBlock: String,
        override val other: String,
    ) : RpcPeerMetadataEntry()

    @Serializable
    public data class V1(
        override val branch: String,
        override val head: String,
        @SerialName("block_header") override val blockHeader: String,
        override val operations: String,
        override val protocols: String,
        @SerialName("operation_hashes_for_block") override val operationHashesForBlock: String,
        @SerialName("operations_for_block") override val operationsForBlock: String,
        override val checkpoint: String,
        @SerialName("protocol_branch") override val protocolBranch: String,
        @SerialName("predecessor_header") override val predecessorHeader: String,
        override val other: String,
    ) : RpcPeerMetadataEntry()
}

// -- RpcPrevalidatorResults --

@Serializable
public data class RpcPrevalidatorResults(
    @SerialName("cannot_download") public val cannotDownload: String,
    @SerialName("cannot_parse") public val cannotParse: String,
    @SerialName("refused_by_prefilter") public val refusedByPrefilter: String,
    @SerialName("refused_by_postfilter") public val refusedByPostfilter: String,
    public val applied: String,
    @SerialName("branch_delayed") public val branchDelayed: String,
    @SerialName("branch_refused") public val branchRefused: String,
    public val refused: String,
    public val duplicate: String,
    public val outdated: String,
)

// -- RpcUnadvertised --

@Serializable
public data class RpcUnadvertised(public val block: String, public val operations: String, public val protocol: String)

// -- RpcAdvertisements --

@Serializable
public data class RpcAdvertisements(public val sent: RpcAdvertisement, public val received: RpcAdvertisement)

// -- RpcAdvertisement --

@Serializable
public data class RpcAdvertisement(public val head: String, public val branch: String)

// -- RpcAcl --

@Serializable
public enum class RpcAcl {
    @SerialName("open") Open,
    @SerialName("trust") Trust,
    @SerialName("ban") Ban,
}
