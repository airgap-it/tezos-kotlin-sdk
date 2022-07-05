package it.airgap.tezos.michelson.internal.coder

import filterValuesNotNull
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.michelson.coder.fromJsonString
import it.airgap.tezos.michelson.coder.toJsonString
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelineJsonCoderTest {

    private lateinit var tezos: Tezos
    private lateinit var michelineJsonCoder: MichelineJsonCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        michelineJsonCoder = MichelineJsonCoder()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode Micheline to JSON`() = withTezosContext {
        listOf(
            literalIntegerWithJson(),
            literalStringWithJson(),
            literalBytesWithJson(),
            literalBytesWithJson("0x00"),
            primitiveApplicationWithJson(),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0))),
            primitiveApplicationWithJson(annots = listOf("%annot")),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0)), annots = listOf("%annot")),
            sequenceWithJson(),
            sequenceWithJson(listOf(MichelineLiteral.Integer(0))),
            sequenceWithJson(
                listOf(
                    MichelinePrimitiveApplication(
                        "prim",
                        listOf(MichelineLiteral.Integer(0)),
                        listOf("%annot")
                    )
                )
            ),
        ).forEach {
            val jsonElement = Json.decodeFromString<JsonElement>(it.second)
            assertEquals(Json.encodeToString(jsonElement), Json.encodeToString(it.first))
            assertEquals(jsonElement, michelineJsonCoder.encode(it.first))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(tezos))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(michelineJsonCoder))
        }
    }

    @Test
    fun `should encode Micheline Literal to JSON`() = withTezosContext {
        listOf(
            literalIntegerWithJson(),
            literalStringWithJson(),
            literalBytesWithJson(),
            literalBytesWithJson("0x00"),
        ).forEach {
            val jsonElement = Json.decodeFromString<JsonElement>(it.second)
            assertEquals(Json.encodeToString(jsonElement), Json.encodeToString(it.first))
            assertEquals(jsonElement, michelineJsonCoder.encode(it.first))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(tezos))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(michelineJsonCoder))
        }

        listOf(
            literalBytesWithJson(),
            literalBytesWithJson("0x00"),
        ).forEach {
            val jsonElement = Json.decodeFromString<JsonElement>(it.second)
            assertEquals(Json.encodeToString(jsonElement), Json.encodeToString(it.first))
            assertEquals(jsonElement, michelineJsonCoder.encode(it.first))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(tezos))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(michelineJsonCoder))
        }
    }

    @Test
    fun `should encode Micheline Primitive Application to JSON`() = withTezosContext {
        listOf(
            primitiveApplicationWithJson(),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0))),
            primitiveApplicationWithJson(annots = listOf("%annot")),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0)), annots = listOf("%annot")),
        ).forEach {
            val jsonElement = Json.decodeFromString<JsonElement>(it.second)
            assertEquals(Json.encodeToString(jsonElement), Json.encodeToString(it.first))
            assertEquals(jsonElement, michelineJsonCoder.encode(it.first))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(tezos))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(michelineJsonCoder))
        }
    }

    @Test
    fun `should encode Micheline Sequence to JSON`() = withTezosContext {
        listOf(
            sequenceWithJson(),
            sequenceWithJson(listOf(MichelineLiteral.Integer(0))),
            sequenceWithJson(
                listOf(
                    MichelinePrimitiveApplication(
                        "prim",
                        listOf(MichelineLiteral.Integer(0)),
                        listOf("%annot")
                    )
                )
            ),
        ).forEach {
            val jsonElement = Json.decodeFromString<JsonElement>(it.second)
            assertEquals(Json.encodeToString(jsonElement), Json.encodeToString(it.first))
            assertEquals(jsonElement, michelineJsonCoder.encode(it.first))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(tezos))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(michelineJsonCoder))
        }
    }

    @Test
    fun `should decode Micheline from JSON`() = withTezosContext {
        listOf(
            literalIntegerWithJson(),
            literalStringWithJson(),
            literalBytesWithJson(),
            primitiveApplicationWithJson(),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0))),
            primitiveApplicationWithJson(annots = listOf("%annot")),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0)), annots = listOf("%annot")),
            sequenceWithJson(),
            sequenceWithJson(listOf(MichelineLiteral.Integer(0))),
            sequenceWithJson(
                listOf(
                    MichelinePrimitiveApplication(
                        "prim",
                        listOf(MichelineLiteral.Integer(0)),
                        listOf("%annot")
                    )
                )
            ),
        ).forEach {
            assertEquals(it.first, Json.decodeFromString(it.second))
            assertEquals(it.first, michelineJsonCoder.decode(Json.decodeFromString(it.second)))
            assertEquals(it.first, Micheline.fromJsonString(it.second, tezos))
            assertEquals(it.first, Micheline.fromJsonString(it.second, michelineJsonCoder))
        }
    }

    @Test
    fun `should decode Micheline Literal from JSON`() = withTezosContext {
        listOf(
            literalIntegerWithJson(),
            literalStringWithJson(),
            literalBytesWithJson(),
        ).forEach {
            assertEquals(it.first, Json.decodeFromString(it.second))
            assertEquals(it.first, michelineJsonCoder.decode(Json.decodeFromString(it.second)))
            assertEquals(it.first, Micheline.fromJsonString(it.second, tezos))
            assertEquals(it.first, Micheline.fromJsonString(it.second, michelineJsonCoder))
        }

        listOf(
            literalBytesWithJson(),
        ).forEach {
            assertEquals(it.first, Json.decodeFromString(it.second))
            assertEquals(it.first, michelineJsonCoder.decode(Json.decodeFromString(it.second)))
            assertEquals(it.first, Micheline.fromJsonString(it.second, tezos))
            assertEquals(it.first, Micheline.fromJsonString(it.second, michelineJsonCoder))
        }
    }

    @Test
    fun `should decode Micheline Primitive Application from JSON`() = withTezosContext {
        listOf(
            primitiveApplicationWithJson(),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0))),
            primitiveApplicationWithJson(annots = listOf("%annot")),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0)), annots = listOf("%annot")),
        ).forEach {
            assertEquals(it.first, Json.decodeFromString(it.second))
            assertEquals(it.first, michelineJsonCoder.decode(Json.decodeFromString(it.second)))
            assertEquals(it.first, Micheline.fromJsonString(it.second, tezos))
            assertEquals(it.first, Micheline.fromJsonString(it.second, michelineJsonCoder))
        }
    }

    @Test
    fun `should decode Micheline Sequence from JSON`() = withTezosContext {
        listOf(
            sequenceWithJson(),
            sequenceWithJson(listOf(MichelineLiteral.Integer(0))),
            sequenceWithJson(
                listOf(
                    MichelinePrimitiveApplication(
                        "prim",
                        listOf(MichelineLiteral.Integer(0)),
                        listOf("%annot")
                    )
                )
            ),
        ).forEach {
            assertEquals(it.first, Json.decodeFromString(it.second))
            assertEquals(it.first, michelineJsonCoder.decode(Json.decodeFromString(it.second)))
            assertEquals(it.first, Micheline.fromJsonString(it.second, tezos))
            assertEquals(it.first, Micheline.fromJsonString(it.second, michelineJsonCoder))
        }
    }

    private fun literalIntegerWithJson(int: Int = 0): Pair<MichelineLiteral.Integer, String> =
        MichelineLiteral.Integer(int) to """
            {
              "int": "$int"
            }
        """.trimIndent()

    private fun literalStringWithJson(string: String = "string"): Pair<MichelineLiteral.String, String> =
        MichelineLiteral.String(string) to """
            {
              "string": "$string"
            }
        """.trimIndent()

    private fun literalBytesWithJson(bytes: String = "0x"): Pair<MichelineLiteral.Bytes, String> =
        MichelineLiteral.Bytes(bytes) to """
            {
              "bytes": "${bytes.removePrefix("0x")}"
            }
        """.trimIndent()

    private fun primitiveApplicationWithJson(
        prim: String = "prim",
        args: List<Micheline> = emptyList(),
        annots: List<String> = emptyList(),
    ): Pair<MichelinePrimitiveApplication, String> {
        val fields = mapOf(
            "prim" to Json.encodeToJsonElement(prim),
            "args" to args.takeIf { it.isNotEmpty() }?.let { Json.encodeToJsonElement(it) },
            "annots" to annots.takeIf { it.isNotEmpty() }?.let { Json.encodeToJsonElement(it) },
        ).filterValuesNotNull()

        return MichelinePrimitiveApplication(prim, args, annots) to JsonObject(fields).toString()
    }

    private fun sequenceWithJson(expressions: List<Micheline> = emptyList()): Pair<MichelineSequence, String> =
        MichelineSequence(expressions) to Json.encodeToString(expressions)
}