package it.airgap.tezos.rpc.type.history

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
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

class HistoryModeTest {

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
    fun `should serialize RpcHistoryMode`() {
        objectsWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcHistoryMode`() {
        objectsWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString<RpcHistoryMode>(string)
            assertEquals(expected, actual)
        }
    }

    private val objectsWithJson: List<Pair<RpcHistoryMode, String>>
        get() = listOf(
            RpcHistoryMode.Archive to "\"archive\"",
            RpcHistoryMode.Full() to "\"full\"",
            RpcHistoryMode.Full(
                RpcAdditionalCycles(1U),
            ) to """
                {
                    "full": {
                        "additional_cycles": 1
                    }
                }
            """.trimIndent(),
            RpcHistoryMode.Rolling() to "\"rolling\"",
            RpcHistoryMode.Rolling(
                RpcAdditionalCycles(1U),
            ) to """
                {
                    "rolling": {
                        "additional_cycles": 1
                    }
                }
            """.trimIndent(),
        )
}