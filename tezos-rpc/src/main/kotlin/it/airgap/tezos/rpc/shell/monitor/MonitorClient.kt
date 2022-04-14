package it.airgap.tezos.rpc.shell.monitor

import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient

internal class MonitorClient(parentUrl: String, private val httpClient: HttpClient) : Monitor {
    private val baseUrl: String = /* /monitor */ "$parentUrl/monitor"

    override val activeChains: Monitor.ActiveChains by lazy { MonitorActiveChainsClient(baseUrl, httpClient) }
    override val bootstrapped: Monitor.Bootstrapped by lazy { MonitorBootstrappedClient(baseUrl, httpClient) }
    override val heads: Monitor.Heads by lazy { MonitorHeadsClient(baseUrl, httpClient) }
    override val protocols: Monitor.Protocols by lazy { MonitorProtocolsClient(baseUrl, httpClient) }
    override val validBlocks: Monitor.ValidBlocks by lazy { MonitorValidBlocksClient(baseUrl, httpClient) }
}

private class MonitorActiveChainsClient(parentUrl: String, private val httpClient: HttpClient): Monitor.ActiveChains {
    private val baseUrl: String = /* /monitor/active_chains */ "$parentUrl/active_chains"

    override suspend fun get(headers: List<HttpHeader>): MonitorActiveChainsResponse = httpClient.get(baseUrl, "/", headers)
}

private class MonitorBootstrappedClient(parentUrl: String, private val httpClient: HttpClient): Monitor.Bootstrapped {
    private val baseUrl: String = /* /monitor/bootstrapped */ "$parentUrl/bootstrapped"

    override suspend fun get(headers: List<HttpHeader>): MonitorBootstrappedResponse = httpClient.get(baseUrl, "/", headers)
}

private class MonitorHeadsClient(parentUrl: String, private val httpClient: HttpClient): Monitor.Heads {
    private val baseUrl: String = /* /monitor/heads */ "$parentUrl/heads"

    override operator fun invoke(chainId: String): Monitor.Heads.Head = MonitorHeadsHeadClient(baseUrl, chainId, httpClient)
}

private class MonitorHeadsHeadClient(parentUrl: String, chainId: String, private val httpClient: HttpClient): Monitor.Heads.Head {
    private val baseUrl: String = /* /monitor/heads/<chain_id> */ "$parentUrl/$chainId"

    override suspend fun get(nextProtocol: ProtocolHash?, headers: List<HttpHeader>): MonitorHeadResponse =
        httpClient.get(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                nextProtocol?.let { add("next_protocol" to it.base58) }
            },
        )
}

private class MonitorProtocolsClient(parentUrl: String, private val httpClient: HttpClient): Monitor.Protocols {
    private val baseUrl: String = /* /monitor/protocols */ "$parentUrl/protocols"

    override suspend fun get(headers: List<HttpHeader>): MonitorProtocolsResponse = httpClient.get(baseUrl, "/", headers)
}

private class MonitorValidBlocksClient(parentUrl: String, private val httpClient: HttpClient): Monitor.ValidBlocks {
    private val baseUrl: String = /* /monitor/valid_blocks */ "$parentUrl/valid_blocks"

    override suspend fun get(
        protocol: ProtocolHash?,
        nextProtocol: ProtocolHash?,
        chain: String?,
        headers: List<HttpHeader>,
    ): MonitorValidBlocksResponse =
        httpClient.get(
            baseUrl,
            "/",
            headers,
            parameters = buildList {
                protocol?.let { add("protocol" to it.base58) }
                nextProtocol?.let { add("next_protocol" to it.base58) }
                chain?.let { add("chain" to it) }
            },
        )
}