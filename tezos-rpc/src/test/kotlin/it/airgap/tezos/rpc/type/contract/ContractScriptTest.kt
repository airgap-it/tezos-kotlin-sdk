package it.airgap.tezos.rpc.type.contract

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

class ContractScriptTest {

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
    fun `should serialize RpcScriptParsing`() {
        objectsWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcScriptParsing`() {
        objectsWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString<RpcScriptParsing>(string)
            assertEquals(expected, actual)
        }
    }

    private val objectsWithJson: List<Pair<RpcScriptParsing, String>>
        get() = listOf(
            RpcScriptParsing.Readable to "\"Readable\"",
            RpcScriptParsing.Optimized to "\"Optimized\"",
            RpcScriptParsing.OptimizedLegacy to "\"Optimized_legacy\"",
        )
}