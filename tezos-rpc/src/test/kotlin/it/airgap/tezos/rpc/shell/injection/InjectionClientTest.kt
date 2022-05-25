package it.airgap.tezos.rpc.shell.injection

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.OperationHash
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.http.HttpParameter
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.internal.rpcModule
import it.airgap.tezos.rpc.type.operation.RpcInjectableOperation
import it.airgap.tezos.rpc.type.protocol.RpcProtocolComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mockTezos
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class InjectionClientTest {

    @MockK
    private lateinit var httpClientProvider : HttpClientProvider

    private lateinit var json: Json
    private lateinit var httpClient: HttpClient

    private lateinit var injectionClient: InjectionClient

    private val nodeUrl = "https://example.com"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val tezos = mockTezos(httpClientProvider = httpClientProvider)

        json = tezos.rpcModule.dependencyRegistry.json
        httpClient = tezos.rpcModule.dependencyRegistry.httpClient

        injectionClient = InjectionClient(nodeUrl, httpClient)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should call POST on 'injection - block'`() {
        data class Parameters(val async: Boolean? = null, val force: Boolean? = null, val chain: ChainId? = null)

        val parameters = listOf(
            Parameters(),
            Parameters(async = true),
            Parameters(force = true),
            Parameters(chain = ChainId("NetXdQprcVkpaWU")),
            Parameters(async = true, force = true, chain = ChainId("NetXdQprcVkpaWU")),
            Parameters(async = false, force = false),
        )
        val data = "a8a043507783594ede53ddbee766b4a3"
        val operations = listOf(
            listOf(
                RpcInjectableOperation(
                    branch = BlockHash("BKz4CFd8FhgSLJRp7muW6EwSMKMBaCmyUgzTzrSGQiwximB5dbw"),
                    data = "50b8461382a4826d0162f8dc814c5a9c",
                ),
            )
        )

        val (expectedRequest, expectedResponse, jsonRequest ,jsonResponse) = blockPostRequestConfiguration(data, operations)
        coEvery { httpClientProvider.post(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            parameters.forEach { parameters ->
                val (async, force, chain) = parameters
                val expectedParameters = buildList<HttpParameter> {
                    async?.takeIf { it }?.let { add("async" to null) }
                    force?.takeIf { it }?.let { add("force" to null) }
                    chain?.let { add("chain" to chain.base58) }
                }
                val response = runBlocking { injectionClient.block.post(data, operations, async, force, chain, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClient.post("$nodeUrl/injection/block", "/", headers = headers, parameters = expectedParameters, request = expectedRequest) }
                coVerify { httpClientProvider.post("$nodeUrl/injection/block", "/", headers = headers, parameters = expectedParameters, body = jsonRequest?.normalizeWith(json)) }
            }
        }
    }

    @Test
    fun `should call POST on 'injection - operation'`() {
        data class Parameters(val async: Boolean? = null, val chain: ChainId? = null)

        val parameters = listOf(
            Parameters(),
            Parameters(async = true),
            Parameters(chain = ChainId("NetXdQprcVkpaWU")),
            Parameters(async = true, chain = ChainId("NetXdQprcVkpaWU")),
            Parameters(async = false),
        )
        val data = "a8a043507783594ede53ddbee766b4a3"

        val (expectedRequest, expectedResponse, jsonRequest, jsonResponse) = operationPostRequestConfiguration(data)
        coEvery { httpClientProvider.post(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            parameters.forEach { parameters ->
                val (async, chain) = parameters
                val expectedParameters = buildList<HttpParameter> {
                    async?.takeIf { it }?.let { add("async" to null) }
                    chain?.let { add("chain" to chain.base58) }
                }
                val response = runBlocking { injectionClient.operation.post(data, async, chain, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClient.post("$nodeUrl/injection/operation", "/", headers = headers, parameters = expectedParameters, request = expectedRequest) }
                coVerify { httpClientProvider.post("$nodeUrl/injection/operation", "/", headers = headers, parameters = expectedParameters, body = jsonRequest?.normalizeWith(json)) }
            }
        }
    }

    @Test
    fun `should call POST on 'injection - protocol'`() {
        data class Parameters(val async: Boolean? = null)

        val parameters = listOf(
            Parameters(),
            Parameters(async = true),
            Parameters(async = false),
        )
        val expectedEnvVersion = 1.toUShort()
        val components = listOf<RpcProtocolComponent>()

        val (expectedRequest, expectedResponse, jsonRequest, jsonResponse) = protocolPostRequestConfiguration(expectedEnvVersion, components)
        coEvery { httpClientProvider.post(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            parameters.forEach { parameters ->
                val (async) = parameters
                val expectedParameters = buildList<HttpParameter> {
                    async?.takeIf { it }?.let { add("async" to null) }
                }
                val response = runBlocking { injectionClient.protocol.post(expectedEnvVersion, components, async, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClient.post("$nodeUrl/injection/protocol", "/", headers = headers, parameters = expectedParameters, request = expectedRequest) }
                coVerify { httpClientProvider.post("$nodeUrl/injection/protocol", "/", headers = headers, parameters = expectedParameters, body = jsonRequest?.normalizeWith(json)) }
            }
        }
    }

    private val headers: List<List<HttpHeader>>
        get() = listOf(
            emptyList(),
            listOf("Authorization" to "Bearer")
        )

    private fun blockPostRequestConfiguration(data: String, operations: List<List<RpcInjectableOperation>>): RequestConfiguration<InjectBlockRequest, InjectBlockResponse> =
        RequestConfiguration(
            request = InjectBlockRequest(data, operations),
            response = InjectBlockResponse(BlockHash("BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE")),
            jsonRequest = """
                {
                  "data": "$data",
                  "operations": ${json.encodeToString(operations)}
                }
            """.trimIndent(),
            jsonResponse = """
                "BLnJawGEsLm4H3o6uoCtB5Di99QVNGtrtVC7rhBHqUbY6TqwtdE"
            """.trimIndent(),
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

    private fun protocolPostRequestConfiguration(expectedEnvVersion: UShort, components: List<RpcProtocolComponent>): RequestConfiguration<InjectProtocolRequest, InjectProtocolResponse> =
        RequestConfiguration(
            request = InjectProtocolRequest(expectedEnvVersion, components),
            response = InjectProtocolResponse(ProtocolHash("PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt")),
            jsonRequest = """
                {
                  "expected_env_version": $expectedEnvVersion,
                  "components": ${json.encodeToString(components)}
                }
            """.trimIndent(),
            jsonResponse = """
                "PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt"
            """.trimIndent()
        )

    private data class RequestConfiguration<Req, Res>(val request: Req, val response: Res, val jsonRequest: String?, val jsonResponse: String)
}