package it.airgap.tezos.rpc.type.delegate

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.type.encoded.Ed25519PublicKey
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

class DelegateSelectionTest {

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
    fun `should serialize RpcDelegateSelection`() {
        objectsWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcDelegateSelection`() {
        objectsWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString<RpcDelegateSelection>(string)
            assertEquals(expected, actual)
        }
    }

    private val objectsWithJson: List<Pair<RpcDelegateSelection, String>>
        get() = listOf(
            RpcDelegateSelection.Random to "\"random\"",
            RpcDelegateSelection.RoundRobinOver(
                listOf(
                    listOf(Ed25519PublicKey("edpkuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4"))
                )
            ) to """
                [
                    ["edpkuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4"]
                ]
            """.trimIndent()
        )
}