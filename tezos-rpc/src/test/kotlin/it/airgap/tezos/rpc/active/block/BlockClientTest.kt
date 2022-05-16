package it.airgap.tezos.rpc.active.block

import io.mockk.*
import io.mockk.impl.annotations.MockK
import it.airgap.tezos.core.internal.converter.encoded.*
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonInstruction
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.http.HttpParameter
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.internal.serializer.rpcJson
import it.airgap.tezos.rpc.type.block.RpcBlock
import it.airgap.tezos.rpc.type.block.RpcBlockHeader
import it.airgap.tezos.rpc.type.block.RpcFullBlockHeader
import it.airgap.tezos.rpc.type.constants.RpcConstants
import it.airgap.tezos.rpc.type.contract.RpcScript
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing
import it.airgap.tezos.rpc.type.operation.*
import it.airgap.tezos.rpc.type.primitive.RpcRatio
import it.airgap.tezos.rpc.type.sapling.RpcSaplingCiphertext
import it.airgap.tezos.rpc.type.sapling.RpcSaplingStateDiff
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import mockTezosSdk
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class BlockClientTest {

    @MockK
    private lateinit var dependencyRegistry: DependencyRegistry

    @MockK
    private lateinit var httpClientProvider : HttpClientProvider

    private lateinit var json: Json
    private lateinit var httpClient: HttpClient
    private lateinit var blockClient: BlockClient

    private val nodeUrl = "https://example.com/chains/chainId/blocks"
    private val blockId = "blockId"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        every { dependencyRegistry.core().stringToAddressConverter } returns StringToAddressConverter()
        every { dependencyRegistry.core().stringToImplicitAddressConverter } returns StringToImplicitAddressConverter()
        every { dependencyRegistry.core().stringToPublicKeyConverter } returns StringToPublicKeyConverter()
        every { dependencyRegistry.core().stringToPublicKeyHashConverter } returns StringToPublicKeyHashConverter()
        every { dependencyRegistry.core().stringToSignatureConverter } returns StringToSignatureConverter()

        json = Json(from = rpcJson) {
            prettyPrint = true
        }
        httpClient = HttpClient(httpClientProvider, json)
        blockClient = BlockClient(nodeUrl, blockId, httpClient)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should call GET on '_ - $block_id'`() {
        val (_, expectedResponse, _, jsonResponse) = getRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - big_maps - $big_map_id'`() {
        val bigMapId = "bigMapId"

        val (_, expectedResponse, _, jsonResponse) = contextBigMapsBigMapGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.bigMaps(bigMapId).get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/big_maps/$bigMapId", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/big_maps/$bigMapId", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - big_maps - $big_map_id - $script_expr'`() {
        val bigMapId = "bigMapId"
        val scriptExpr = ScriptExprHash("exprtzaFcULaeXHPaufCkPW9BkwiajuxeboiHRzseQxfnxq1cxxeQu")

        val (_, expectedResponse, _, jsonResponse) = contextBigMapsBigMapValueGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.bigMaps(bigMapId)(scriptExpr).get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/big_maps/$bigMapId/${scriptExpr.base58}", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/big_maps/$bigMapId/${scriptExpr.base58}", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - constants'`() {
        val (_, expectedResponse, _, jsonResponse) = contextConstantsGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.constants.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/constants", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/constants", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - contracts - $contract_id'`() {
        val contractId = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextContractsContractGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - contracts - $contract_id - balance'`() {
        val contractId = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextContractsContractBalanceGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).balance.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/balance", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/balance", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - contracts - $contract_id - counter'`() {
        val contractId = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextContractsContractCounterGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).counter.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/counter", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/counter", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - contracts - $contract_id - delegate'`() {
        val contractId = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextContractsContractDelegateGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).delegate.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/delegate", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/delegate", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - contracts - $contract_id - entrypoints'`() {
        val contractId = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc")

        val (_, expectedResponse, _, jsonResponse) = contextContractsContractEntrypointsGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).entrypoints.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/entrypoints", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/entrypoints", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - contracts - $contract_id - entrypoints - $string'`() {
        val contractId = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc")
        val entrypoint = "entrypoint"

        val (_, expectedResponse, _, jsonResponse) = contextContractsContractEntrypointsEntrypointGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).entrypoints(entrypoint).get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/entrypoints/entrypoint", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/entrypoints/entrypoint", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - contracts - $contract_id - manager_key'`() {
        val contractId = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextContractsContractManagerKeyGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).managerKey.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/manager_key", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/manager_key", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - contracts - $contract_id - script'`() {
        val contractId = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc")

        val (_, expectedResponse, _, jsonResponse) = contextContractsContractScriptGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).script.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/script", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/script", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call POST on '_ - $block_id - context - contracts - $contract_id - script - normalized'`() {
        val contractId = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc")

        val (expectedRequest, expectedResponse, jsonRequest, jsonResponse) = contextContractsContractScriptNormalizedPostRequestConfiguration
        coEvery { httpClientProvider.post(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).script.normalized.post(expectedRequest.unparsingMode, headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.post("$nodeUrl/$blockId/context/contracts/${contractId.base58}/script/normalized", "/", headers = headers, request = expectedRequest) }
            coVerify { httpClientProvider.post("$nodeUrl/$blockId/context/contracts/${contractId.base58}/script/normalized", "/", headers = headers, parameters = emptyList(), body = jsonRequest?.normalizeWith(json)) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - contracts - $contract_id - single_sapling_get_diff'`() {
        data class Parameters(val commitmentOffset: ULong? = null, val nullifierOffset: ULong? = null)

        val contractId = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc")
        val parameters = listOf(
            Parameters(),
            Parameters(commitmentOffset = 1U),
            Parameters(nullifierOffset = 2U),
            Parameters(commitmentOffset = 1U, nullifierOffset = 2U),
        )

        val (_, expectedResponse, _, jsonResponse) = contextContractsContractSaplingStateDiffGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            parameters.forEach {  parameters ->
                val (commitmentOffset, nullifierOffset) = parameters
                val expectedParameters = buildList<HttpParameter> {
                    commitmentOffset?.let { add("offset_commitment" to it.toString()) }
                    nullifierOffset?.let { add("offset_nullifier" to it.toString()) }
                }
                val response = runBlocking { blockClient.context.contracts(contractId).singleSaplingGetDiff.get(commitmentOffset, nullifierOffset, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClient.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/single_sapling_get_diff", "/", headers = headers, parameters = expectedParameters) }
                coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/single_sapling_get_diff", "/", headers = headers, parameters = expectedParameters) }
            }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - contracts - $contract_id - storage'`() {
        val contractId = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc")

        val (_, expectedResponse, _, jsonResponse) = contextContractsContractStorageGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).storage.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/storage", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/contracts/${contractId.base58}/storage", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call POST on '_ - $block_id - context - contracts - $contract_id - storage - normalized'`() {
        val contractId = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc")

        val (expectedRequest, expectedResponse, jsonRequest, jsonResponse) = contextContractsContractStorageNormalizedPostRequestConfiguration
        coEvery { httpClientProvider.post(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.contracts(contractId).storage.normalized.post(expectedRequest.unparsingMode, headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.post("$nodeUrl/$blockId/context/contracts/${contractId.base58}/storage/normalized", "/", headers = headers, request = expectedRequest) }
            coVerify { httpClientProvider.post("$nodeUrl/$blockId/context/contracts/${contractId.base58}/storage/normalized", "/", headers = headers, parameters = emptyList(), body = jsonRequest?.normalizeWith(json)) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - current_frozen_deposits'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateCurrentFrozenDepositsGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).currentFrozenDeposits.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/current_frozen_deposits", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/current_frozen_deposits", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - deactivated'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateDeactivatedGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).deactivated.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/deactivated", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/deactivated", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - delegated_balance'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateDelegatedBalanceGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).delegatedBalance.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/delegated_balance", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/delegated_balance", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - delegated_contracts'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateDelegatedContractsGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).delegatedContracts.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/delegated_contracts", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/delegated_contracts", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - frozen_deposits'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateFrozenDepositsGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).frozenDeposits.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/frozen_deposits", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/frozen_deposits", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - frozen_deposits_limit'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateFrozenDepositsLimitGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).frozenDepositsLimit.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/frozen_deposits_limit", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/frozen_deposits_limit", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - full_balance'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateFullBalanceGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).fullBalance.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/full_balance", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/full_balance", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - grace_period'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateGracePeriodGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).gracePeriod.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/grace_period", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/grace_period", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - participation'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateParticipationGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).participation.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/participation", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/participation", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - staking_balance'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateStakingBalanceGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).stakingBalance.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/staking_balance", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/staking_balance", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - delegates - $pkh - voting_power'`() {
        val pkh = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")

        val (_, expectedResponse, _, jsonResponse) = contextDelegatesDelegateVotingPowerGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.context.delegates(pkh).votingPower.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/voting_power", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/delegates/${pkh.base58}/voting_power", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - context - sapling - $sapling_state_id - get_diff'`() {
        data class Parameters(val commitmentOffset: ULong? = null, val nullifierOffset: ULong? = null)

        val saplingStateId = "saplingStateId"
        val parameters = listOf(
            Parameters(),
            Parameters(commitmentOffset = 1U),
            Parameters(nullifierOffset = 2U),
            Parameters(commitmentOffset = 1U, nullifierOffset = 2U),
        )

        val (_, expectedResponse, _, jsonResponse) = contextSaplingStateDiffGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            parameters.forEach {  parameters ->
                val (commitmentOffset, nullifierOffset) = parameters
                val expectedParameters = buildList<HttpParameter> {
                    commitmentOffset?.let { add("offset_commitment" to it.toString()) }
                    nullifierOffset?.let { add("offset_nullifier" to it.toString()) }
                }
                val response = runBlocking { blockClient.context.sapling(saplingStateId).getDiff.get(commitmentOffset, nullifierOffset, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClient.get("$nodeUrl/$blockId/context/sapling/$saplingStateId/get_diff", "/", headers = headers, parameters = expectedParameters) }
                coVerify { httpClientProvider.get("$nodeUrl/$blockId/context/sapling/$saplingStateId/get_diff", "/", headers = headers, parameters = expectedParameters) }
            }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - header'`() {
        val (_, expectedResponse, _, jsonResponse) = headerGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.header.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/header", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/header", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call POST on '_ - $block_id - helpers - preapply - operations'`() {
        val (expectedRequest, expectedResponse, jsonRequest, jsonResponse) = helpersPreapplyOperationsPostRequestConfiguration
        coEvery { httpClientProvider.post(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.helpers.preapply.operations.post(expectedRequest.operations, headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.post("$nodeUrl/$blockId/helpers/preapply/operations", "/", headers = headers, request = expectedRequest) }
            coVerify { httpClientProvider.post("$nodeUrl/$blockId/helpers/preapply/operations", "/", headers = headers, parameters = emptyList(), body = jsonRequest?.normalizeWith(json)) }
        }
    }

    @Test
    fun `should call POST on '_ - $block_id - helpers - scripts - run_operation'`() {
        val (expectedRequest, expectedResponse, jsonRequest, jsonResponse) = helpersScriptRunOperationPostRequestConfiguration
        coEvery { httpClientProvider.post(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.helpers.scripts.runOperation.post(expectedRequest.operation, headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.post("$nodeUrl/$blockId/helpers/scripts/run_operation", "/", headers = headers, request = expectedRequest) }
            coVerify { httpClientProvider.post("$nodeUrl/$blockId/helpers/scripts/run_operation", "/", headers = headers, parameters = emptyList(), body = jsonRequest) }
        }
    }

    @Test
    fun `should call GET on '_ - $block_id - operations'`() {
        val (_, expectedResponse, _, jsonResponse) = operationsGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { blockClient.operations.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/$blockId/operations", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/$blockId/operations", "/", headers = headers, parameters = emptyList()) }
        }
    }

    private val headers: List<List<HttpHeader>>
        get() = listOf(
            emptyList(),
            listOf("Authorization" to "Bearer")
        )

    private val getRequestConfiguration: RequestConfiguration<Unit, GetBlockResponse> =
        RequestConfiguration(
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

    private val contextBigMapsBigMapGetRequestConfiguration: RequestConfiguration<Unit, GetBigMapResponse> =
        RequestConfiguration(
            response = GetBigMapResponse(
                listOf(
                    MichelineLiteral.Integer(1000000),
                    MichelineLiteral.Integer(100000000),
                    MichelineLiteral.Integer(814),
                    MichelineLiteral.Integer(80000000),
                    MichelineLiteral.Integer(60000000),
                )
            ),
            jsonResponse = """
                [
                    {
                        "int": "1000000"
                    },
                    {
                        "int": "100000000"
                    },
                    {
                        "int": "814"
                    },
                    {
                        "int": "80000000"
                    },
                    {
                        "int": "60000000"
                    }
                ]
            """.trimIndent(),
        )

    private val contextBigMapsBigMapValueGetRequestConfiguration: RequestConfiguration<Unit, GetBigMapValueResponse> =
        RequestConfiguration(
            response = GetBigMapValueResponse(MichelineLiteral.Integer(1000000)),
            jsonResponse = """
                {
                    "int": "1000000"
                }
            """.trimIndent(),
        )

    private val contextConstantsGetRequestConfiguration: RequestConfiguration<Unit, GetConstantsResponse> =
        RequestConfiguration(
            response = GetConstantsResponse(
                RpcConstants.Active(
                    proofOfWorkNonceSize = 8U,
                    nonceLength = 32U,
                    maxAnonOpsPerBlock = 132U,
                    maxOperationDataLength = 32768,
                    maxProposalsPerDelegate = 20U,
                    maxMichelineNodeCount = 50000,
                    maxMichelineBytesLimit = 50000,
                    maxAllowedGlobalConstantsDepth = 10000,
                    cacheLayout = listOf(
                        100000000,
                        240000,
                        2560,
                    ),
                    michelsonMaximumTypeSize = 2001U,
                    preservedCycles = 5U,
                    blocksPerCycle = 8192,
                    blocksPerCommitment = 64,
                    blocksPerStakeSnapshot = 512,
                    blocksPerVotingPeriod = 40960,
                    hardGasLimitPerOperation = "1040000",
                    hardGasLimitPerBlock = "5200000",
                    proofOfWorkThreshold = 70368744177663,
                    tokensPerRoll = "6000000000",
                    seedNonceRevelationTip = "125000",
                    originationSize = 257,
                    bakingRewardFixedPortion = "10000000",
                    bakingRewardBonusPerSlot = "4286",
                    endorsingRewardPerSlot = "2857",
                    costPerByte = "250",
                    hardStorageLimitPerOperation = "60000",
                    quorumMin = 2000,
                    quorumMax = 7000,
                    minProposalQuorum = 500,
                    liquidityBakingSubsidy = "2500000",
                    liquidityBakingSunsetLevel = 3063809,
                    liquidityBakingEscapeEmaThreshold = 666667,
                    maxOperationsTimeToLive = 120,
                    minimalBlockDelay = 30,
                    delayIncrementPerRound = 15,
                    consensusCommitteeSize = 7000,
                    consensusThreshold = 4667,
                    minimalParticipationRatio = RpcRatio(numerator = 2U, denominator = 3U),
                    maxSlashingPeriod = 2,
                    frozenDepositsPercentage = 10,
                    doubleBakingPunishment = "640000000",
                    ratioOfFrozenDepositsSlashedPerDoubleEndorsement = RpcRatio(numerator = 1U, denominator = 2U),
                )
            ),
            jsonResponse = """
                {
                    "proof_of_work_nonce_size": 8,
                    "nonce_length": 32,
                    "max_anon_ops_per_block": 132,
                    "max_operation_data_length": 32768,
                    "max_proposals_per_delegate": 20,
                    "max_micheline_node_count": 50000,
                    "max_micheline_bytes_limit": 50000,
                    "max_allowed_global_constants_depth": 10000,
                    "cache_layout": [
                        "100000000",
                        "240000",
                        "2560"
                    ],
                    "michelson_maximum_type_size": 2001,
                    "preserved_cycles": 5,
                    "blocks_per_cycle": 8192,
                    "blocks_per_commitment": 64,
                    "blocks_per_stake_snapshot": 512,
                    "blocks_per_voting_period": 40960,
                    "hard_gas_limit_per_operation": "1040000",
                    "hard_gas_limit_per_block": "5200000",
                    "proof_of_work_threshold": "70368744177663",
                    "tokens_per_roll": "6000000000",
                    "seed_nonce_revelation_tip": "125000",
                    "origination_size": 257,
                    "baking_reward_fixed_portion": "10000000",
                    "baking_reward_bonus_per_slot": "4286",
                    "endorsing_reward_per_slot": "2857",
                    "cost_per_byte": "250",
                    "hard_storage_limit_per_operation": "60000",
                    "quorum_min": 2000,
                    "quorum_max": 7000,
                    "min_proposal_quorum": 500,
                    "liquidity_baking_subsidy": "2500000",
                    "liquidity_baking_sunset_level": 3063809,
                    "liquidity_baking_escape_ema_threshold": 666667,
                    "max_operations_time_to_live": 120,
                    "minimal_block_delay": "30",
                    "delay_increment_per_round": "15",
                    "consensus_committee_size": 7000,
                    "consensus_threshold": 4667,
                    "minimal_participation_ratio": {
                        "numerator": 2,
                        "denominator": 3
                    },
                    "max_slashing_period": 2,
                    "frozen_deposits_percentage": 10,
                    "double_baking_punishment": "640000000",
                    "ratio_of_frozen_deposits_slashed_per_double_endorsement": {
                        "numerator": 1,
                        "denominator": 2
                    }
                }
            """.trimIndent(),
        )

    private val contextContractsContractGetRequestConfiguration: RequestConfiguration<Unit, GetContractDetailsResponse> =
        RequestConfiguration(
            response = GetContractDetailsResponse(
                balance = "1500000",
                counter = "50143",
            ),
            jsonResponse = """
                {
                    "balance": "1500000",
                    "counter": "50143"
                }
            """.trimIndent(),
        )

    private val contextContractsContractBalanceGetRequestConfiguration: RequestConfiguration<Unit, GetContractBalanceResponse> =
        RequestConfiguration(
            response = GetContractBalanceResponse("1500000"),
            jsonResponse = """
                "1500000"
            """.trimIndent(),
        )

    private val contextContractsContractCounterGetRequestConfiguration: RequestConfiguration<Unit, GetContractCounterResponse> =
        RequestConfiguration(
            response = GetContractCounterResponse("50143"),
            jsonResponse = """
                "50143"
            """.trimIndent(),
        )

    private val contextContractsContractDelegateGetRequestConfiguration: RequestConfiguration<Unit, GetContractDelegateResponse> =
        RequestConfiguration(
            response = GetContractDelegateResponse(
                Ed25519PublicKeyHash("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb")
            ),
            jsonResponse = """
                "tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb"
            """.trimIndent(),
        )

    private val contextContractsContractEntrypointsGetRequestConfiguration: RequestConfiguration<Unit, GetContractEntrypointsResponse> =
        RequestConfiguration(
            response = GetContractEntrypointsResponse(
                entrypoints = mapOf(
                    "default" to MichelinePrimitiveApplication(prim = MichelsonComparableType.Unit)
                ),
            ),
            jsonResponse = """
                {
                    "entrypoints": {
                        "default": {
                            "prim": "unit"
                        }
                    }
                }
            """.trimIndent(),
        )

    private val contextContractsContractEntrypointsEntrypointGetRequestConfiguration: RequestConfiguration<Unit, GetContractEntrypointResponse> =
        RequestConfiguration(
            response = GetContractEntrypointResponse(
                MichelinePrimitiveApplication(prim = MichelsonComparableType.Unit)
            ),
            jsonResponse = """
                {
                    "prim": "unit"
                }
            """.trimIndent(),
        )

    private val contextContractsContractManagerKeyGetRequestConfiguration: RequestConfiguration<Unit, GetContractManagerKeyResponse> =
        RequestConfiguration(
            response = GetContractManagerKeyResponse(Ed25519PublicKey("edpktxQEW5NVGSd6kYvNyupsnuoYxKhcye8qKVEZuqMx1QnNG4tz9Y")),
            jsonResponse = """
                "edpktxQEW5NVGSd6kYvNyupsnuoYxKhcye8qKVEZuqMx1QnNG4tz9Y"
            """.trimIndent(),
        )

    private val contextContractsContractScriptGetRequestConfiguration: RequestConfiguration<Unit, GetContractScriptResponse> =
        RequestConfiguration(
            response = GetContractScriptResponse(
                RpcScript(
                    code = MichelineSequence(
                        MichelinePrimitiveApplication(
                            prim = MichelsonType.Parameter,
                            args = listOf(
                                MichelinePrimitiveApplication(prim = MichelsonComparableType.Nat),
                            )
                        ),
                        MichelinePrimitiveApplication(
                            prim = MichelsonType.Storage,
                            args = listOf(
                                MichelinePrimitiveApplication(prim = MichelsonComparableType.Nat),
                            )
                        ),
                        MichelinePrimitiveApplication(
                            prim = MichelsonType.Code,
                            args = listOf(
                                MichelineSequence(
                                    MichelinePrimitiveApplication(prim = MichelsonInstruction.Car),
                                    MichelinePrimitiveApplication(
                                        prim = MichelsonInstruction.Nil,
                                        args = listOf(
                                            MichelinePrimitiveApplication(prim = MichelsonType.Operation),
                                        )
                                    ),
                                    MichelinePrimitiveApplication(prim = MichelsonInstruction.Pair),
                                ),
                            )
                        )
                    ),
                    storage = MichelineLiteral.Integer("2000000000000000000000000000000000000000000000000000000")
                )
            ),
            jsonResponse = """
                {
                    "code": [
                        {
                            "prim": "parameter",
                            "args": [
                                {
                                    "prim": "nat"
                                }
                            ]
                        },
                        {
                            "prim": "storage",
                            "args": [
                                {
                                    "prim": "nat"
                                }
                            ]
                        },
                        {
                            "prim": "code",
                            "args": [
                                [
                                    {
                                        "prim": "CAR"
                                    },
                                    {
                                        "prim": "NIL",
                                        "args": [
                                            {
                                                "prim": "operation"
                                            }
                                        ]
                                    },
                                    {
                                        "prim": "PAIR"
                                    }
                                ]
                            ]
                        }
                    ],
                    "storage": {
                        "int": "2000000000000000000000000000000000000000000000000000000"
                    }
                }
            """.trimIndent(),
        )

    private val contextContractsContractScriptNormalizedPostRequestConfiguration: RequestConfiguration<GetContractNormalizedScriptRequest, GetContractNormalizedScriptResponse> =
        RequestConfiguration(
            request = GetContractNormalizedScriptRequest(unparsingMode = RpcScriptParsing.Readable),
            response = GetContractNormalizedScriptResponse(
                RpcScript(
                    code = MichelineSequence(
                        MichelinePrimitiveApplication(
                            prim = MichelsonType.Parameter,
                            args = listOf(
                                MichelinePrimitiveApplication(prim = MichelsonComparableType.Nat),
                            )
                        ),
                        MichelinePrimitiveApplication(
                            prim = MichelsonType.Storage,
                            args = listOf(
                                MichelinePrimitiveApplication(prim = MichelsonComparableType.Nat),
                            )
                        ),
                        MichelinePrimitiveApplication(
                            prim = MichelsonType.Code,
                            args = listOf(
                                MichelineSequence(
                                    MichelinePrimitiveApplication(prim = MichelsonInstruction.Car),
                                    MichelinePrimitiveApplication(
                                        prim = MichelsonInstruction.Nil,
                                        args = listOf(
                                            MichelinePrimitiveApplication(prim = MichelsonType.Operation),
                                        )
                                    ),
                                    MichelinePrimitiveApplication(prim = MichelsonInstruction.Pair),
                                ),
                            )
                        )
                    ),
                    storage = MichelineLiteral.Integer("2000000000000000000000000000000000000000000000000000000")
                )
            ),
            jsonRequest = """
                {
                    "unparsing_mode": "Readable"
                }
            """.trimIndent(),
            jsonResponse = """
                {
                    "code": [
                        {
                            "prim": "parameter",
                            "args": [
                                {
                                    "prim": "nat"
                                }
                            ]
                        },
                        {
                            "prim": "storage",
                            "args": [
                                {
                                    "prim": "nat"
                                }
                            ]
                        },
                        {
                            "prim": "code",
                            "args": [
                                [
                                    {
                                        "prim": "CAR"
                                    },
                                    {
                                        "prim": "NIL",
                                        "args": [
                                            {
                                                "prim": "operation"
                                            }
                                        ]
                                    },
                                    {
                                        "prim": "PAIR"
                                    }
                                ]
                            ]
                        }
                    ],
                    "storage": {
                        "int": "2000000000000000000000000000000000000000000000000000000"
                    }
                }
            """.trimIndent(),
        )

    private val contextContractsContractSaplingStateDiffGetRequestConfiguration: RequestConfiguration<Unit, GetContractSaplingStateDiffResponse> =
        RequestConfiguration(
            response = GetContractSaplingStateDiffResponse(
                RpcSaplingStateDiff(
                    root = HexString("9ea19ec68c2f5a0b8055a8d4790b889d1da8a6af4b3da4ce02cc6da76769f448"),
                    commitmentsAndCiphertexts = listOf(
                        Pair(
                            HexString("b17136cf8ff51a20a99967908de7ff7c409aad21aea03b8aac9d24115d69f34b"),
                            RpcSaplingCiphertext(
                                cv = HexString("0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87"),
                                epk = HexString("1f1a68d2d2fae1cbad7133ee79fadc9c5851560a84d243ae68763745e849ebd3"),
                                payloadEnc = HexString("bd527314d755b4144ebc50b00dd99133a838f95565df034f1fdb9e676c8dc52ddd1b7db4ee6f0fe479ab9cae7b729c73f76f77eccaa7b160462c077324780deb2dd9ec9505408fe3e7b5a042497faa"),
                                nonceEnc = HexString("86188510d1621bbb9c3541480120075b35860a9a568168f7"),
                                payloadOut = HexString("8e021af51cc7945146de0f090346c9e4bd07b925ee94844526554da55d7083f0341962cfefd50bdb0764e68fcebea474158cf15992112e3a3a6cb335599db09cc24d332cec37f251aaf2448c0834f8ae"),
                                nonceOut = HexString("da70e51067fb21a3c38f9114b14e8edd9e741f35389dfec8"),
                            ),
                        )
                    ),
                    nullifiers = listOf(
                        HexString("4438506437b4c028170378f5c4bb9cde198d19a558f1e004c9ac0977c100a5ac"),
                    ),
                )
            ),
            jsonResponse = """
                {
                    "root": "9ea19ec68c2f5a0b8055a8d4790b889d1da8a6af4b3da4ce02cc6da76769f448",
                    "commitments_and_ciphertexts": [
                        [
                            "b17136cf8ff51a20a99967908de7ff7c409aad21aea03b8aac9d24115d69f34b",
                            {
                                "cv": "0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87",
                                "epk": "1f1a68d2d2fae1cbad7133ee79fadc9c5851560a84d243ae68763745e849ebd3",
                                "payload_enc": "bd527314d755b4144ebc50b00dd99133a838f95565df034f1fdb9e676c8dc52ddd1b7db4ee6f0fe479ab9cae7b729c73f76f77eccaa7b160462c077324780deb2dd9ec9505408fe3e7b5a042497faa",
                                "nonce_enc": "86188510d1621bbb9c3541480120075b35860a9a568168f7",
                                "payload_out": "8e021af51cc7945146de0f090346c9e4bd07b925ee94844526554da55d7083f0341962cfefd50bdb0764e68fcebea474158cf15992112e3a3a6cb335599db09cc24d332cec37f251aaf2448c0834f8ae",
                                "nonce_out": "da70e51067fb21a3c38f9114b14e8edd9e741f35389dfec8"
                            }
                        ]
                    ],
                    "nullifiers": [
                        "4438506437b4c028170378f5c4bb9cde198d19a558f1e004c9ac0977c100a5ac"
                    ]
                }
            """.trimIndent(),
        )

    private val contextContractsContractStorageGetRequestConfiguration: RequestConfiguration<Unit, GetContractStorageResponse> =
        RequestConfiguration(
            response = GetContractStorageResponse(MichelineLiteral.Integer("2000000000000000000000000000000000000000000000000000000")),
            jsonResponse = """
                {
                    "int": "2000000000000000000000000000000000000000000000000000000"
                }
            """.trimIndent(),
        )

    private val contextContractsContractStorageNormalizedPostRequestConfiguration: RequestConfiguration<GetContractNormalizedStorageRequest, GetContractNormalizedStorageResponse> =
        RequestConfiguration(
            request = GetContractNormalizedStorageRequest(unparsingMode = RpcScriptParsing.Readable),
            response = GetContractNormalizedStorageResponse(MichelineLiteral.Integer("2000000000000000000000000000000000000000000000000000000")),
            jsonRequest = """
                {
                    "unparsing_mode": "Readable"
                }
            """.trimIndent(),
            jsonResponse = """
                {
                    "int": "2000000000000000000000000000000000000000000000000000000"
                }
            """.trimIndent(),
        )

    private val contextDelegatesDelegateGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateDetailsResponse> =
        RequestConfiguration(
            response = GetDelegateDetailsResponse(
                fullBalance = "1412416115211",
                currentFrozenDeposits = "141246510472",
                frozenDeposits = "141246510472",
                stakingBalance = "1413762727872",
                delegatedContracts = listOf(
                    ContractHash("KT1W6RyVDyGLwazwfJCphcYwb4g2Ltr82ebg"),
                    Ed25519PublicKeyHash("tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq"),
                    Ed25519PublicKeyHash("tz1KkJtLB9pMdLKNpVRNZw9zmysrxKmYcRGU"),
                ),
                delegatedBalance = "1346612661",
                deactivated = false,
                gracePeriod = 111,
                votingPower = 235,
            ),
            jsonResponse = """
                {
                    "full_balance": "1412416115211",
                    "current_frozen_deposits": "141246510472",
                    "frozen_deposits": "141246510472",
                    "staking_balance": "1413762727872",
                    "delegated_contracts": [
                        "KT1W6RyVDyGLwazwfJCphcYwb4g2Ltr82ebg",
                        "tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq",
                        "tz1KkJtLB9pMdLKNpVRNZw9zmysrxKmYcRGU"
                    ],
                    "delegated_balance": "1346612661",
                    "deactivated": false,
                    "grace_period": 111,
                    "voting_power": 235
                }
            """.trimIndent(),
        )

    private val contextDelegatesDelegateCurrentFrozenDepositsGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateCurrentFrozenDepositsResponse> =
        RequestConfiguration(
            response = GetDelegateCurrentFrozenDepositsResponse("141246510472"),
            jsonResponse = """
                "141246510472"
            """.trimIndent(),
        )

    private val contextDelegatesDelegateDeactivatedGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateDeactivatedStatusResponse> =
        RequestConfiguration(
            response = GetDelegateDeactivatedStatusResponse(false),
            jsonResponse = """
                false
            """.trimIndent(),
        )

    private val contextDelegatesDelegateDelegatedBalanceGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateDelegatedBalanceResponse> =
        RequestConfiguration(
            response = GetDelegateDelegatedBalanceResponse("1346612661"),
            jsonResponse = """
                "1346612661"
            """.trimIndent(),
        )

    private val contextDelegatesDelegateDelegatedContractsGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateDelegatedContractsResponse> =
        RequestConfiguration(
            response = GetDelegateDelegatedContractsResponse(
                listOf(
                    ContractHash("KT1W6RyVDyGLwazwfJCphcYwb4g2Ltr82ebg"),
                    Ed25519PublicKeyHash("tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq"),
                    Ed25519PublicKeyHash("tz1KkJtLB9pMdLKNpVRNZw9zmysrxKmYcRGU"),
                ),
            ),
            jsonResponse = """
                [
                    "KT1W6RyVDyGLwazwfJCphcYwb4g2Ltr82ebg",
                    "tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq",
                    "tz1KkJtLB9pMdLKNpVRNZw9zmysrxKmYcRGU"
                ]
            """.trimIndent(),
        )

    private val contextDelegatesDelegateFrozenDepositsGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateFrozenDepositsResponse> =
        RequestConfiguration(
            response = GetDelegateFrozenDepositsResponse("141246510472"),
            jsonResponse = """
                "141246510472"
            """.trimIndent(),
        )

    private val contextDelegatesDelegateFrozenDepositsLimitGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateFrozenDepositsLimitResponse> =
        RequestConfiguration(
            response = GetDelegateFrozenDepositsLimitResponse("141246510472"),
            jsonResponse = """
                "141246510472"
            """.trimIndent(),
        )

    private val contextDelegatesDelegateFullBalanceGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateFullBalanceResponse> =
        RequestConfiguration(
            response = GetDelegateFullBalanceResponse("1412416115211"),
            jsonResponse = """
                "1412416115211"
            """.trimIndent(),
        )

    private val contextDelegatesDelegateGracePeriodGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateGracePeriodResponse> =
        RequestConfiguration(
            response = GetDelegateGracePeriodResponse(111),
            jsonResponse = """
                111
            """.trimIndent(),
        )

    private val contextDelegatesDelegateParticipationGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateParticipationResponse> =
        RequestConfiguration(
            response = GetDelegateParticipationResponse(
                expectedCycleActivity = 1073741823,
                minimalCycleActivity = 1073741823,
                missedSlots = 1073741823,
                missedLevels = 1073741823,
                remainingAllowedMissedSlots = 1073741823,
                expectedEndorsingRewards = "1073741823",
            ),
            jsonResponse = """
                {
                    "expected_cycle_activity": 1073741823,
                    "minimal_cycle_activity": 1073741823,
                    "missed_slots": 1073741823,
                    "missed_levels": 1073741823,
                    "remaining_allowed_missed_slots": 1073741823,
                    "expected_endorsing_rewards": "1073741823"
                }
            """.trimIndent(),
        )

    private val contextDelegatesDelegateStakingBalanceGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateStakingBalanceResponse> =
        RequestConfiguration(
            response = GetDelegateStakingBalanceResponse("1413762727872"),
            jsonResponse = """
                "1413762727872"
            """.trimIndent(),
        )

    private val contextDelegatesDelegateVotingPowerGetRequestConfiguration: RequestConfiguration<Unit, GetDelegateVotingPowerResponse> =
        RequestConfiguration(
            response = GetDelegateVotingPowerResponse(235),
            jsonResponse = """
                235
            """.trimIndent(),
        )

    private val contextSaplingStateDiffGetRequestConfiguration: RequestConfiguration<Unit, GetSaplingStateDiffResponse> =
        RequestConfiguration(
            response = GetSaplingStateDiffResponse(
                RpcSaplingStateDiff(
                    root = HexString("9ea19ec68c2f5a0b8055a8d4790b889d1da8a6af4b3da4ce02cc6da76769f448"),
                    commitmentsAndCiphertexts = listOf(
                        Pair(
                            HexString("b17136cf8ff51a20a99967908de7ff7c409aad21aea03b8aac9d24115d69f34b"),
                            RpcSaplingCiphertext(
                                cv = HexString("0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87"),
                                epk = HexString("1f1a68d2d2fae1cbad7133ee79fadc9c5851560a84d243ae68763745e849ebd3"),
                                payloadEnc = HexString("bd527314d755b4144ebc50b00dd99133a838f95565df034f1fdb9e676c8dc52ddd1b7db4ee6f0fe479ab9cae7b729c73f76f77eccaa7b160462c077324780deb2dd9ec9505408fe3e7b5a042497faa"),
                                nonceEnc = HexString("86188510d1621bbb9c3541480120075b35860a9a568168f7"),
                                payloadOut = HexString("8e021af51cc7945146de0f090346c9e4bd07b925ee94844526554da55d7083f0341962cfefd50bdb0764e68fcebea474158cf15992112e3a3a6cb335599db09cc24d332cec37f251aaf2448c0834f8ae"),
                                nonceOut = HexString("da70e51067fb21a3c38f9114b14e8edd9e741f35389dfec8"),
                            ),
                        )
                    ),
                    nullifiers = listOf(
                        HexString("4438506437b4c028170378f5c4bb9cde198d19a558f1e004c9ac0977c100a5ac"),
                    ),
                )
            ),
            jsonResponse = """
                {
                    "root": "9ea19ec68c2f5a0b8055a8d4790b889d1da8a6af4b3da4ce02cc6da76769f448",
                    "commitments_and_ciphertexts": [
                        [
                            "b17136cf8ff51a20a99967908de7ff7c409aad21aea03b8aac9d24115d69f34b",
                            {
                                "cv": "0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87",
                                "epk": "1f1a68d2d2fae1cbad7133ee79fadc9c5851560a84d243ae68763745e849ebd3",
                                "payload_enc": "bd527314d755b4144ebc50b00dd99133a838f95565df034f1fdb9e676c8dc52ddd1b7db4ee6f0fe479ab9cae7b729c73f76f77eccaa7b160462c077324780deb2dd9ec9505408fe3e7b5a042497faa",
                                "nonce_enc": "86188510d1621bbb9c3541480120075b35860a9a568168f7",
                                "payload_out": "8e021af51cc7945146de0f090346c9e4bd07b925ee94844526554da55d7083f0341962cfefd50bdb0764e68fcebea474158cf15992112e3a3a6cb335599db09cc24d332cec37f251aaf2448c0834f8ae",
                                "nonce_out": "da70e51067fb21a3c38f9114b14e8edd9e741f35389dfec8"
                            }
                        ]
                    ],
                    "nullifiers": [
                        "4438506437b4c028170378f5c4bb9cde198d19a558f1e004c9ac0977c100a5ac"
                    ]
                }
            """.trimIndent(),
        )

    private val headerGetRequestConfiguration: RequestConfiguration<Unit, GetBlockHeaderResponse> =
        RequestConfiguration(
            response = GetBlockHeaderResponse(
                RpcFullBlockHeader(
                    protocol = ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                    chainId = ChainId("NetXnHfVqm9iesp"),
                    hash = BlockHash("BLkKavdWXZdfEXmup3FQKCrPJXTrVGfrbLhgBE3UU3SvyztZHHh"),
                    level = 440680,
                    proto = 2U,
                    predecessor = BlockHash("BM23hAmphaYyGvLCNJUYRohSYRMV3sb1dfSqCF5rNzmnrQ4sSdg"),
                    timestamp = Timestamp.Rfc3339("2022-04-25T12:07:50Z"),
                    validationPass = 4U,
                    operationsHash = OperationListListHash("LLoZqw9RLZro7WCumciaAbPHPvvL1TkTuyzqhyR9T3fdSUqZywxFX"),
                    fitness = listOf(
                        "02",
                        "0006b968",
                        "",
                        "fffffffe",
                        "00000000",
                    ),
                    context = ContextHash("CoVy5CXH95FPmcDHygVY7djgp4HFtCBDP16joWosVXLQBK1AjqfZ"),
                    payloadHash = BlockPayloadHash("vh2xdPFM2AAyd6qw8HpwrT3RopLofPDeJTUNmFqgrd7bSSkrbFsr"),
                    payloadRound = 0,
                    proofOfWorkNonce = "219a2eff641e0100",
                    liquidityBakingEscapeVote = false,
                    signature = GenericSignature("sigSAACcr7xsBJuTvxyAVbhJ91GHCxJV9g282UXXe5WbF4pqLwju7qgjtVF8N7PA8nAr3h6bMeqNtnpopVRhShwGLbKDRqMH"),
                )
            ),
            jsonResponse = """
                {
                    "protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                    "chain_id": "NetXnHfVqm9iesp",
                    "hash": "BLkKavdWXZdfEXmup3FQKCrPJXTrVGfrbLhgBE3UU3SvyztZHHh",
                    "level": 440680,
                    "proto": 2,
                    "predecessor": "BM23hAmphaYyGvLCNJUYRohSYRMV3sb1dfSqCF5rNzmnrQ4sSdg",
                    "timestamp": "2022-04-25T12:07:50Z",
                    "validation_pass": 4,
                    "operations_hash": "LLoZqw9RLZro7WCumciaAbPHPvvL1TkTuyzqhyR9T3fdSUqZywxFX",
                    "fitness": [
                        "02",
                        "0006b968",
                        "",
                        "fffffffe",
                        "00000000"
                    ],
                    "context": "CoVy5CXH95FPmcDHygVY7djgp4HFtCBDP16joWosVXLQBK1AjqfZ",
                    "payload_hash": "vh2xdPFM2AAyd6qw8HpwrT3RopLofPDeJTUNmFqgrd7bSSkrbFsr",
                    "payload_round": 0,
                    "proof_of_work_nonce": "219a2eff641e0100",
                    "liquidity_baking_escape_vote": false,
                    "signature": "sigSAACcr7xsBJuTvxyAVbhJ91GHCxJV9g282UXXe5WbF4pqLwju7qgjtVF8N7PA8nAr3h6bMeqNtnpopVRhShwGLbKDRqMH"
                }
            """.trimIndent(),
        )

    private val helpersPreapplyOperationsPostRequestConfiguration: RequestConfiguration<PreapplyOperationsRequest, PreapplyOperationsResponse>
        get() = RequestConfiguration(
            request = PreapplyOperationsRequest(
                listOf(
                    RpcApplicableOperation(
                        protocol = ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                        branch = BlockHash("BLVb91yM3Es88TFcrRNK36TTLnDnwbNoUVnKfUTc8KhJKJCL4SD"),
                        contents = listOf(
                            RpcOperationContent.Endorsement(
                                slot = 65535U,
                                level = 2147483647,
                                round = 2147483647,
                                blockPayloadHash = BlockPayloadHash("vh3cmyyrTLesEpcyUFgn2oqwE66dFq5TSFpvLPvhQudzEXUNe1CV"),
                            ),
                            RpcOperationContent.Preendorsement(
                                slot = 65535U,
                                level = 2147483647,
                                round = 2147483647,
                                blockPayloadHash = BlockPayloadHash("vh25yX2HXJ5nWggDDuBfrmUmGkWFK5KFVetErTAmR8mXpfd2vJ1i"),
                            ),
                            RpcOperationContent.SeedNonceRevelation(
                                level = 2147483647,
                                nonce = "IoSOM8gceb8UyOtuGAcYpo9U4ZIEPDxZ6jtSwCxoWWk6",
                            ),
                        ),
                        signature = GenericSignature("sigVBPQhHL3begDeF25s73Drqo381HvC6AVW1nAa5bZYGhBRjHduQtdLa11uFJoQRS8ccYGYZxPYFBdGzFxiRszzS9uV9qbk"),
                    ),
                ),
            ),
            response = PreapplyOperationsResponse(
                listOf(
                    RpcAppliedOperation(
                        contents = listOf(
                            RpcOperationContent.SeedNonceRevelation(
                                2147483647,
                                "DfzbbTlMeqZgXWt3dzJicvVhHrPMyWZe1rF1ME3nHYxDyFshMixHQIQkhhfRtY3dBOhhalR4ZWpLGYrk0LdI2wsiZjDPFJVv06i7twtUyzMmBZ2qnscW2BEuUyXsvBaHNERTm7Pg",
                                metadata = RpcOperationMetadata.SeedNonceRevelation(
                                    balanceUpdates = listOf(
                                        RpcBalanceUpdate.Contract(
                                            contract = Ed25519PublicKeyHash("tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP"),
                                            change = 2147483647,
                                            origin = RpcBalanceUpdate.Origin.Block,
                                        ),
                                    ),
                                ),
                            ),
                            RpcOperationContent.Endorsement(
                                slot = 65535U,
                                level = 2147483647,
                                round = 2147483647,
                                blockPayloadHash = BlockPayloadHash("vh3cmyyrTLesEpcyUFgn2oqwE66dFq5TSFpvLPvhQudzEXUNe1CV"),
                                metadata = RpcOperationMetadata.Endorsement(
                                    balanceUpdates = listOf(
                                        RpcBalanceUpdate.LegacyRewards(
                                            delegate = Ed25519PublicKeyHash("tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP"),
                                            cycle = 2147483647,
                                            change = 2147483647,
                                            origin = RpcBalanceUpdate.Origin.Block,
                                        ),
                                    ),
                                    delegate = Ed25519PublicKeyHash("tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP"),
                                    endorsementPower = 1073741823,
                                )
                            ),
                            RpcOperationContent.Preendorsement(
                                slot = 65535U,
                                level = 2147483647,
                                round = 2147483647,
                                blockPayloadHash = BlockPayloadHash("vh25yX2HXJ5nWggDDuBfrmUmGkWFK5KFVetErTAmR8mXpfd2vJ1i"),
                                metadata = RpcOperationMetadata.Preendorsement(
                                    balanceUpdates = listOf(
                                        RpcBalanceUpdate.BlockFees(
                                            change = 2147483647,
                                            origin = RpcBalanceUpdate.Origin.Block,
                                        ),
                                    ),
                                    delegate = Ed25519PublicKeyHash("tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP"),
                                    preendorsementPower = 1073741823,
                                )
                            ),
                        ),
                        signature = GenericSignature("sigVBPQhHL3begDeF25s73Drqo381HvC6AVW1nAa5bZYGhBRjHduQtdLa11uFJoQRS8ccYGYZxPYFBdGzFxiRszzS9uV9qbk"),
                    )
                )
            ),
            jsonRequest = """
                [
                    {
                        "protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                        "branch": "BLVb91yM3Es88TFcrRNK36TTLnDnwbNoUVnKfUTc8KhJKJCL4SD",
                        "contents": [
                            {
                                "kind": "endorsement",
                                "slot": 65535,
                                "level": 2147483647,
                                "round": 2147483647,
                                "block_payload_hash": "vh3cmyyrTLesEpcyUFgn2oqwE66dFq5TSFpvLPvhQudzEXUNe1CV"
                            },
                            {
                                "kind": "preendorsement",
                                "slot": 65535,
                                "level": 2147483647,
                                "round": 2147483647,
                                "block_payload_hash": "vh25yX2HXJ5nWggDDuBfrmUmGkWFK5KFVetErTAmR8mXpfd2vJ1i"
                            },
                            {
                                "kind": "seed_nonce_revelation",
                                "level": 2147483647,
                                "nonce": "IoSOM8gceb8UyOtuGAcYpo9U4ZIEPDxZ6jtSwCxoWWk6"
                            }
                        ],
                        "signature": "sigVBPQhHL3begDeF25s73Drqo381HvC6AVW1nAa5bZYGhBRjHduQtdLa11uFJoQRS8ccYGYZxPYFBdGzFxiRszzS9uV9qbk"
                    }
                ]
            """.trimIndent(),
            jsonResponse = """
                [
                    {
                        "contents": [
                            {
                                "kind": "seed_nonce_revelation",
                                "level": 2147483647,
                                "nonce": "DfzbbTlMeqZgXWt3dzJicvVhHrPMyWZe1rF1ME3nHYxDyFshMixHQIQkhhfRtY3dBOhhalR4ZWpLGYrk0LdI2wsiZjDPFJVv06i7twtUyzMmBZ2qnscW2BEuUyXsvBaHNERTm7Pg",
                                "metadata": {
                                    "balance_updates": [
                                        {
                                            "kind": "contract",
                                            "contract": "tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP",
                                            "change": "2147483647",
                                            "origin": "block"
                                        }
                                    ]
                                }
                            },
                            {
                                "kind": "endorsement",
                                "slot": 65535,
                                "level": 2147483647,
                                "round": 2147483647,
                                "block_payload_hash": "vh3cmyyrTLesEpcyUFgn2oqwE66dFq5TSFpvLPvhQudzEXUNe1CV",
                                "metadata": {
                                    "balance_updates": [
                                        {
                                              "kind": "freezer",
                                              "category": "legacy_rewards",
                                              "delegate": "tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP",
                                              "cycle": 2147483647,
                                              "change": "2147483647",
                                              "origin": "block"
                                        }
                                    ],
                                    "delegate": "tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP",
                                    "endorsement_power": 1073741823
                                }
                            },
                            {
                                "kind": "preendorsement",
                                "slot": 65535,
                                "level": 2147483647,
                                "round": 2147483647,
                                "block_payload_hash": "vh25yX2HXJ5nWggDDuBfrmUmGkWFK5KFVetErTAmR8mXpfd2vJ1i",
                                "metadata": {
                                    "balance_updates": [
                                        {
                                            "kind": "accumulator",
                                            "category": "block fees",
                                            "change": "2147483647",
                                            "origin": "block"
                                        }
                                    ],
                                    "delegate": "tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP",
                                    "preendorsement_power": 1073741823
                                }
                            }
                        ],
                        "signature": "sigVBPQhHL3begDeF25s73Drqo381HvC6AVW1nAa5bZYGhBRjHduQtdLa11uFJoQRS8ccYGYZxPYFBdGzFxiRszzS9uV9qbk"
                    }
                ]
            """.trimIndent(),
        )

    private val helpersScriptRunOperationPostRequestConfiguration: RequestConfiguration<RunOperationRequest, RunOperationResponse>
        get() = RequestConfiguration(
            request = RunOperationRequest(
                RpcRunnableOperation(
                    branch = BlockHash("BLVb91yM3Es88TFcrRNK36TTLnDnwbNoUVnKfUTc8KhJKJCL4SD"),
                    contents = listOf(
                        RpcOperationContent.Endorsement(
                            slot = 65535U,
                            level = 2147483647,
                            round = 2147483647,
                            blockPayloadHash = BlockPayloadHash("vh3cmyyrTLesEpcyUFgn2oqwE66dFq5TSFpvLPvhQudzEXUNe1CV"),
                        ),
                        RpcOperationContent.Preendorsement(
                            slot = 65535U,
                            level = 2147483647,
                            round = 2147483647,
                            blockPayloadHash = BlockPayloadHash("vh25yX2HXJ5nWggDDuBfrmUmGkWFK5KFVetErTAmR8mXpfd2vJ1i"),
                        ),
                        RpcOperationContent.SeedNonceRevelation(
                            level = 2147483647,
                            nonce = "IoSOM8gceb8UyOtuGAcYpo9U4ZIEPDxZ6jtSwCxoWWk6",
                        ),
                    ),
                    signature = GenericSignature("sigVBPQhHL3begDeF25s73Drqo381HvC6AVW1nAa5bZYGhBRjHduQtdLa11uFJoQRS8ccYGYZxPYFBdGzFxiRszzS9uV9qbk"),
                    chainId = ChainId("NetXnHfVqm9iesp"),
                ),
            ),
            response = RunOperationResponse(
                listOf(
                    RpcOperationContent.SeedNonceRevelation(
                        2147483647,
                        "DfzbbTlMeqZgXWt3dzJicvVhHrPMyWZe1rF1ME3nHYxDyFshMixHQIQkhhfRtY3dBOhhalR4ZWpLGYrk0LdI2wsiZjDPFJVv06i7twtUyzMmBZ2qnscW2BEuUyXsvBaHNERTm7Pg",
                        metadata = RpcOperationMetadata.SeedNonceRevelation(
                            balanceUpdates = listOf(
                                RpcBalanceUpdate.Contract(
                                    contract = Ed25519PublicKeyHash("tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP"),
                                    change = 2147483647,
                                    origin = RpcBalanceUpdate.Origin.Block,
                                ),
                            ),
                        ),
                    ),
                    RpcOperationContent.Endorsement(
                        slot = 65535U,
                        level = 2147483647,
                        round = 2147483647,
                        blockPayloadHash = BlockPayloadHash("vh3cmyyrTLesEpcyUFgn2oqwE66dFq5TSFpvLPvhQudzEXUNe1CV"),
                        metadata = RpcOperationMetadata.Endorsement(
                            balanceUpdates = listOf(
                                RpcBalanceUpdate.LegacyRewards(
                                    delegate = Ed25519PublicKeyHash("tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP"),
                                    cycle = 2147483647,
                                    change = 2147483647,
                                    origin = RpcBalanceUpdate.Origin.Block,
                                ),
                            ),
                            delegate = Ed25519PublicKeyHash("tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP"),
                            endorsementPower = 1073741823,
                        )
                    ),
                    RpcOperationContent.Preendorsement(
                        slot = 65535U,
                        level = 2147483647,
                        round = 2147483647,
                        blockPayloadHash = BlockPayloadHash("vh25yX2HXJ5nWggDDuBfrmUmGkWFK5KFVetErTAmR8mXpfd2vJ1i"),
                        metadata = RpcOperationMetadata.Preendorsement(
                            balanceUpdates = listOf(
                                RpcBalanceUpdate.BlockFees(
                                    change = 2147483647,
                                    origin = RpcBalanceUpdate.Origin.Block,
                                ),
                            ),
                            delegate = Ed25519PublicKeyHash("tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP"),
                            preendorsementPower = 1073741823,
                        )
                    )
                )
            ),
            jsonRequest = """
                {
                    "operation": {
                        "branch": "BLVb91yM3Es88TFcrRNK36TTLnDnwbNoUVnKfUTc8KhJKJCL4SD",
                        "contents": [
                            {
                                "kind": "endorsement",
                                "slot": 65535,
                                "level": 2147483647,
                                "round": 2147483647,
                                "block_payload_hash": "vh3cmyyrTLesEpcyUFgn2oqwE66dFq5TSFpvLPvhQudzEXUNe1CV"
                            },
                            {
                                "kind": "preendorsement",
                                "slot": 65535,
                                "level": 2147483647,
                                "round": 2147483647,
                                "block_payload_hash": "vh25yX2HXJ5nWggDDuBfrmUmGkWFK5KFVetErTAmR8mXpfd2vJ1i"
                            },
                            {
                                "kind": "seed_nonce_revelation",
                                "level": 2147483647,
                                "nonce": "IoSOM8gceb8UyOtuGAcYpo9U4ZIEPDxZ6jtSwCxoWWk6"
                            }
                        ],
                        "signature": "sigVBPQhHL3begDeF25s73Drqo381HvC6AVW1nAa5bZYGhBRjHduQtdLa11uFJoQRS8ccYGYZxPYFBdGzFxiRszzS9uV9qbk"
                    },
                    "chain_id": "NetXnHfVqm9iesp"
                }
            """.trimIndent(),
            jsonResponse = """
                [
                    {
                        "kind": "seed_nonce_revelation",
                        "level": 2147483647,
                        "nonce": "DfzbbTlMeqZgXWt3dzJicvVhHrPMyWZe1rF1ME3nHYxDyFshMixHQIQkhhfRtY3dBOhhalR4ZWpLGYrk0LdI2wsiZjDPFJVv06i7twtUyzMmBZ2qnscW2BEuUyXsvBaHNERTm7Pg",
                        "metadata": {
                            "balance_updates": [
                                {
                                    "kind": "contract",
                                    "contract": "tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP",
                                    "change": "2147483647",
                                    "origin": "block"
                                }
                            ]
                        }
                    },
                    {
                        "kind": "endorsement",
                        "slot": 65535,
                        "level": 2147483647,
                        "round": 2147483647,
                        "block_payload_hash": "vh3cmyyrTLesEpcyUFgn2oqwE66dFq5TSFpvLPvhQudzEXUNe1CV",
                        "metadata": {
                            "balance_updates": [
                                {
                                      "kind": "freezer",
                                      "category": "legacy_rewards",
                                      "delegate": "tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP",
                                      "cycle": 2147483647,
                                      "change": "2147483647",
                                      "origin": "block"
                                }
                            ],
                            "delegate": "tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP",
                            "endorsement_power": 1073741823
                        }
                    },
                    {
                        "kind": "preendorsement",
                        "slot": 65535,
                        "level": 2147483647,
                        "round": 2147483647,
                        "block_payload_hash": "vh25yX2HXJ5nWggDDuBfrmUmGkWFK5KFVetErTAmR8mXpfd2vJ1i",
                        "metadata": {
                            "balance_updates": [
                                {
                                    "kind": "accumulator",
                                    "category": "block fees",
                                    "change": "2147483647",
                                    "origin": "block"
                                }
                            ],
                            "delegate": "tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP",
                            "preendorsement_power": 1073741823
                       }
                    }
                ]
            """.trimIndent(),
        )

    private val operationsGetRequestConfiguration: RequestConfiguration<Unit, GetBlockOperationsResponse> =
        RequestConfiguration(
            response = GetBlockOperationsResponse(
                listOf(
                    listOf(
                        RpcOperation(
                            protocol = ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                            chainId = ChainId("NetXnHfVqm9iesp"),
                            hash = OperationHash("ooAvuePdKpG5bid766yD5C36p4kaKERkNEGcXY68AAqFZrKcxXm"),
                            branch = BlockHash("BLVb91yM3Es88TFcrRNK36TTLnDnwbNoUVnKfUTc8KhJKJCL4SD"),
                            contents = listOf(
                                RpcOperationContent.Preendorsement(
                                    slot = 0U,
                                    level = 440708,
                                    round = 0,
                                    blockPayloadHash = BlockPayloadHash("vh25yX2HXJ5nWggDDuBfrmUmGkWFK5KFVetErTAmR8mXpfd2vJ1i"),
                                    metadata = RpcOperationMetadata.Preendorsement(
                                        balanceUpdates = emptyList(),
                                        delegate = Ed25519PublicKeyHash("tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP"),
                                        preendorsementPower = 1469,
                                    ),
                                ),
                            ),
                            signature = GenericSignature("sigVBPQhHL3begDeF25s73Drqo381HvC6AVW1nAa5bZYGhBRjHduQtdLa11uFJoQRS8ccYGYZxPYFBdGzFxiRszzS9uV9qbk"),
                        ),
                        RpcOperation(
                            protocol = ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                            chainId = ChainId("NetXnHfVqm9iesp"),
                            hash = OperationHash("ooq7oaQ9TKsd8oSaYfHyzNqL7MDvA1RiKZQ7Vg3x925846hWGcy"),
                            branch = BlockHash("BLZkVfKEhcH3b59DciYukrY8ghHVsSdsJiMyxHNfreB8xRtPqby"),
                            contents = listOf(
                                RpcOperationContent.Endorsement(
                                    slot = 0U,
                                    level = 440707,
                                    round = 0,
                                    blockPayloadHash = BlockPayloadHash("vh3cmyyrTLesEpcyUFgn2oqwE66dFq5TSFpvLPvhQudzEXUNe1CV"),
                                    metadata = RpcOperationMetadata.Endorsement(
                                        balanceUpdates = emptyList(),
                                        delegate = Ed25519PublicKeyHash("tz1aWXP237BLwNHJcCD4b3DutCevhqq2T1Z9"),
                                        endorsementPower = 1151,
                                    ),
                                ),
                            ),
                            signature = GenericSignature("signFYqYeo2K8WznMQ5oUdWw1HAoFdwmin9WkiUsKdLrwZEPuZqDDvDND1X16iGqwJmeV6StWUv4RVybdFszNCqoy6XVpLKz"),
                        ),
                    ),
                    listOf(),
                )
            ),
            jsonResponse = """
                [
                    [
                        {
                            "protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                            "chain_id": "NetXnHfVqm9iesp",
                            "hash": "ooAvuePdKpG5bid766yD5C36p4kaKERkNEGcXY68AAqFZrKcxXm",
                            "branch": "BLVb91yM3Es88TFcrRNK36TTLnDnwbNoUVnKfUTc8KhJKJCL4SD",
                            "contents": [
                                {
                                    "kind": "preendorsement",
                                    "slot": 0,
                                    "level": 440708,
                                    "round": 0,
                                    "block_payload_hash": "vh25yX2HXJ5nWggDDuBfrmUmGkWFK5KFVetErTAmR8mXpfd2vJ1i",
                                    "metadata": {
                                        "balance_updates": [],
                                        "delegate": "tz1RuHDSj9P7mNNhfKxsyLGRDahTX5QD1DdP",
                                        "preendorsement_power": 1469
                                    }
                                }
                            ],
                            "signature": "sigVBPQhHL3begDeF25s73Drqo381HvC6AVW1nAa5bZYGhBRjHduQtdLa11uFJoQRS8ccYGYZxPYFBdGzFxiRszzS9uV9qbk"
                        },
                        {
                            "protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                            "chain_id": "NetXnHfVqm9iesp",
                            "hash": "ooq7oaQ9TKsd8oSaYfHyzNqL7MDvA1RiKZQ7Vg3x925846hWGcy",
                            "branch": "BLZkVfKEhcH3b59DciYukrY8ghHVsSdsJiMyxHNfreB8xRtPqby",
                            "contents": [
                                {
                                    "kind": "endorsement",
                                    "slot": 0,
                                    "level": 440707,
                                    "round": 0,
                                    "block_payload_hash": "vh3cmyyrTLesEpcyUFgn2oqwE66dFq5TSFpvLPvhQudzEXUNe1CV",
                                    "metadata": {
                                        "balance_updates": [],
                                        "delegate": "tz1aWXP237BLwNHJcCD4b3DutCevhqq2T1Z9",
                                        "endorsement_power": 1151
                                    }
                                }
                            ],
                            "signature": "signFYqYeo2K8WznMQ5oUdWw1HAoFdwmin9WkiUsKdLrwZEPuZqDDvDND1X16iGqwJmeV6StWUv4RVybdFszNCqoy6XVpLKz"
                        }
                    ],
                    []
                ]
            """.trimIndent(),
        )

    private data class RequestConfiguration<Req, Res>(val request: Req, val response: Res, val jsonRequest: String?, val jsonResponse: String)
    private fun <Res> RequestConfiguration(response: Res, jsonResponse: String): RequestConfiguration<Unit, Res> = RequestConfiguration(Unit, response, null, jsonResponse)
}