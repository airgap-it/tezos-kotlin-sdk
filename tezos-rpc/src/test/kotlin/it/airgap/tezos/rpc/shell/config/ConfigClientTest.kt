package it.airgap.tezos.rpc.shell.chains

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.internal.serializer.rpcJson
import it.airgap.tezos.rpc.shell.config.*
import it.airgap.tezos.rpc.type.history.RpcHistoryMode
import it.airgap.tezos.rpc.type.protocol.RpcUserActivatedProtocolOverride
import it.airgap.tezos.rpc.type.protocol.RpcUserActivatedUpgrade
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ConfigClientTest {

    @MockK
    private lateinit var httpClientProvider : HttpClientProvider

    private lateinit var json: Json
    private lateinit var httpClient: HttpClient
    private lateinit var configClient: ConfigClient

    private val nodeUrl = "https://example.com"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        json = Json(from = rpcJson) {
            prettyPrint = true
        }
        httpClient = HttpClient(httpClientProvider, json)
        configClient = ConfigClient(nodeUrl, httpClient)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should call GET on 'config - history_mode'`() {
        val (_, expectedResponse, _, jsonResponse) = configHistoryModeRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { configClient.historyMode.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/config/history_mode", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/config/history_mode", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call PUT on 'config - logging'`() {
        val activeSinks = listOf("sink")

        val (expectedRequest, expectedResponse, jsonRequest, jsonResponse) = configLoggingRequestConfiguration(activeSinks)
        coEvery { httpClientProvider.put(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { configClient.logging.put(activeSinks, headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.put("$nodeUrl/config/logging", "/", headers = headers, request = expectedRequest) }
            coVerify { httpClientProvider.put("$nodeUrl/config/logging", "/", headers = headers, parameters = emptyList(), body = jsonRequest?.normalizeWith(json)) }
        }
    }

    @Test
    fun `should call GET on 'config - network - user_activated_protocol_overrides'`() {
        val (_, expectedResponse, _, jsonResponse) = configNetworkUserActivatedProtocolOverridesRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { configClient.network.userActivatedProtocolOverrides.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/config/network/user_activated_protocol_overrides", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/config/network/user_activated_protocol_overrides", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'config - network - user_activated_upgrades'`() {
        val (_, expectedResponse, _, jsonResponse) = configNetworkUserActivatedUpgradesRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { configClient.network.userActivatedUpgrades.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/config/network/user_activated_upgrades", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/config/network/user_activated_upgrades", "/", headers = headers, parameters = emptyList()) }
        }
    }

    private val headers: List<List<HttpHeader>>
        get() = listOf(
            emptyList(),
            listOf("Authorization" to "Bearer")
        )

    private val configHistoryModeRequestConfiguration: RequestConfiguration<Unit, GetHistoryModeResponse>
        get() = RequestConfiguration(
            response = GetHistoryModeResponse(RpcHistoryMode.Archive),
            jsonResponse = """
                {
                  "history_mode": "archive"
                }
            """.trimIndent(),
        )

    private fun configLoggingRequestConfiguration(activeSinks: List<String>): RequestConfiguration<SetLoggingRequest, SetLoggingResponse> =
        RequestConfiguration(
            request = SetLoggingRequest(activeSinks),
            response = SetLoggingResponse,
            jsonRequest = """
                {
                  "active_sinks": ${json.encodeToString(activeSinks)}
                }
            """.trimIndent(),
            jsonResponse = "",
        )

    private val configNetworkUserActivatedProtocolOverridesRequestConfiguration: RequestConfiguration<Unit, GetUserActivatedProtocolOverridesResponse>
        get() = RequestConfiguration(
            response = GetUserActivatedProtocolOverridesResponse(
                listOf(
                    RpcUserActivatedProtocolOverride(
                        replacedProtocol = ProtocolHash("PsBABY5HQTSkA4297zNHfsZNKtxULfL18y95qb3m53QJiXGmrbU"),
                        replacementProtocol = ProtocolHash("PsBabyM1eUXZseaJdmXFApDSBqj8YBfwELoxZHHW77EMcAbbwAS"),
                    ),
                    RpcUserActivatedProtocolOverride(
                        replacedProtocol = ProtocolHash("PtEdoTezd3RHSC31mpxxo1npxFjoWWcFgQtxapi51Z8TLu6v6Uq"),
                        replacementProtocol = ProtocolHash("PtEdo2ZkT9oKpimTah6x2embF25oss54njMuPzkJTEi5RqfdZFA"),
                    ),
                    RpcUserActivatedProtocolOverride(
                        replacedProtocol = ProtocolHash("PtHangzHogokSuiMHemCuowEavgYTP8J5qQ9fQS793MHYFpCY3r"),
                        replacementProtocol = ProtocolHash("PtHangz2aRngywmSRGGvrcTyMbbdpWdpFKuS4uMWxg2RaH9i1qx"),
                    ),
                )
            ),
            jsonResponse = """
                [
                    {
                        "replaced_protocol": "PsBABY5HQTSkA4297zNHfsZNKtxULfL18y95qb3m53QJiXGmrbU",
                        "replacement_protocol": "PsBabyM1eUXZseaJdmXFApDSBqj8YBfwELoxZHHW77EMcAbbwAS"
                    },
                    {
                        "replaced_protocol": "PtEdoTezd3RHSC31mpxxo1npxFjoWWcFgQtxapi51Z8TLu6v6Uq",
                        "replacement_protocol": "PtEdo2ZkT9oKpimTah6x2embF25oss54njMuPzkJTEi5RqfdZFA"
                    },
                    {
                        "replaced_protocol": "PtHangzHogokSuiMHemCuowEavgYTP8J5qQ9fQS793MHYFpCY3r",
                        "replacement_protocol": "PtHangz2aRngywmSRGGvrcTyMbbdpWdpFKuS4uMWxg2RaH9i1qx"
                    }
                ]
            """.trimIndent()
        )

    private val configNetworkUserActivatedUpgradesRequestConfiguration: RequestConfiguration<Unit, GetUserActivatedUpgradesResponse>
        get() = RequestConfiguration(
            response = GetUserActivatedUpgradesResponse(
                listOf(
                    RpcUserActivatedUpgrade(
                        level = 28082,
                        replacementProtocol = ProtocolHash("PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt"),
                    ),
                    RpcUserActivatedUpgrade(
                        level = 204761,
                        replacementProtocol = ProtocolHash("PsddFKi32cMJ2qPjf43Qv5GDWLDPZb3T3bF6fLKiF5HtvHNU7aP"),
                    ),
                )
            ),
            jsonResponse = """
                [
                    {
                        "level": 28082,
                        "replacement_protocol": "PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt"
                    },
                    {
                        "level": 204761,
                        "replacement_protocol": "PsddFKi32cMJ2qPjf43Qv5GDWLDPZb3T3bF6fLKiF5HtvHNU7aP"
                    }
                ]
            """.trimIndent()
        )

    private data class RequestConfiguration<Req, Res>(val request: Req, val response: Res, val jsonRequest: String?, val jsonResponse: String)
    private fun <Res> RequestConfiguration(response: Res, jsonResponse: String): RequestConfiguration<Unit, Res> = RequestConfiguration(Unit, response, null, jsonResponse)
}