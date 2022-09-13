package it.airgap.tezos.rpc.shell.network

import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.type.network.RpcAcl
import it.airgap.tezos.rpc.type.network.RpcPeerState

/**
 * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network`
 */
public interface Network {
    public val connections: Connections
    public val greylist: Greylist
    public val log: Log
    public val peers: Peers

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network/connections`
     */
    public interface Connections {
        public suspend fun get(
            headers: List<HttpHeader> = emptyList(),
            requestTimeout: Long? = null,
            connectionTimeout: Long? = null,
        ): GetConnectionsResponse

        public operator fun invoke(peerId: CryptoboxPublicKeyHash): Connection

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network/connections/<peer_id>`
         */
        public interface Connection {
            public suspend fun get(
                headers: List<HttpHeader> = emptyList(),
                requestTimeout: Long? = null,
                connectionTimeout: Long? = null,
            ): GetConnectionResponse
            public suspend fun delete(
                wait: Boolean? = null,
                headers: List<HttpHeader> = emptyList(),
                requestTimeout: Long? = null,
                connectionTimeout: Long? = null,
            ): CloseConnectionResponse
        }
    }

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network/greylist`
     */
    public interface Greylist {
        public val ips: Ips
        public val peers: Peers

        public suspend fun delete(
            headers: List<HttpHeader> = emptyList(),
            requestTimeout: Long? = null,
            connectionTimeout: Long? = null,
        ): ClearGreylistResponse

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network/greylist/ips`
         */
        public interface Ips {
            public suspend fun get(
                headers: List<HttpHeader> = emptyList(),
                requestTimeout: Long? = null,
                connectionTimeout: Long? = null,
            ): GetGreylistedIpsResponse
        }

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network/greylist/peers`
         */
        public interface Peers {
            public suspend fun get(
                headers: List<HttpHeader> = emptyList(),
                requestTimeout: Long? = null,
                connectionTimeout: Long? = null,
            ): GetGreylistedPeersResponse
        }
    }

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network/log`
     */
    public interface Log {
        public suspend fun get(
            headers: List<HttpHeader> = emptyList(),
            requestTimeout: Long? = null,
            connectionTimeout: Long? = null,
        ): GetLogResponse
    }

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network/peers`
     */
    public interface Peers {
        public suspend fun get(
            filter: RpcPeerState? = null,
            headers: List<HttpHeader> = emptyList(),
            requestTimeout: Long? = null,
            connectionTimeout: Long? = null,
        ): GetPeersResponse

        public operator fun invoke(peerId: CryptoboxPublicKeyHash): Peer

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network/peers/<peer_id>`
         */
        public interface Peer {
            public suspend fun get(
                headers: List<HttpHeader> = emptyList(),
                requestTimeout: Long? = null,
                connectionTimeout: Long? = null,
            ): GetPeerResponse
            public suspend fun patch(
                acl: RpcAcl,
                headers: List<HttpHeader> = emptyList(),
                requestTimeout: Long? = null,
                connectionTimeout: Long? = null,
            ): ChangePeerPermissionResponse

            public val banned: Banned
            public val log: Log

            /**
             * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network/peers/<peer_id>/banned`
             */
            public interface Banned {
                public suspend fun get(
                    headers: List<HttpHeader> = emptyList(),
                    requestTimeout: Long? = null,
                    connectionTimeout: Long? = null,
                ): GetPeerBannedStatusResponse
            }

            /**
             * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/network/peers/<peer_id>/log`
             */
            public interface Log {
                public suspend fun get(
                    monitor: Boolean? = null,
                    headers: List<HttpHeader> = emptyList(),
                    requestTimeout: Long? = null,
                    connectionTimeout: Long? = null,
                ): GetPeerEventsResponse
            }
        }
    }
}