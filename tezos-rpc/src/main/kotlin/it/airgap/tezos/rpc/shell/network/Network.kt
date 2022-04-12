package it.airgap.tezos.rpc.shell.network

import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.type.network.RpcAcl
import it.airgap.tezos.rpc.type.network.RpcPeerState

public interface Network {
    public val connections: Connections
    public val greylist: Greylist
    public val log: Log
    public val peers: Peers

    public interface Connections {
        public suspend fun get(headers: List<HttpHeader> = emptyList()): GetConnectionsResponse

        public operator fun invoke(peerId: CryptoboxPublicKeyHash): Connection

        public interface Connection {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetConnectionResponse
            public suspend fun delete(wait: Boolean? = null, headers: List<HttpHeader> = emptyList()): CloseConnectionResponse
        }
    }

    public interface Greylist {
        public val ips: Ips
        public val peers: Peers

        public suspend fun delete(headers: List<HttpHeader> = emptyList()): ClearGreylistResponse

        public interface Ips {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetGreylistedIPsResponse
        }

        public interface Peers {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetLastGreylistedPeersResponse
        }
    }

    public interface Log {
        public suspend fun get(headers: List<HttpHeader> = emptyList()): GetLogResponse
    }

    public interface Peers {
        public suspend fun get(filter: RpcPeerState?, headers: List<HttpHeader> = emptyList()): GetPeersResponse

        public fun peer(peerId: CryptoboxPublicKeyHash): Peer

        public interface Peer {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetPeerResponse
            public suspend fun patch(acl: RpcAcl, headers: List<HttpHeader> = emptyList()): ChangePeerPermissionResponse

            public val banned: Banned
            public val log: Log

            public interface Banned {
                public suspend fun get(headers: List<HttpHeader> = emptyList()): GetPeerBannedStatusResponse
            }

            public interface Log {
                public suspend fun get(monitor: Boolean? = null, headers: List<HttpHeader> = emptyList()): GetPeerEventsResponse
            }
        }
    }
}