package it.airgap.tezos.rpc.type

import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcCryptoboxPublicKeyHash --

internal typealias TransitionalRpcCryptoboxPublicKeyHash = Unistring

// -- RpcConnection --

@Serializable
public data class GenericRpcConnection<PeerId, ConnectionId, NetworkVersion>(
    public val incoming: Boolean,
    @SerialName("peer_id") public val peerId: PeerId,
    @SerialName("id_point") public val idPoint: ConnectionId,
    @SerialName("remote_socket_port") public val remoteSocketPort: UShort,
    @SerialName("announced_version") public val announcedVersion: NetworkVersion,
    public val private: Boolean,
    @SerialName("local_metadata") public val localMetadata: RpcConnectionMetadata,
    @SerialName("remote_metadata") public val remoteMetadata: RpcConnectionMetadata,
)
internal typealias TransitionalRpcConnection = GenericRpcConnection<TransitionalRpcCryptoboxPublicKeyHash, RpcConnectionId, TransitionalRpcNetworkVersion>
public typealias RpcConnection = GenericRpcConnection<@Contextual CryptoboxPublicKeyHash, @Contextual RpcConnectionId, RpcNetworkVersion>

// -- RpcConnectionId --

@Serializable
public data class GenericRpcConnectionId<Address>(@SerialName("addr") public val address: Address, public val port: UShort? = null)
internal typealias TransitionalRpcConnectionId = GenericRpcConnectionId<TransitionalRpcIPAddress>
public typealias RpcConnectionId = GenericRpcConnectionId<RpcIPAddress>

// -- RpcConnectionMetadata --

@Serializable
public data class RpcConnectionMetadata(@SerialName("disable_mempool") public val disableMempool: Boolean, @SerialName("private_node") public val privateNode: Boolean)
