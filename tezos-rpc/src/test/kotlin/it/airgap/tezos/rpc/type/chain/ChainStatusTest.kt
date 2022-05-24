package it.airgap.tezos.rpc.type.chain

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.internal.rpcModule
import it.airgap.tezos.rpc.internal.utils.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mockTezos
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ChainStatusTest {

    private lateinit var json: Json

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val tezos = mockTezos()
        json = Json(from = tezos.rpcModule.dependencyRegistry.json) {
            prettyPrint = true
        }
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should serialize RpcChainStatus`() {
        chainStatusWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcChainStatus`() {
        chainStatusWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString<RpcChainStatus>(string)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should serialize RpcTestChainStatus`() {
        testChainStatusWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcTestChainStatus`() {
        testChainStatusWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString<RpcTestChainStatus>(string)
            assertEquals(expected, actual)
        }
    }

    private val chainStatusWithJson: List<Pair<RpcChainStatus, String>>
        get() = listOf(
            RpcChainStatus.Stuck to "\"stuck\"",
            RpcChainStatus.Synced to "\"synced\"",
            RpcChainStatus.Unsynced to "\"unsynced\"",
        )

    private val testChainStatusWithJson: List<Pair<RpcTestChainStatus, String>>
        get() = listOf(
            RpcTestChainStatus.NotRunning to """
                {
                    "status": "not_running"
                }
            """.trimIndent(),
            RpcTestChainStatus.Forking(
                ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                Timestamp.Rfc3339("2022-04-26T07:07:25Z"),
            ) to """
                {
                    "status": "forking",
                    "protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                    "expiration": "2022-04-26T07:07:25Z"
                }
            """.trimIndent(),
            RpcTestChainStatus.Running(
                ChainId("NetXdQprcVkpaWU"),
                BlockHash("BLznWUsgQuUMKXKgAcxUFZwq9Y9KpZevDVruQ8V1So3jjHF68WG"),
                ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                Timestamp.Rfc3339("2022-04-26T07:07:25Z"),
            ) to """
                {
                    "status": "running",
                    "chain_id": "NetXdQprcVkpaWU",
                    "genesis": "BLznWUsgQuUMKXKgAcxUFZwq9Y9KpZevDVruQ8V1So3jjHF68WG",
                    "protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                    "expiration": "2022-04-26T07:07:25Z"
                }
            """.trimIndent(),
        )
}