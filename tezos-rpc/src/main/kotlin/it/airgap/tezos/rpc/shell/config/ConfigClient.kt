package it.airgap.tezos.rpc.shell.config

import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient

internal class ConfigClient(parentUrl: String, private val httpClient: HttpClient) : Config {
    private val baseUrl: String = /* /config */ "$parentUrl/config"

    override val historyMode: Config.HistoryMode by lazy { ConfigHistoryModeClient(baseUrl, httpClient) }
    override val logging: Config.Logging by lazy { ConfigLoggingClient(baseUrl, httpClient) }
    override val network: Config.Network by lazy { ConfigNetworkClient(baseUrl, httpClient) }
}

private class ConfigHistoryModeClient(parentUrl: String, private val httpClient: HttpClient) : Config.HistoryMode {
    private val baseUrl: String = /* /config/history_mode */ "$parentUrl/history_mode"

    override suspend fun get(headers: List<HttpHeader>): GetHistoryModeResponse = httpClient.get(baseUrl, "/", headers)
}

private class ConfigLoggingClient(parentUrl: String, private val httpClient: HttpClient) : Config.Logging {
    private val baseUrl: String = /* /config/logging */ "$parentUrl/logging"

    override suspend fun put(activeSinks: String, headers: List<HttpHeader>): SetLoggingResponse =
        httpClient.put(baseUrl, "/", headers, request = SetLoggingRequest(activeSinks))
}

private class ConfigNetworkClient(parentUrl: String, private val httpClient: HttpClient) : Config.Network {
    private val baseUrl: String = /* /config/network */ "$parentUrl/network"

    override val userActivatedProtocolOverrides: Config.Network.UserActivatedProtocolOverrides by lazy { ConfigNetworkUserActivatedProtocolOverridesClient(baseUrl, httpClient) }
    override val userActivatedUpgrades: Config.Network.UserActivatedUpgrades by lazy { ConfigNetworkUserActivatedUpgradesClient(baseUrl, httpClient) }
}

private class ConfigNetworkUserActivatedProtocolOverridesClient(parentUrl: String, private val httpClient: HttpClient) : Config.Network.UserActivatedProtocolOverrides {
    private val baseUrl: String = /* /config/network/user_activated_protocol_overrides */ "$parentUrl/network/user_activated_protocol_overrides"

    override suspend fun get(headers: List<HttpHeader>): GetUserActivatedProtocolOverridesResponse = httpClient.get(baseUrl, "/", headers)
}

private class ConfigNetworkUserActivatedUpgradesClient(parentUrl: String, private val httpClient: HttpClient) : Config.Network.UserActivatedUpgrades {
    private val baseUrl: String = /* /config/network/user_activated_upgrades */ "$parentUrl/network/user_activated_upgrades"

    override suspend fun get(headers: List<HttpHeader>): GetUserActivatedUpgradesResponse = httpClient.get(baseUrl, "/", headers)
}