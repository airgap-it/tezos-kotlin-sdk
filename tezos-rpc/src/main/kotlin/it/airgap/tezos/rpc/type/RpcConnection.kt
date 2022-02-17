package it.airgap.tezos.rpc.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RpcConnection<PeerId, Address, DistributedDbVersionName>(
    public val incoming: Boolean,
    @SerialName("peer_id") public val peerId: PeerId,
    @SerialName("id_point") public val idPoint: RpcConnectionId<Address>,
    @SerialName("remote_socket_port") public val remoteSocketPort: UShort,
    @SerialName("announced_version") public val announcedVersion: RpcNetworkVersion<DistributedDbVersionName>,
    public val private: Boolean,
    @SerialName("local_metadata") public val localMetadata: RpcConnectionMetadata,
    @SerialName("remote_metadata") public val remoteMetadata: RpcConnectionMetadata,
)

@Serializable
public data class RpcConnectionId<Address>(public val addr: Address, public val port: UShort? = null)

@Serializable
public data class RpcConnectionMetadata(@SerialName("disable_mempool") public val disableMempool: Boolean, @SerialName("private_node") public val privateNode: Boolean)
