package it.airgap.tezos.michelson.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.michelson.converter.toCompactExpression
import it.airgap.tezos.michelson.converter.toExpression
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelineToStringConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var michelineToStringConverter: MichelineToStringConverter
    private lateinit var michelineToCompactStringConverter: MichelineToCompactStringConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        michelineToStringConverter = MichelineToStringConverter()
        michelineToCompactStringConverter = MichelineToCompactStringConverter()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert Micheline Literal to string`() {
        val expectedWithMicheline = listOf(
            "1" to MichelineLiteral.Integer(1),
            "string" to MichelineLiteral.String("string"),
            "0x00" to MichelineLiteral.Bytes("0x00"),
        )

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToStringConverter.convert(it.second))
            assertEquals(it.first, it.second.toExpression(tezos))
            assertEquals(it.first, it.second.toExpression(michelineToStringConverter))
        }
    }

    @Test
    fun `should convert Micheline Literal to compact string`() {
        val expectedWithMicheline = listOf(
            "1" to MichelineLiteral.Integer(1),
            "string" to MichelineLiteral.String("string"),
            "0x00" to MichelineLiteral.Bytes("0x00"),
        )

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToCompactStringConverter.convert(it.second))
            assertEquals(it.first, it.second.toCompactExpression(tezos))
            assertEquals(it.first, it.second.toCompactExpression(michelineToCompactStringConverter))
        }
    }

    @Test
    fun `should convert Micheline Primitive Application to string`() {
        val expectedWithMicheline = listOf(
            "prim" to MichelinePrimitiveApplication("prim"),
            "prim arg1 arg2" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(MichelineLiteral.String("arg1"), MichelineLiteral.String("arg2")),
            ),
            "prim %annot" to MichelinePrimitiveApplication(
                "prim",
                annots = listOf("%annot"),
            ),
            "prim %annot1 @annot2" to MichelinePrimitiveApplication(
                "prim",
                annots = listOf("%annot1", "@annot2"),
            ),
            "prim %annot1 @annot2 arg1 arg2" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(MichelineLiteral.String("arg1"), MichelineLiteral.String("arg2")),
                annots = listOf("%annot1", "@annot2"),
            ),
            "prim (prim1 arg11 arg12) (prim2 arg21 arg22)" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(
                    MichelinePrimitiveApplication(
                        "prim1",
                        args = listOf(MichelineLiteral.String("arg11"), MichelineLiteral.String("arg12")),
                    ),
                    MichelinePrimitiveApplication(
                        "prim2",
                        args = listOf(MichelineLiteral.String("arg21"), MichelineLiteral.String("arg22")),
                    ),
                ),
            ),
            "prim arg1 arg2 { arg3_expr1 ; arg3_expr2 }" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(
                    MichelineLiteral.String("arg1"),
                    MichelineLiteral.String("arg2"),
                    MichelineSequence(
                        MichelineLiteral.String("arg3_expr1"),
                        MichelineLiteral.String("arg3_expr2"),
                    ),
                ),
            ),
        )

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToStringConverter.convert(it.second))
            assertEquals(it.first, it.second.toExpression(tezos))
            assertEquals(it.first, it.second.toExpression(michelineToStringConverter))
        }
    }

    @Test
    fun `should convert Micheline Primitive Application to compact string`() {
        val expectedWithMicheline = listOf(
            "prim" to MichelinePrimitiveApplication("prim"),
            "prim arg1 arg2" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(MichelineLiteral.String("arg1"), MichelineLiteral.String("arg2")),
            ),
            "prim arg1 ... arg3" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(
                    MichelineLiteral.String("arg1"),
                    MichelineLiteral.String("arg2"),
                    MichelineLiteral.String("arg3"),
                ),
            ),
            "prim %annot" to MichelinePrimitiveApplication(
                "prim",
                annots = listOf("%annot"),
            ),
            "prim %annot1 @annot2" to MichelinePrimitiveApplication(
                "prim",
                annots = listOf("%annot1", "@annot2"),
            ),
            "prim %annot1 ... !annot" to MichelinePrimitiveApplication(
                "prim",
                annots = listOf("%annot1", "@annot2", "!annot"),
            ),
            "prim %annot1 @annot2 arg1 arg2" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(MichelineLiteral.String("arg1"), MichelineLiteral.String("arg2")),
                annots = listOf("%annot1", "@annot2"),
            ),
            "prim %annot1 ... !annot3 arg1 ... arg3" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(
                    MichelineLiteral.String("arg1"),
                    MichelineLiteral.String("arg2"),
                    MichelineLiteral.String("arg3"),
                ),
                annots = listOf("%annot1", "@annot2", "!annot3"),
            ),
            "prim (prim1) (prim2)" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(
                    MichelinePrimitiveApplication("prim1"),
                    MichelinePrimitiveApplication("prim2"),
                ),
            ),
            "prim (prim1 ...) (prim2 ...)" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(
                    MichelinePrimitiveApplication(
                        "prim1",
                        args = listOf(MichelineLiteral.String("arg11"), MichelineLiteral.String("arg12")),
                    ),
                    MichelinePrimitiveApplication(
                        "prim2",
                        args = listOf(MichelineLiteral.String("arg21"), MichelineLiteral.String("arg22")),
                    ),
                ),
            ),
            "prim arg1 ... { arg3_expr1 ; arg3_expr2 }" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(
                    MichelineLiteral.String("arg1"),
                    MichelineLiteral.String("arg2"),
                    MichelineSequence(
                        MichelineLiteral.String("arg3_expr1"),
                        MichelineLiteral.String("arg3_expr2"),
                    ),
                ),
            ),
            "prim arg1 ... { arg3_expr1 ; ... ; arg3_expr3 }" to MichelinePrimitiveApplication(
                "prim",
                args = listOf(
                    MichelineLiteral.String("arg1"),
                    MichelineLiteral.String("arg2"),
                    MichelineSequence(
                        MichelineLiteral.String("arg3_expr1"),
                        MichelineLiteral.String("arg3_expr2"),
                        MichelineLiteral.String("arg3_expr3"),
                    ),
                ),
            ),
        )

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToCompactStringConverter.convert(it.second))
            assertEquals(it.first, it.second.toCompactExpression(tezos))
        }
    }

    @Test
    fun `should convert Micheline Sequence to string`() {
        val expectedWithMicheline = listOf(
            "{ expr1 ; expr2 ; expr3 ; expr4 }" to MichelineSequence(
                MichelineLiteral.String("expr1"),
                MichelineLiteral.String("expr2"),
                MichelineLiteral.String("expr3"),
                MichelineLiteral.String("expr4"),
            ),
            "{ prim arg1 arg2 }" to MichelineSequence(
                MichelinePrimitiveApplication(
                    "prim",
                    args = listOf(
                        MichelineLiteral.String("arg1"),
                        MichelineLiteral.String("arg2"),
                    ),
                ),
            ),
            "{ prim1 arg11 arg12 ; prim2 arg21 arg22 }" to MichelineSequence(
                MichelinePrimitiveApplication(
                    "prim1",
                    args = listOf(
                        MichelineLiteral.String("arg11"),
                        MichelineLiteral.String("arg12"),
                    ),
                ),
                MichelinePrimitiveApplication(
                    "prim2",
                    args = listOf(
                        MichelineLiteral.String("arg21"),
                        MichelineLiteral.String("arg22"),
                    ),
                ),
            ),
            "{ prim (prim1 arg11 arg12) (prim2 arg21 arg22) ; expr1 }" to MichelineSequence(
                MichelinePrimitiveApplication(
                    "prim",
                    args = listOf(
                        MichelinePrimitiveApplication(
                            "prim1",
                            args = listOf(MichelineLiteral.String("arg11"), MichelineLiteral.String("arg12")),
                        ),
                        MichelinePrimitiveApplication(
                            "prim2",
                            args = listOf(MichelineLiteral.String("arg21"), MichelineLiteral.String("arg22")),
                        ),
                    ),
                ),
                MichelineLiteral.String("expr1")
            ),
        )

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToStringConverter.convert(it.second))
            assertEquals(it.first, it.second.toExpression(tezos))
            assertEquals(it.first, it.second.toExpression(michelineToStringConverter))
        }
    }

    @Test
    fun `should convert Micheline Sequence to compact string`() {
        val expectedWithMicheline = listOf(
            "{ }" to MichelineSequence(),
            "{ expr1 ; expr2 }" to MichelineSequence(
                MichelineLiteral.String("expr1"),
                MichelineLiteral.String("expr2"),
            ),
            "{ expr1 ; ... ; expr4 }" to MichelineSequence(
                MichelineLiteral.String("expr1"),
                MichelineLiteral.String("expr2"),
                MichelineLiteral.String("expr3"),
                MichelineLiteral.String("expr4"),
            ),
            "{ prim arg1 arg2 }" to MichelineSequence(
                MichelinePrimitiveApplication(
                    "prim",
                    args = listOf(
                        MichelineLiteral.String("arg1"),
                        MichelineLiteral.String("arg2"),
                    ),
                ),
            ),
            "{ prim1 arg11 arg12 ; prim2 arg21 arg22 }" to MichelineSequence(
                MichelinePrimitiveApplication(
                    "prim1",
                    args = listOf(
                        MichelineLiteral.String("arg11"),
                        MichelineLiteral.String("arg12"),
                    ),
                ),
                MichelinePrimitiveApplication(
                    "prim2",
                    args = listOf(
                        MichelineLiteral.String("arg21"),
                        MichelineLiteral.String("arg22"),
                    ),
                ),
            ),
            "{ prim (prim1 ...) (prim2 ...) ; expr1 }" to MichelineSequence(
                MichelinePrimitiveApplication(
                    "prim",
                    args = listOf(
                        MichelinePrimitiveApplication(
                            "prim1",
                            args = listOf(MichelineLiteral.String("arg11"), MichelineLiteral.String("arg12")),
                        ),
                        MichelinePrimitiveApplication(
                            "prim2",
                            args = listOf(MichelineLiteral.String("arg21"), MichelineLiteral.String("arg22")),
                        ),
                    ),
                ),
                MichelineLiteral.String("expr1"),
            ),
        )

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToCompactStringConverter.convert(it.second))
            assertEquals(it.first, it.second.toCompactExpression(tezos))
        }
    }
}