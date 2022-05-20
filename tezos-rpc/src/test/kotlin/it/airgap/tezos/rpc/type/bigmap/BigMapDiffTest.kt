package it.airgap.tezos.rpc.type.bigmap

import io.mockk.unmockkAll
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
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

class BigMapDiffTest {

    private lateinit var json: Json

    @Before
    fun setup() {
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
    fun `should serialize RpcBigMapDiff`() {
        objectsWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcBigMapDiff`() {
        objectsWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString<RpcBigMapDiff>(string)
            assertEquals(expected, actual)
        }
    }

    private val objectsWithJson: List<Pair<RpcBigMapDiff, String>>
        get() = listOf(
            RpcBigMapDiff.Update(
                bigMap = "123",
                keyHash = ScriptExprHash("exprtmQ8hNMPRj9ktdKP55oRiBcnGYKDGoYkF8vJ9g1xLt7L7JydSJ"),
                key = MichelineLiteral.String("key"),
            ) to """
                {
                    "action": "update",
                    "big_map": "123",
                    "key_hash": "exprtmQ8hNMPRj9ktdKP55oRiBcnGYKDGoYkF8vJ9g1xLt7L7JydSJ",
                    "key": {
                        "string": "key"
                    }
                }
            """.trimIndent(),
            RpcBigMapDiff.Update(
                bigMap = "123",
                keyHash = ScriptExprHash("exprtmQ8hNMPRj9ktdKP55oRiBcnGYKDGoYkF8vJ9g1xLt7L7JydSJ"),
                key = MichelineLiteral.String("key"),
                value = MichelineLiteral.String("value"),
            ) to """
                {
                    "action": "update",
                    "big_map": "123",
                    "key_hash": "exprtmQ8hNMPRj9ktdKP55oRiBcnGYKDGoYkF8vJ9g1xLt7L7JydSJ",
                    "key": {
                        "string": "key"
                    },
                    "value": {
                        "string": "value"
                    }
                }
            """.trimIndent(),
            RpcBigMapDiff.Remove(
                bigMap = "123",
            ) to """
                {
                    "action": "remove",
                    "big_map": "123"
                }
            """.trimIndent(),
            RpcBigMapDiff.Copy(
                sourceBigMap = "123",
                destinationBigMap = "321"
            ) to """
                {
                    "action": "copy",
                    "source_big_map": "123",
                    "destination_big_map": "321"
                }
            """.trimIndent(),
            RpcBigMapDiff.Alloc(
                bigMap = "123",
                keyType = MichelinePrimitiveApplication(prim = MichelsonComparableType.String),
                valueType = MichelinePrimitiveApplication(prim = MichelsonComparableType.String),
            ) to """
                {
                    "action": "alloc",
                    "big_map": "123",
                    "key_type": {
                        "prim": "string"
                    },
                    "value_type": {
                        "prim": "string"
                    }
                }
            """.trimIndent(),
        )
}