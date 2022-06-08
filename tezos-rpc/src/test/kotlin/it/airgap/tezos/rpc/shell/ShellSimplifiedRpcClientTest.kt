package it.airgap.tezos.rpc.shell

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.OperationHash
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.http.HttpParameter
import it.airgap.tezos.rpc.internal.rpcModule
import it.airgap.tezos.rpc.shell.chains.GetBlocksResponse
import it.airgap.tezos.rpc.shell.chains.GetBootstrappedStatusResponse
import it.airgap.tezos.rpc.shell.chains.GetChainIdResponse
import it.airgap.tezos.rpc.shell.injection.InjectOperationRequest
import it.airgap.tezos.rpc.shell.injection.InjectOperationResponse
import it.airgap.tezos.rpc.type.chain.RpcChainStatus
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import mockTezos
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ShellSimplifiedRpcClientTest {

    @MockK
    private lateinit var httpClientProvider : HttpClientProvider

    private lateinit var json: Json
    private lateinit var shellSimplifiedRpcClient: ShellSimplifiedRpcClient

    private val nodeUrl = "https://example.com"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val tezos = mockTezos(httpClientProvider = httpClientProvider)

        json = tezos.rpcModule.dependencyRegistry.json

        shellSimplifiedRpcClient = ShellSimplifiedRpcClient(
            tezos.rpcModule.dependencyRegistry.chains(nodeUrl),
            tezos.rpcModule.dependencyRegistry.injection(nodeUrl),
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should get blocks`() {
        data class Parameters(val length: UInt? = null, val head: BlockHash? = null, val minDate: String? = null)

        val chainId = "chainId"
        val parameters = listOf(
            Parameters(),
            Parameters(length = 1U),
            Parameters(head = BlockHash("BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE")),
            Parameters(minDate = "date"),
            Parameters(length = 1U, head = BlockHash("BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE"), minDate = "date"),
        )

        val (_, expectedResponse, _, jsonResponse) = chainBlocksGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            parameters.forEach {  parameters ->
                val (length, head, minDate) = parameters
                val expectedParameters = buildList<HttpParameter> {
                    length?.let { add("length" to it.toString()) }
                    head?.let { add("head" to it.base58) }
                    minDate?.let { add("min_date" to it) }
                }
                val response = runBlocking { shellSimplifiedRpcClient.getBlocks(chainId, length, head, minDate, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/blocks", "/", headers = headers, parameters = expectedParameters) }
            }
        }
    }

    @Test
    fun `should get chain id`() {
        val chainId = "chainId"

        val (_, expectedResponse, _, jsonResponse) = chainChainIdGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { shellSimplifiedRpcClient.getChainId(chainId, headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/chain_id", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should check if chain is bootstrapped`() {
        val chainId = "chainId"

        val (_, expectedResponse, _, jsonResponse) = chainIsBootstrappedGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { shellSimplifiedRpcClient.isBootstrapped(chainId, headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/is_bootstrapped", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should inject operation`() {
        data class Parameters(val async: Boolean? = null, val chain: ChainId? = null)

        val parameters = listOf(
            Parameters(),
            Parameters(async = true),
            Parameters(chain = ChainId("NetXdQprcVkpaWU")),
            Parameters(async = true, chain = ChainId("NetXdQprcVkpaWU")),
            Parameters(async = false),
        )
        val data = "a8a043507783594ede53ddbee766b4a3"

        val (_, expectedResponse, jsonRequest, jsonResponse) = operationPostRequestConfiguration(data)
        coEvery { httpClientProvider.post(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            parameters.forEach { parameters ->
                val (async, chain) = parameters
                val expectedParameters = buildList<HttpParameter> {
                    async?.takeIf { it }?.let { add("async" to null) }
                    chain?.let { add("chain" to chain.base58) }
                }
                val response = runBlocking { shellSimplifiedRpcClient.injectOperation(data, async, chain, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClientProvider.post("$nodeUrl/injection/operation", "/", headers = headers, parameters = expectedParameters, body = jsonRequest?.normalizeWith(json)) }
            }
        }
    }

    private val headers: List<List<HttpHeader>>
        get() = listOf(
            emptyList(),
            listOf("Authorization" to "Bearer")
        )

    private val chainBlocksGetRequestConfiguration: RequestConfiguration<Unit, GetBlocksResponse>
        get() = RequestConfiguration(
            response = GetBlocksResponse(
                listOf(
                    listOf(BlockHash("BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE")),
                    listOf(BlockHash("BLKvJKAGxvuVMWjjSJPjycWVGFza7W9SxBpYRgY4W7Vti1z5kFi")),
                    listOf(BlockHash("BMPisGb4HMtZcDhDR9Hd3SVRMpLWGc5huLe4fxecSy3ThpMG2sa")),
                    listOf(BlockHash("BMBRPrthqrT5SwYqPwYkfab29swv3gHgi1nxJSoXgW1LQCee4Rk")),
                    listOf(BlockHash("BMZhH384DnfD5rBFQm2tPr3Vj2S99qPazsVb8mRuxidC8fRFTkM")),
                    listOf(BlockHash("BLWFEynnUXR5vVN47D7fnrfrcE8vivQN6nuT3HB8w6L4frxx2fQ")),
                )
            ),
            jsonResponse = """
                [
                    [
                        "BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE"
                    ],
                    [
                        "BLKvJKAGxvuVMWjjSJPjycWVGFza7W9SxBpYRgY4W7Vti1z5kFi"
                    ],
                    [
                        "BMPisGb4HMtZcDhDR9Hd3SVRMpLWGc5huLe4fxecSy3ThpMG2sa"
                    ],
                    [
                        "BMBRPrthqrT5SwYqPwYkfab29swv3gHgi1nxJSoXgW1LQCee4Rk"
                    ],
                    [
                        "BMZhH384DnfD5rBFQm2tPr3Vj2S99qPazsVb8mRuxidC8fRFTkM"
                    ],
                    [
                        "BLWFEynnUXR5vVN47D7fnrfrcE8vivQN6nuT3HB8w6L4frxx2fQ"
                    ]
                ]
            """.trimIndent(),
        )

    private val chainChainIdGetRequestConfiguration: RequestConfiguration<Unit, GetChainIdResponse>
        get() = RequestConfiguration(
            response = GetChainIdResponse(ChainId("NetXdQprcVkpaWU")),
            jsonResponse = """
                "NetXdQprcVkpaWU"
            """.trimIndent(),
        )

    private val chainIsBootstrappedGetRequestConfiguration: RequestConfiguration<Unit, GetBootstrappedStatusResponse>
        get() = RequestConfiguration(
            response = GetBootstrappedStatusResponse(bootstrapped = true, RpcChainStatus.Synced),
            jsonResponse = """
                {
                    "bootstrapped": true,
                    "sync_state": "synced"
                }
            """.trimIndent()
        )

    private fun operationPostRequestConfiguration(data: String): RequestConfiguration<InjectOperationRequest, InjectOperationResponse> =
        RequestConfiguration(
            request = InjectOperationRequest(data),
            response = InjectOperationResponse(OperationHash("oooRdEmr6yG7GPjE77N6cFCF2JTyDvkdkCMFqNSe1S67ZKG9Pvn")),
            jsonRequest = """
                "$data"
            """.trimIndent(),
            jsonResponse = """
                "oooRdEmr6yG7GPjE77N6cFCF2JTyDvkdkCMFqNSe1S67ZKG9Pvn"
            """.trimIndent(),
        )

    private data class RequestConfiguration<Req, Res>(val request: Req, val response: Res, val jsonRequest: String?, val jsonResponse: String)
    private fun <Res> RequestConfiguration(response: Res, jsonResponse: String): RequestConfiguration<Unit, Res> = RequestConfiguration(Unit, response, null, jsonResponse)
}