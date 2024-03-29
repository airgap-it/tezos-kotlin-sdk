package it.airgap.tezos.rpc.shell.network

import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.internal.utils.encodeToString
import it.airgap.tezos.rpc.type.network.RpcAcl
import it.airgap.tezos.rpc.type.network.RpcPeerState
import kotlinx.serialization.json.Json

internal class NetworkClient(parentUrl: String, private val httpClient: HttpClient, private val json: Json) : Network {
    private val baseUrl: String = /* /network */ "$parentUrl/network"

    override val connections: Network.Connections by lazy { NetworkConnectionsClient(baseUrl, httpClient) }
    override val greylist: Network.Greylist by lazy { NetworkGreylistClient(baseUrl, httpClient) }
    override val log: Network.Log by lazy { NetworkLogClient(baseUrl, httpClient) }
    override val peers: Network.Peers by lazy { NetworkPeersClient(baseUrl, httpClient, json) }
}

private class NetworkConnectionsClient(parentUrl: String, private val httpClient: HttpClient) : Network.Connections {
    private val baseUrl: String = /* /network/connections */ "$parentUrl/connections"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetConnectionsResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)

    override operator fun invoke(peerId: CryptoboxPublicKeyHash): Network.Connections.Connection = NetworkConnectionsConnectionClient(baseUrl, peerId, httpClient)
}

private class NetworkConnectionsConnectionClient(parentUrl: String, peerId: CryptoboxPublicKeyHash, private val httpClient: HttpClient) : Network.Connections.Connection {
    private val baseUrl: String = /* /network/connections/<peer_id> */ "$parentUrl/${peerId.base58}"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetConnectionResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
    override suspend fun delete(
        wait: Boolean?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): CloseConnectionResponse =
        httpClient.delete(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                wait?.takeIf { it }?.let { add("wait" to null) }
            },
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}

private class NetworkGreylistClient(parentUrl: String, private val httpClient: HttpClient) : Network.Greylist {
    private val baseUrl: String = /* /network/greylist */ "$parentUrl/greylist"

    override val ips: Network.Greylist.Ips by lazy { NetworkGreylistIpsClient(baseUrl, httpClient) }
    override val peers: Network.Greylist.Peers by lazy { NetworkGreylistPeersClient(baseUrl, httpClient) }

    override suspend fun delete(headers: List<HttpHeader>, requestTimeout: Long?, connectionTimeout: Long?): ClearGreylistResponse =
        httpClient.delete(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class NetworkGreylistIpsClient(parentUrl: String, private val httpClient: HttpClient) : Network.Greylist.Ips {
    private val baseUrl: String = /* /network/greylist/ips */ "$parentUrl/ips"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetGreylistedIpsResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class NetworkGreylistPeersClient(parentUrl: String, private val httpClient: HttpClient) : Network.Greylist.Peers {
    private val baseUrl: String = /* /network/greylist/peers */ "$parentUrl/peers"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetGreylistedPeersResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class NetworkLogClient(parentUrl: String, private val httpClient: HttpClient) : Network.Log {
    private val baseUrl: String = /* /network/log */ "$parentUrl/log"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetLogResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class NetworkPeersClient(parentUrl: String, private val httpClient: HttpClient, private val json: Json) : Network.Peers {
    private val baseUrl: String = /* /network/peers */ "$parentUrl/peers"

    override suspend fun get(
        filter: RpcPeerState?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetPeersResponse =
        httpClient.get(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                filter?.let { add("filter" to json.encodeToString(it)) }
            },
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )

    override operator fun invoke(peerId: CryptoboxPublicKeyHash): Network.Peers.Peer = NetworkPeersPeerClient(baseUrl, peerId, httpClient)
}

private class NetworkPeersPeerClient(parentUrl: String, peerId: CryptoboxPublicKeyHash, private val httpClient: HttpClient) : Network.Peers.Peer {
    private val baseUrl: String = /* /network/peers/<peer_id> */ "$parentUrl/${peerId.base58}"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetPeerResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)

    override suspend fun patch(
        acl: RpcAcl,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): ChangePeerPermissionResponse =
        httpClient.patch(
            baseUrl,
            "/",
            headers,
            request = ChangePeerPermissionRequest(acl),
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )

    override val banned: Network.Peers.Peer.Banned by lazy { NetworkPeersPeerBannedClient(baseUrl, httpClient) }
    override val log: Network.Peers.Peer.Log by lazy { NetworkPeersPeerLogClient(baseUrl, httpClient) }
}

private class NetworkPeersPeerBannedClient(parentUrl: String, private val httpClient: HttpClient) : Network.Peers.Peer.Banned {
    private val baseUrl: String = /* /network/peers/<peer_id>/banned */ "$parentUrl/banned"

    override suspend fun get(
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetPeerBannedStatusResponse = httpClient.get(baseUrl, "/", headers, requestTimeout = requestTimeout, connectionTimeout = connectionTimeout)
}

private class NetworkPeersPeerLogClient(parentUrl: String, private val httpClient: HttpClient) : Network.Peers.Peer.Log {
    private val baseUrl: String = /* /network/peers/<peer_id>/log */ "$parentUrl/log"

    override suspend fun get(
        monitor: Boolean?,
        headers: List<HttpHeader>,
        requestTimeout: Long?,
        connectionTimeout: Long?,
    ): GetPeerEventsResponse =
        httpClient.get(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                monitor?.takeIf { it }?.let { add("monitor" to null) }
            },
            requestTimeout = requestTimeout,
            connectionTimeout = connectionTimeout,
        )
}
