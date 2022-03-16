package it.airgap.tezos.michelson.internal.coder

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.fromJsonString
import it.airgap.tezos.michelson.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.toJsonString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelineJsonCoderTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var michelineJsonCoder: MichelineJsonCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        michelineJsonCoder = MichelineJsonCoder()

        every { dependencyRegistry.michelineJsonCoder } returns michelineJsonCoder
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode Micheline Node to JSON`() {
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
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString())
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(michelineJsonCoder))
        }
    }

    @Test
    fun `should encode Micheline Literal to JSON`() {
        listOf(
            literalIntegerWithJson(),
            literalStringWithJson(),
            literalBytesWithJson(),
            literalBytesWithJson("0x00"),
        ).forEach {
            val jsonElement = Json.decodeFromString<JsonElement>(it.second)
            assertEquals(Json.encodeToString(jsonElement), Json.encodeToString(it.first))
            assertEquals(jsonElement, michelineJsonCoder.encode(it.first))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString())
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(michelineJsonCoder))
        }

        listOf(
            literalBytesWithJson(),
            literalBytesWithJson("0x00"),
        ).forEach {
            val jsonElement = Json.decodeFromString<JsonElement>(it.second)
            assertEquals(Json.encodeToString(jsonElement), Json.encodeToString(it.first))
            assertEquals(jsonElement, michelineJsonCoder.encode(it.first))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString())
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(michelineJsonCoder))
        }
    }

    @Test
    fun `should encode Micheline Primitive Application to JSON`() {
        listOf(
            primitiveApplicationWithJson(),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0))),
            primitiveApplicationWithJson(annots = listOf("%annot")),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0)), annots = listOf("%annot")),
        ).forEach {
            val jsonElement = Json.decodeFromString<JsonElement>(it.second)
            assertEquals(Json.encodeToString(jsonElement), Json.encodeToString(it.first))
            assertEquals(jsonElement, michelineJsonCoder.encode(it.first))
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString())
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(michelineJsonCoder))
        }
    }

    @Test
    fun `should encode Micheline Sequence to JSON`() {
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
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString())
            assertEquals(Json.encodeToString(jsonElement), it.first.toJsonString(michelineJsonCoder))
        }
    }

    @Test
    fun `should decode Micheline Node from JSON`() {
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
            assertEquals(it.first, MichelineNode.fromJsonString(it.second))
            assertEquals(it.first, MichelineNode.fromJsonString(it.second, michelineJsonCoder))
        }
    }

    @Test
    fun `should decode Micheline Literal from JSON`() {
        listOf(
            literalIntegerWithJson(),
            literalStringWithJson(),
            literalBytesWithJson(),
        ).forEach {
            assertEquals(it.first, Json.decodeFromString(it.second))
            assertEquals(it.first, michelineJsonCoder.decode(Json.decodeFromString(it.second)))
            assertEquals(it.first, MichelineNode.fromJsonString(it.second))
            assertEquals(it.first, MichelineNode.fromJsonString(it.second, michelineJsonCoder))
        }

        listOf(
            literalBytesWithJson(),
        ).forEach {
            assertEquals(it.first, Json.decodeFromString(it.second))
            assertEquals(it.first, michelineJsonCoder.decode(Json.decodeFromString(it.second)))
            assertEquals(it.first, MichelineNode.fromJsonString(it.second))
            assertEquals(it.first, MichelineNode.fromJsonString(it.second, michelineJsonCoder))
        }
    }

    @Test
    fun `should decode Micheline Primitive Application from JSON`() {
        listOf(
            primitiveApplicationWithJson(),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0))),
            primitiveApplicationWithJson(annots = listOf("%annot")),
            primitiveApplicationWithJson(args = listOf(MichelineLiteral.Integer(0)), annots = listOf("%annot")),
        ).forEach {
            assertEquals(it.first, Json.decodeFromString(it.second))
            assertEquals(it.first, michelineJsonCoder.decode(Json.decodeFromString(it.second)))
            assertEquals(it.first, MichelineNode.fromJsonString(it.second))
            assertEquals(it.first, MichelineNode.fromJsonString(it.second, michelineJsonCoder))
        }
    }

    @Test
    fun `should decode Micheline Sequence from JSON`() {
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
            assertEquals(it.first, MichelineNode.fromJsonString(it.second))
            assertEquals(it.first, MichelineNode.fromJsonString(it.second, michelineJsonCoder))
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
        args: List<MichelineNode> = emptyList(),
        annots: List<String> = emptyList(),
    ): Pair<MichelinePrimitiveApplication, String> =
        MichelinePrimitiveApplication(prim, args, annots) to """
            {
              "prim": "$prim",
              "args": ${Json.encodeToJsonElement(args)},
              "annots": ${Json.encodeToJsonElement(annots)}
            }
        """.trimIndent()

    private fun sequenceWithJson(expressions: List<MichelineNode> = emptyList()): Pair<MichelineSequence, String> =
        MichelineSequence(expressions) to Json.encodeToString(expressions)
}