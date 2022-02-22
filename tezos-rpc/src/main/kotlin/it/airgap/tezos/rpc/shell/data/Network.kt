package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.type.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ==== /network ====

// -- /connections --

@Serializable
@JvmInline
internal value class GetConnectionsTransitionalResponse(val connections: List<TransitionalRpcConnection>)

@Serializable
@JvmInline
public value class GetConnectionsResponse(public val connections: List<RpcConnection>)

// -- /connections/<peer_id> --

@Serializable
@JvmInline
internal value class GetConnectionTransitionalResponse(val connection: TransitionalRpcConnection)

@Serializable
@JvmInline
public value class GetConnectionResponse(public val connection: RpcConnection)

public typealias CloseConnectionResponse = Unit

// -- /greylist --

public typealias ClearGreylistResponse = Unit

@Serializable
internal data class GetGreylistedIPsTransitionalResponse(val ips: List<TransitionalRpcIPAddress>, @SerialName("not_reliable_since") val notReliableSince: TransitionalTimestamp)

@Serializable
public data class GetGreylistedIPsResponse(public val ips: List<RpcIPAddress>, @SerialName("not_reliable_since") val notReliableSince: Timestamp)

@Serializable
@JvmInline
internal value class GetLastGreylistedPeersTransitionalResponse(val peers: List<TransitionalRpcCryptoboxPublicKeyHash>)

@Serializable
@JvmInline
public value class GetLastGreylistedPeersResponse(public val peers: List<@Contextual CryptoboxPublicKeyHash>)
