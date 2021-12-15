package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.unit
import org.junit.Test
import kotlin.test.assertEquals

class MichelineDslTest {

    @Test
    fun `builds Micheline Literal`() {
        assertEquals(
            MichelineLiteral.Integer(0),
            micheline { int("0") },
        )

        assertEquals(
            MichelineLiteral.Integer(0),
            micheline { int((0).toByte()) },
        )

        assertEquals(
            MichelineLiteral.Integer(0),
            micheline { int((0).toShort()) },
        )

        assertEquals(
            MichelineLiteral.Integer(0),
            micheline { int(0) },
        )

        assertEquals(
            MichelineLiteral.Integer(0),
            micheline { int(0L) },
        )

        assertEquals(
            MichelineLiteral.String("string"),
            micheline { string("string") },
        )

        assertEquals(
            MichelineLiteral.Bytes("0x00"),
            micheline { bytes("0x00") },
        )

        assertEquals(
            MichelineLiteral.Bytes("0x00"),
            micheline { bytes("00") },
        )

        assertEquals(
            MichelineLiteral.Bytes("0x00"),
            micheline { bytes(byteArrayOf(0)) },
        )
    }

    @Test
    fun `builds Micheline Primitive Application`() {
        assertEquals(
            MichelinePrimitiveApplication("Unit"),
            micheline {
                primitiveApplication(MichelsonData.Unit) {}
            },
        )

        assertEquals(
            MichelinePrimitiveApplication(
                "Pair",
                args = listOf(MichelineLiteral.Integer(0), MichelineLiteral.String("string")),
            ),
            micheline {
                primitiveApplication(MichelsonData.Pair)  {
                    arg { int(0) }
                    arg { string("string") }
                }
            },
        )

        assertEquals(
            MichelinePrimitiveApplication(
                "Pair",
                listOf(MichelineLiteral.Integer(0), MichelineLiteral.String("string")),
                listOf("%annot")
            ),
            micheline {
                primitiveApplication(MichelsonData.Pair)  {
                    arg { int(0) }
                    arg { string("string") }
                    annots("%annot")
                }
            },
        )

        assertEquals(
            MichelinePrimitiveApplication(
                "Pair",
                listOf(MichelineLiteral.Integer(0), MichelineLiteral.String("string")),
                listOf("%annot")
            ),
            micheline {
                primitiveApplication(MichelsonData.Pair)  {
                    arg { int(0) }
                    arg { string("string") }
                } annots(listOf("%annot"))
            },
        )

        assertEquals(
            MichelinePrimitiveApplication(
                "Pair",
                args = listOf(
                    MichelineLiteral.Integer(0),
                    MichelineLiteral.String("string"),
                    MichelinePrimitiveApplication("Unit", annots = listOf("%arg"))
                ),
            ),
            micheline {
                primitiveApplication(MichelsonData.Pair)  {
                    arg { int(0) }
                    arg { string("string") }
                    arg {
                        primitiveApplication(MichelsonData.Unit) annots listOf("%arg")
                    }
                }
            },
        )
    }

    @Test
    fun `builds Micheline Sequence`() {
        assertEquals(
            MichelineSequence(MichelineLiteral.Integer(0)),
            micheline {
                sequence { int(0) }
            }
        )
        assertEquals(
            MichelineSequence(
                MichelineLiteral.Integer(0),
                MichelineLiteral.String("string"),
            ),
            micheline {
                int(0)
                string("string")
            }
        )
        assertEquals(
            MichelineSequence(
                MichelinePrimitiveApplication("unit"),
                MichelineSequence(
                    MichelineLiteral.Integer(0),
                    MichelineLiteral.String("string"),
                ),
            ),
            micheline {
                unit
                sequence {
                    int(0)
                    string("string")
                }
            }
        )
    }

    @Test
    fun `builds Micheline from Michelson`() {
        assertEquals(
            MichelinePrimitiveApplication("Unit"),
            micheline {
                michelson(MichelsonData.Unit)
            }
        )
    }
}