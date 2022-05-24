package it.airgap.tezos.rpc.type.error

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

class ErrorTest {

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
    fun `should serialize RpcError`() {
        objectsWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcError`() {
        objectsWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString<RpcError>(string)
            assertEquals(expected, actual)
        }
    }

    private val objectsWithJson: List<Pair<RpcError, String>>
        get() = listOf(
            RpcError(
                RpcError.Kind.Branch,
                "id",
            ) to """
                {
                    "kind": "branch",
                    "id": "id"
                }
            """.trimIndent(),
            RpcError(
                RpcError.Kind.Temporary,
                "id",
            ) to """
                {
                    "kind": "temporary",
                    "id": "id"
                }
            """.trimIndent(),
            RpcError(
                RpcError.Kind.Permanent,
                "id",
            ) to """
                {
                    "kind": "permanent",
                    "id": "id"
                }
            """.trimIndent(),
            RpcError(
                RpcError.Kind.Unknown("other"),
                "id",
            ) to """
                {
                    "kind": "other",
                    "id": "id"
                }
            """.trimIndent(),
            RpcError(
                RpcError.Kind.Branch,
                "proto.012-Psithaca.implicit.empty_implicit_contract",
                mapOf(
                    "implicit" to "tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c",
                ),
            ) to """
                {
                    "kind": "branch",
                    "id": "proto.012-Psithaca.implicit.empty_implicit_contract",
                    "implicit": "tz1PwXjsrgYBi9wpe3tFhazJpt7JMTVzBp5c"
                }
            """.trimIndent(),
        )
}