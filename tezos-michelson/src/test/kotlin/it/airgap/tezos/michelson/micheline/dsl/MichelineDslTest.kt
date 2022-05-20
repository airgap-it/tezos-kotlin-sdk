package it.airgap.tezos.michelson.micheline.dsl

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.unit
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelineDslTest {

    private lateinit var tezos: Tezos
    private lateinit var michelsonToMichelineConverter: MichelsonToMichelineConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        michelsonToMichelineConverter = MichelsonToMichelineConverter()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `builds Micheline Literal`() {
        val expectedWithActual = listOf(
            MichelineLiteral.Integer(0) to listOf(
                micheline(tezos) { int("0") },
                micheline(michelsonToMichelineConverter) { int("0") },
                micheline(tezos) { int((0).toByte()) },
                micheline(michelsonToMichelineConverter) { int((0).toByte()) },
                micheline(tezos) { int((0).toShort()) },
                micheline(michelsonToMichelineConverter) { int((0).toShort()) },
                micheline(tezos) { int(0) },
                micheline(michelsonToMichelineConverter) { int(0) },
                micheline(tezos) { int(0L) },
                micheline(michelsonToMichelineConverter) { int(0L) },
            ),
            MichelineLiteral.String("string") to listOf(
                micheline(tezos) { string("string") },
                micheline(michelsonToMichelineConverter) { string("string") },
            ),
            MichelineLiteral.Bytes("0x00") to listOf(
                micheline(tezos) { bytes("0x00") },
                micheline(michelsonToMichelineConverter) { bytes("0x00") },
                micheline(tezos) { bytes("00") },
                micheline(michelsonToMichelineConverter) { bytes("00") },
                micheline(tezos) { bytes(byteArrayOf(0)) },
                micheline(michelsonToMichelineConverter) { bytes(byteArrayOf(0)) },
            ),
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }

    @Test
    fun `builds Micheline Primitive Application`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("Unit") to listOf(
                micheline(tezos) {
                    primitiveApplication(MichelsonData.Unit) {}
                },
                micheline(michelsonToMichelineConverter) {
                    primitiveApplication(MichelsonData.Unit) {}
                },
            ),
            MichelinePrimitiveApplication(
                "Pair",
                args = listOf(MichelineLiteral.Integer(0), MichelineLiteral.String("string")),
            ) to listOf(
                micheline(tezos) {
                    primitiveApplication(MichelsonData.Pair)  {
                        arg { int(0) }
                        arg { string("string") }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    primitiveApplication(MichelsonData.Pair)  {
                        arg { int(0) }
                        arg { string("string") }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "Pair",
                listOf(MichelineLiteral.Integer(0), MichelineLiteral.String("string")),
                listOf("%annot")
            ) to listOf(
                micheline(tezos) {
                    primitiveApplication(MichelsonData.Pair)  {
                        arg { int(0) }
                        arg { string("string") }
                        annots("%annot")
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    primitiveApplication(MichelsonData.Pair)  {
                        arg { int(0) }
                        arg { string("string") }
                        annots("%annot")
                    }
                },
                micheline(tezos) {
                    primitiveApplication(MichelsonData.Pair)  {
                        arg { int(0) }
                        arg { string("string") }
                    } annots(listOf("%annot"))
                },
                micheline(michelsonToMichelineConverter) {
                    primitiveApplication(MichelsonData.Pair)  {
                        arg { int(0) }
                        arg { string("string") }
                    } annots(listOf("%annot"))
                },
            ),
            MichelinePrimitiveApplication(
                "Pair",
                args = listOf(
                    MichelineLiteral.Integer(0),
                    MichelineLiteral.String("string"),
                    MichelinePrimitiveApplication("Unit", annots = listOf("%arg"))
                ),
            ) to listOf(
                micheline(tezos) {
                    primitiveApplication(MichelsonData.Pair)  {
                        arg { int(0) }
                        arg { string("string") }
                        arg {
                            primitiveApplication(MichelsonData.Unit) annots listOf("%arg")
                        }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    primitiveApplication(MichelsonData.Pair)  {
                        arg { int(0) }
                        arg { string("string") }
                        arg {
                            primitiveApplication(MichelsonData.Unit) annots listOf("%arg")
                        }
                    }
                },
            ),
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }

    @Test
    fun `builds Micheline Sequence`() {
        val expectedWithActual = listOf(
            MichelineSequence(MichelineLiteral.Integer(0)) to listOf(
                micheline(tezos) {
                    sequence { int(0) }
                },
                micheline(michelsonToMichelineConverter) {
                    sequence { int(0) }
                },
            ),
            MichelineSequence(
                MichelineLiteral.Integer(0),
                MichelineLiteral.String("string"),
            ) to listOf(
                micheline(tezos) {
                    int(0)
                    string("string")
                },
                micheline(michelsonToMichelineConverter) {
                    int(0)
                    string("string")
                },
            ),
            MichelineSequence(
                MichelinePrimitiveApplication("unit"),
                MichelineSequence(
                    MichelineLiteral.Integer(0),
                    MichelineLiteral.String("string"),
                ),
            ) to listOf(
                micheline(tezos) {
                    unit
                    sequence {
                        int(0)
                        string("string")
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    unit
                    sequence {
                        int(0)
                        string("string")
                    }
                },
            ),
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }

    @Test
    fun `builds Micheline from Michelson`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("Unit") to listOf(
                micheline(tezos) {
                    michelson(MichelsonData.Unit)
                },
                micheline(michelsonToMichelineConverter) {
                    michelson(MichelsonData.Unit)
                }
            ),
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }
}