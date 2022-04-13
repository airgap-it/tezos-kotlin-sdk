package it.airgap.tezos.rpc.shell.monitor

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.http.HttpParameter
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.internal.serializer.rpcJson
import it.airgap.tezos.rpc.type.chain.RpcActiveChain
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MonitorClientTest {

    @MockK
    private lateinit var httpClientProvider : HttpClientProvider

    private lateinit var json: Json
    private lateinit var httpClient: HttpClient
    private lateinit var monitorClient: MonitorClient

    private val nodeUrl = "https://example.com"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        json = Json(from = rpcJson) {
            prettyPrint = true
        }
        httpClient = HttpClient(httpClientProvider, json)
        monitorClient = MonitorClient(nodeUrl, httpClient)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should call GET on 'monitor - active_chains'`() {
        val (_, expectedResponse, _, jsonResponse) = monitorActiveChainsRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { monitorClient.activeChains.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/monitor/active_chains", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/monitor/active_chains", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'monitor - bootstrapped'`() {
        val (_, expectedResponse, _, jsonResponse) = monitorBootstrappedRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { monitorClient.bootstrapped.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/monitor/bootstrapped", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/monitor/bootstrapped", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'monitor - heads - $chain_id'`() {
        data class Parameters(val nextProtocol: ProtocolHash? = null)

        val chainId = "chainId"
        val parameters = listOf(
            Parameters(),
            Parameters(nextProtocol = ProtocolHash("PsddFKi32cMJ2qPjf43Qv5GDWLDPZb3T3bF6fLKiF5HtvHNU7aP")),
        )

        val (_, expectedResponse, _, jsonResponse) = monitorHeadsHeadRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            parameters.forEach { parameters ->
                val (nextProtocol) = parameters
                val expectedParameters = buildList<HttpParameter> {
                    nextProtocol?.let { add("next_protocol" to it.base58) }
                }
                val response = runBlocking { monitorClient.heads(chainId).get(nextProtocol, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClient.get("$nodeUrl/monitor/heads/$chainId", "/", headers = headers, parameters = expectedParameters) }
                coVerify { httpClientProvider.get("$nodeUrl/monitor/heads/$chainId", "/", headers = headers, parameters = expectedParameters) }
            }
        }
    }

    @Test
    fun `should call GET on 'monitor - protocols'`() {
        val (_, expectedResponse, _, jsonResponse) = monitorProtocolsRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { monitorClient.protocols.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/monitor/protocols", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/monitor/protocols", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'monitor - valid_blocks'`() {
        data class Parameters(val protocol: ProtocolHash? = null, val nextProtocol: ProtocolHash? = null, val chain: String? = null)

        val parameters = listOf(
            Parameters(),
            Parameters(protocol = ProtocolHash("PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt")),
            Parameters(nextProtocol = ProtocolHash("PsddFKi32cMJ2qPjf43Qv5GDWLDPZb3T3bF6fLKiF5HtvHNU7aP")),
            Parameters(chain = "main"),
            Parameters(
                protocol = ProtocolHash("PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt"),
                nextProtocol = ProtocolHash("PsddFKi32cMJ2qPjf43Qv5GDWLDPZb3T3bF6fLKiF5HtvHNU7aP"),
                chain = "main",
            ),
        )

        val (_, expectedResponse, _, jsonResponse) = monitorValidBlocksRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            parameters.forEach { parameters ->
                val (protocol, nextProtocol, chain) = parameters
                val expectedParameters = buildList<HttpParameter> {
                    protocol?.let { add("protocol" to it.base58) }
                    nextProtocol?.let { add("next_protocol" to it.base58) }
                    chain?.let { add("chain" to it) }
                }
                val response = runBlocking { monitorClient.validBlocks.get(protocol, nextProtocol, chain, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClient.get("$nodeUrl/monitor/valid_blocks", "/", headers = headers, parameters = expectedParameters) }
                coVerify { httpClientProvider.get("$nodeUrl/monitor/valid_blocks", "/", headers = headers, parameters = expectedParameters) }
            }
        }
    }

    private val headers: List<List<HttpHeader>>
        get() = listOf(
            emptyList(),
            listOf("Authorization" to "Bearer")
        )

    private val monitorActiveChainsRequestConfiguration: RequestConfiguration<Unit, MonitorActiveChainsResponse>
        get() = RequestConfiguration(
            response = MonitorActiveChainsResponse(
                listOf(
                    RpcActiveChain.Main(ChainId("NetXdQprcVkpaWU")),
                    RpcActiveChain.Test(
                        chainId = ChainId("NetXdQprcVkpaWU"),
                        testProtocol = ProtocolHash("PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt"),
                        expirationDate = Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    RpcActiveChain.Stopping(ChainId("NetXdQprcVkpaWU")),
                )
            ),
            jsonResponse = """                	
                [
                    {
                        "chain_id": "NetXdQprcVkpaWU"
                    },
                    {
                        "chain_id": "NetXdQprcVkpaWU",
                        "test_protocol": "PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt",
                        "expiration_date": "2022-04-12T13:07:00Z"
                    },
                    {
                        "stopping": "NetXdQprcVkpaWU"
                    }
                ]
            """.trimIndent(),
        )

    private val monitorBootstrappedRequestConfiguration: RequestConfiguration<Unit, MonitorBootstrappedResponse>
        get() = RequestConfiguration(
            response = MonitorBootstrappedResponse(
                block = BlockHash("BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE"),
                timestamp = Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
            ),
            jsonResponse = """
                {
                    "block": "BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE",
                    "timestamp": "2022-04-12T13:07:00Z"
                }
            """.trimIndent(),
        )

    private val monitorHeadsHeadRequestConfiguration: RequestConfiguration<Unit, MonitorHeadResponse>
        get() = RequestConfiguration(
            response = MonitorHeadResponse(
                hash = BlockHash("BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE"),
                level = 2147483647,
                proto = 255U,
                predecessor = BlockHash("BLJNdTWekHnVbsbgxwuXAdhzEg84w1Jn73nVjdzbSnxZikJSsGm"),
                timestamp = Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                validationPass = 4U,
                operationsHash = OperationListListHash("LLoZzrsq6NfsHydhDCBqXJyBTosRR9R9xNXTjm4PCtZ2juiEv9VBD"),
                fitness = listOf(
                    HexString("02"),
                    HexString("0005beb3"),
                    HexString(""),
                    HexString("ffffffff"),
                    HexString("00000000"),
                ),
                context = ContextHash("CoVuxLsBWaDZXA6jdcSuU6NW3pzrsC1Z6uggBb71e9PS8XAEgWrj"),
                protocolData = HexString("ffffffff"),
            ),
            jsonResponse = """
                {
                    "hash": "BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE",
                    "level": 2147483647,
                    "proto": 255,
                    "predecessor": "BLJNdTWekHnVbsbgxwuXAdhzEg84w1Jn73nVjdzbSnxZikJSsGm",
                    "timestamp": "2022-04-12T13:07:00Z",
                    "validation_pass": 4,
                    "operations_hash": "LLoZzrsq6NfsHydhDCBqXJyBTosRR9R9xNXTjm4PCtZ2juiEv9VBD",
                    "fitness": [
                        "02",
                        "0005beb3",
                        "",
                        "ffffffff",
                        "00000000"
                    ],
                    "context": "CoVuxLsBWaDZXA6jdcSuU6NW3pzrsC1Z6uggBb71e9PS8XAEgWrj",
                    "protocol_data": "ffffffff"
                }
            """.trimIndent()
        )

    private val monitorProtocolsRequestConfiguration: RequestConfiguration<Unit, MonitorProtocolsResponse>
        get() = RequestConfiguration(
            response = MonitorProtocolsResponse(ProtocolHash("PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt")),
            jsonResponse = """
                "PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt"
            """.trimIndent()
        )

    private val monitorValidBlocksRequestConfiguration: RequestConfiguration<Unit, MonitorValidBlocksResponse>
        get() = RequestConfiguration(
            response = MonitorValidBlocksResponse(
                chainId = ChainId("NetXdQprcVkpaWU"),
                hash = BlockHash("BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE"),
                level = 2147483647,
                proto = 255U,
                predecessor = BlockHash("BLJNdTWekHnVbsbgxwuXAdhzEg84w1Jn73nVjdzbSnxZikJSsGm"),
                timestamp = Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                validationPass = 4U,
                operationsHash = OperationListListHash("LLoZzrsq6NfsHydhDCBqXJyBTosRR9R9xNXTjm4PCtZ2juiEv9VBD"),
                fitness = listOf(
                    HexString("02"),
                    HexString("0005beb3"),
                    HexString(""),
                    HexString("ffffffff"),
                    HexString("00000000"),
                ),
                context = ContextHash("CoVuxLsBWaDZXA6jdcSuU6NW3pzrsC1Z6uggBb71e9PS8XAEgWrj"),
                protocolData = HexString("ffffffff"),
            ),
            jsonResponse = """
                {
                    "chain_id": "NetXdQprcVkpaWU",
                    "hash": "BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE",
                    "level": 2147483647,
                    "proto": 255,
                    "predecessor": "BLJNdTWekHnVbsbgxwuXAdhzEg84w1Jn73nVjdzbSnxZikJSsGm",
                    "timestamp": "2022-04-12T13:07:00Z",
                    "validation_pass": 4,
                    "operations_hash": "LLoZzrsq6NfsHydhDCBqXJyBTosRR9R9xNXTjm4PCtZ2juiEv9VBD",
                    "fitness": [
                        "02",
                        "0005beb3",
                        "",
                        "ffffffff",
                        "00000000"
                    ],
                    "context": "CoVuxLsBWaDZXA6jdcSuU6NW3pzrsC1Z6uggBb71e9PS8XAEgWrj",
                    "protocol_data": "ffffffff"
                }
            """.trimIndent()
        )

    private data class RequestConfiguration<Req, Res>(val request: Req, val response: Res, val jsonRequest: String?, val jsonResponse: String)
    private fun <Res> RequestConfiguration(response: Res, jsonResponse: String): RequestConfiguration<Unit, Res> = RequestConfiguration(Unit, response, null, jsonResponse)
}