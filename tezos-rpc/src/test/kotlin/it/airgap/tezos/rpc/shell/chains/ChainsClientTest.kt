package it.airgap.tezos.rpc.shell.chains

import io.mockk.*
import io.mockk.impl.annotations.MockK
import it.airgap.tezos.core.internal.converter.StringToSignatureConverter
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.active.block.GetBlockResponse
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.http.HttpParameter
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.internal.serializer.rpcJson
import it.airgap.tezos.rpc.type.RpcError
import it.airgap.tezos.rpc.type.block.RpcBlock
import it.airgap.tezos.rpc.type.block.RpcBlockHeader
import it.airgap.tezos.rpc.type.block.RpcInvalidBlock
import it.airgap.tezos.rpc.type.chain.RpcChainStatus
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import mockTezosSdk
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ChainsClientTest {

    @MockK
    private lateinit var dependencyRegistry: DependencyRegistry

    @MockK
    private lateinit var httpClientProvider : HttpClientProvider

    private lateinit var json: Json
    private lateinit var httpClient: HttpClient
    private lateinit var chainsClient: ChainsClient

    private val nodeUrl = "https://example.com"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        every { dependencyRegistry.core().stringToSignatureConverter } returns StringToSignatureConverter()

        json = Json(from = rpcJson) {
            prettyPrint = true
        }
        httpClient = HttpClient(httpClientProvider, json)
        chainsClient = ChainsClient(nodeUrl, httpClient)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should call PATCH on 'chains - $chain_id'`() {
        val chainId = "chainId"
        val bootstrapped = true

        val (expectedRequest, expectedResponse, jsonRequest, jsonResponse) = chainPatchRequestConfiguration(bootstrapped)
        coEvery { httpClientProvider.patch(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { chainsClient(chainId).patch(bootstrapped, headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.patch("$nodeUrl/chains/$chainId", "/", headers = headers, request = expectedRequest) }
            coVerify { httpClientProvider.patch("$nodeUrl/chains/$chainId", "/", headers = headers, parameters = emptyList(), body = jsonRequest?.normalizeWith(json)) }
        }
    }

    @Test
    fun `should call GET on 'chains - $chain_id - blocks'`() {
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
                val response = runBlocking { chainsClient(chainId).blocks.get(length, head, minDate, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClient.get("$nodeUrl/chains/$chainId/blocks", "/", headers = headers, parameters = expectedParameters) }
                coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/blocks", "/", headers = headers, parameters = expectedParameters) }
            }
        }
    }

    @Test
    fun `should call GET on 'chains - $chain_id - blocks - $block_id'`() {
        val chainId = "chainId"
        val blockId = "blockId"

        val (_, expectedResponse, _, jsonResponse) = chainBlocksBlockGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { chainsClient(chainId).blocks(blockId).get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/chains/$chainId/blocks/$blockId", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/blocks/$blockId", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'chains - $chain_id - chain_id'`() {
        val chainId = "chainId"

        val (_, expectedResponse, _, jsonResponse) = chainChainIdGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { chainsClient(chainId).chainId.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/chains/$chainId/chain_id", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/chain_id", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'chains - $chain_id - invalid_blocks'`() {
        val chainId = "chainId"

        val (_, expectedResponse, _, jsonResponse) = chainInvalidBlocksGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { chainsClient(chainId).invalidBlocks.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/chains/$chainId/invalid_blocks", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/invalid_blocks", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'chains - $chain_id - invalid_blocks - $block_hash'`() {
        val chainId = "chainId"
        val blockHash = "blockHash"

        val (_, expectedResponse, _, jsonResponse) = chainInvalidBlocksBlockGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { chainsClient(chainId).invalidBlocks(blockHash).get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/chains/$chainId/invalid_blocks/$blockHash", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/invalid_blocks/$blockHash", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'chains - $chain_id - is_bootstrapped'`() {
        val chainId = "chainId"

        val (_, expectedResponse, _, jsonResponse) = chainIsBootstrappedGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { chainsClient(chainId).isBootstrapped.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/chains/$chainId/is_bootstrapped", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/is_bootstrapped", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'chains - $chain_id - levels - caboose'`() {
        val chainId = "chainId"

        val (_, expectedResponse, _, jsonResponse) = chainLevelsCabooseGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { chainsClient(chainId).levels.caboose.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/chains/$chainId/levels/caboose", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/levels/caboose", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'chains - $chain_id - levels - checkpoint'`() {
        val chainId = "chainId"

        val (_, expectedResponse, _, jsonResponse) = chainLevelsCheckpointGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { chainsClient(chainId).levels.checkpoint.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/chains/$chainId/levels/checkpoint", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/levels/checkpoint", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'chains - $chain_id - levels - savepoint'`() {
        val chainId = "chainId"

        val (_, expectedResponse, _, jsonResponse) = chainLevelsSavepointGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { chainsClient(chainId).levels.savepoint.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/chains/$chainId/levels/savepoint", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/chains/$chainId/levels/savepoint", "/", headers = headers, parameters = emptyList()) }
        }
    }

    private val headers: List<List<HttpHeader>>
        get() = listOf(
            emptyList(),
            listOf("Authorization" to "Bearer")
        )

    private fun chainPatchRequestConfiguration(bootstrapped: Boolean): RequestConfiguration<SetBootstrappedRequest, SetBootstrappedResponse> =
        RequestConfiguration(
            request = SetBootstrappedRequest(bootstrapped),
            response = SetBootstrappedResponse,
            jsonRequest = """
                {
                  "bootstrapped": $bootstrapped
                }
            """.trimIndent(),
            jsonResponse = "",
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

    private val chainBlocksBlockGetRequestConfiguration: RequestConfiguration<Unit, GetBlockResponse>
        get() = RequestConfiguration(
            response = GetBlockResponse(
                RpcBlock(
                    protocol = ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                    chainId = ChainId("NetXnHfVqm9iesp"),
                    hash = BlockHash("BKz4CFd8FhgSLJRp7muW6EwSMKMBaCmyUgzTzrSGQiwximB5dbw"),
                    header = RpcBlockHeader(
                        level = 376499,
                        proto = 2U,
                        predecessor = BlockHash("BLJNdTWekHnVbsbgxwuXAdhzEg84w1Jn73nVjdzbSnxZikJSsGm"),
                        timestamp = Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                        validationPass = 4U,
                        operationsHash = OperationListListHash("LLoZzrsq6NfsHydhDCBqXJyBTosRR9R9xNXTjm4PCtZ2juiEv9VBD"),
                        fitness = listOf(
                            "02",
                            "0005beb3",
                            "",
                            "ffffffff",
                            "00000000",
                        ),
                        context = ContextHash("CoVuxLsBWaDZXA6jdcSuU6NW3pzrsC1Z6uggBb71e9PS8XAEgWrj"),
                        payloadHash = BlockPayloadHash("vh3Uk8raNVcLYrfT4QeiqykTjPxQHyk2ZpgH8B2XJNXSURujDxt8"),
                        payloadRound = 0,
                        proofOfWorkNonce = "61fed54075090100",
                        liquidityBakingEscapeVote = false,
                        signature = GenericSignature("sigZ3uvQ5oa3pxSZkPjASKFYvHtph3S7VN8mbUXjSAUtpsaWe736Aa2B5Tr8VpeG3b78FNZJpDoSWTQiTYmeuw4WfniEbFrx"),
                    ),
                    operations = emptyList(),
                )
            ),
            jsonResponse = """
                {
                    "protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                    "chain_id": "NetXnHfVqm9iesp",
                    "hash": "BKz4CFd8FhgSLJRp7muW6EwSMKMBaCmyUgzTzrSGQiwximB5dbw",
                    "header": {
                        "level": 376499,
                        "proto": 2,
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
                        "payload_hash": "vh3Uk8raNVcLYrfT4QeiqykTjPxQHyk2ZpgH8B2XJNXSURujDxt8",
                        "payload_round": 0,
                        "proof_of_work_nonce": "61fed54075090100",
                        "liquidity_baking_escape_vote": false,
                        "signature": "sigZ3uvQ5oa3pxSZkPjASKFYvHtph3S7VN8mbUXjSAUtpsaWe736Aa2B5Tr8VpeG3b78FNZJpDoSWTQiTYmeuw4WfniEbFrx"
                    },
                    "operations": []
                }
            """.trimIndent(),
        )

    private val chainChainIdGetRequestConfiguration: RequestConfiguration<Unit, GetChainIdResponse>
        get() = RequestConfiguration(
            response = GetChainIdResponse(ChainId("NetXdQprcVkpaWU")),
            jsonResponse = """
                "NetXdQprcVkpaWU"
            """.trimIndent(),
        )

    private val chainInvalidBlocksGetRequestConfiguration: RequestConfiguration<Unit, GetInvalidBlocksResponse>
        get() = RequestConfiguration(
            response = GetInvalidBlocksResponse(listOf(
                RpcInvalidBlock(
                    block = BlockHash("BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A"),
                    level = 363766,
                    errors = listOf(
                        RpcError(
                            kind = RpcError.Kind.Permanent,
                            id = "validator.invalid_block",
                            details = buildMap {
                                put("invalid_block", "BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A")
                                put("error", "outdated_operation")
                                put("operation", "oozn6N2454Yx2Gy17dTfbGbcfyE7EhAeddvL7kMnfHhGnsPzFAp")
                                put("originating_block", "BLXcio3Rbp4kPZKxECMx5dncaXViCVKnVJRvf6oQqp7UUkxghMK")
                            }
                        )
                    )
                )
            )),
            jsonResponse = """
                [
                    {
                        "block": "BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A",
                        "level": 363766,
                        "errors": [
                            {
                                "kind": "permanent",
                                "id": "validator.invalid_block",
                                "invalid_block": "BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A",
                                "error": "outdated_operation",
                                "operation": "oozn6N2454Yx2Gy17dTfbGbcfyE7EhAeddvL7kMnfHhGnsPzFAp",
                                "originating_block": "BLXcio3Rbp4kPZKxECMx5dncaXViCVKnVJRvf6oQqp7UUkxghMK"
                            }
                        ]
                    }
                ]
            """.trimIndent()
        )

    private val chainInvalidBlocksBlockGetRequestConfiguration: RequestConfiguration<Unit, GetInvalidBlockResponse>
        get() = RequestConfiguration(
            response = GetInvalidBlockResponse(
                RpcInvalidBlock(
                    block = BlockHash("BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A"),
                    level = 363766,
                    errors = listOf(
                        RpcError(
                            kind = RpcError.Kind.Permanent,
                            id = "validator.invalid_block",
                            details = buildMap {
                                put("invalid_block", "BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A")
                                put("error", "outdated_operation")
                                put("operation", "oozn6N2454Yx2Gy17dTfbGbcfyE7EhAeddvL7kMnfHhGnsPzFAp")
                                put("originating_block", "BLXcio3Rbp4kPZKxECMx5dncaXViCVKnVJRvf6oQqp7UUkxghMK")
                            }
                        )
                    )
                )
            ),
            jsonResponse = """
                {
                    "block": "BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A",
                    "level": 363766,
                    "errors": [
                        {
                            "kind": "permanent",
                            "id": "validator.invalid_block",
                            "invalid_block": "BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A",
                            "error": "outdated_operation",
                            "operation": "oozn6N2454Yx2Gy17dTfbGbcfyE7EhAeddvL7kMnfHhGnsPzFAp",
                            "originating_block": "BLXcio3Rbp4kPZKxECMx5dncaXViCVKnVJRvf6oQqp7UUkxghMK"
                        }
                    ]
                }
            """.trimIndent()
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

    private val chainLevelsCabooseGetRequestConfiguration: RequestConfiguration<Unit, GetCabooseResponse>
        get() = RequestConfiguration(
            response = GetCabooseResponse(BlockHash("BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A"), 2147483647),
            jsonResponse = """
                {
                    "block_hash": "BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A",
                    "level": 2147483647
                }
            """.trimIndent()
        )

    private val chainLevelsCheckpointGetRequestConfiguration: RequestConfiguration<Unit, GetCheckpointResponse>
        get() = RequestConfiguration(
            response = GetCheckpointResponse(BlockHash("BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A"), 2147483647),
            jsonResponse = """
                {
                    "block_hash": "BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A",
                    "level": 2147483647
                }
            """.trimIndent()
        )

    private val chainLevelsSavepointGetRequestConfiguration: RequestConfiguration<Unit, GetSavepointResponse>
        get() = RequestConfiguration(
            response = GetSavepointResponse(BlockHash("BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A"), 2147483647),
            jsonResponse = """
                {
                    "block_hash": "BMTJkwYz93C7QoAdu5utKyYTiEhJL6YoC6LwBjDMEVdUgY4rn7A",
                    "level": 2147483647
                }
            """.trimIndent()
        )

    private data class RequestConfiguration<Req, Res>(val request: Req, val response: Res, val jsonRequest: String?, val jsonResponse: String)
    private fun <Res> RequestConfiguration(response: Res, jsonResponse: String): RequestConfiguration<Unit, Res> = RequestConfiguration(Unit, response, null, jsonResponse)
}