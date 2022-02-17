package it.airgap.tezos.michelson.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.normalized
import mockTezosSdk
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class MichelineToNormalizedConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var michelineToNormalizedConverter: MichelineToNormalizedConverter
    private lateinit var michelinePrimitiveApplicationToNormalizedConverter: MichelinePrimitiveApplicationToNormalizedConverter
    private lateinit var michelineSequenceToNormalizedConverter: MichelineSequenceToNormalizedConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        michelineToNormalizedConverter = MichelineToNormalizedConverter()
        michelinePrimitiveApplicationToNormalizedConverter =
            MichelinePrimitiveApplicationToNormalizedConverter(michelineToNormalizedConverter)
        michelineSequenceToNormalizedConverter = MichelineSequenceToNormalizedConverter(michelineToNormalizedConverter)

        every { dependencyRegistry.michelineToNormalizedConverter } returns michelineToNormalizedConverter
        every { dependencyRegistry.michelinePrimitiveApplicationToNormalizedConverter } returns michelinePrimitiveApplicationToNormalizedConverter
        every { dependencyRegistry.michelineSequenceToNormalizedConverter } returns michelineSequenceToNormalizedConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should normalize Micheline Literal`() {
        val expectedWithMicheline = listOf(
            MichelineLiteral.Integer(0),
            MichelineLiteral.String("string"),
            MichelineLiteral.Bytes("0x00"),
        ).map { it to it }

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToNormalizedConverter.convert(it.second))
            assertEquals(it.first, it.second.normalized())
            assertEquals(it.first, it.second.normalized(michelineToNormalizedConverter))
        }
    }

    @Test
    fun `should normalize Micheline Primitive Application`() {
        val expectedWithMicheline = expectedWithMichelinePairs + expectedWithNestedPairs + listOf(
            MichelinePrimitiveApplication("Unit"),
            MichelinePrimitiveApplication("True"),
            MichelinePrimitiveApplication("unit"),
            MichelinePrimitiveApplication("bool"),
        ).map { it to it }

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToNormalizedConverter.convert(it.second))
            assertEquals(it.first, it.second.normalized())
            assertEquals(it.first, it.second.normalized(michelineToNormalizedConverter))
        }
    }

    @Test
    fun `should normalize Micheline Sequence`() {
        val expectedWithMicheline =
            expectedWithMichelinePairs.map {
                MichelineSequence(it.first) to MichelineSequence(it.second)
            } + expectedWithMichelinePairs.fold(MichelineSequence() to MichelineSequence()) { acc, (expected, pair) ->
                MichelineSequence(acc.first.nodes + expected) to MichelineSequence(acc.second.nodes + pair)
            }

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToNormalizedConverter.convert(it.second))
            assertEquals(it.first, it.second.normalized())
            assertEquals(it.first, it.second.normalized(michelineToNormalizedConverter))
        }
    }

    private val expectedWithMichelinePairs = listOf(
        MichelinePrimitiveApplication(
            prim = "Pair",
            args = listOf(
                MichelinePrimitiveApplication("Unit"),
                MichelinePrimitiveApplication(
                    prim = "Pair",
                    args = listOf(
                        MichelinePrimitiveApplication("Unit"),
                        MichelinePrimitiveApplication("Unit"),
                    ),
                ),
            ),
        ) to MichelinePrimitiveApplication(
            prim = "Pair",
            args = listOf(
                MichelinePrimitiveApplication("Unit"),
                MichelinePrimitiveApplication("Unit"),
                MichelinePrimitiveApplication("Unit"),
            ),
        ),
        MichelinePrimitiveApplication(
            prim = "pair",
            args = listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication(
                    prim = "pair",
                    args = listOf(
                        MichelinePrimitiveApplication("unit"),
                        MichelinePrimitiveApplication("unit"),
                    ),
                ),
            ),
        ) to MichelinePrimitiveApplication(
            prim = "pair",
            args = listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
            ),
        ),
        MichelinePrimitiveApplication(
            prim = "Pair",
            args = listOf(
                MichelinePrimitiveApplication("Unit"),
                MichelinePrimitiveApplication(
                    prim = "Pair",
                    args = listOf(
                        MichelinePrimitiveApplication("Unit"),
                        MichelinePrimitiveApplication(
                            prim = "Pair",
                            args = listOf(
                                MichelinePrimitiveApplication("Unit"),
                                MichelinePrimitiveApplication("Unit"),
                            ),
                        )
                    ),
                ),
            ),
        ) to MichelinePrimitiveApplication(
            prim = "Pair",
            args = listOf(
                MichelinePrimitiveApplication("Unit"),
                MichelinePrimitiveApplication("Unit"),
                MichelinePrimitiveApplication("Unit"),
                MichelinePrimitiveApplication("Unit"),
            ),
        ),
        MichelinePrimitiveApplication(
            prim = "pair",
            args = listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication(
                    prim = "pair",
                    args = listOf(
                        MichelinePrimitiveApplication("unit"),
                        MichelinePrimitiveApplication(
                            prim = "pair",
                            args = listOf(
                                MichelinePrimitiveApplication("unit"),
                                MichelinePrimitiveApplication("unit"),
                            ),
                        )
                    ),
                ),
            ),
        ) to MichelinePrimitiveApplication(
            prim = "pair",
            args = listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
            ),
        ),
    )

    private val expectedWithNestedPairs: List<Pair<MichelineNode, MichelineNode>>
        get() = expectedWithMichelinePairs.map {
            MichelinePrimitiveApplication(
                prim = "Some",
                args = listOf(it.first)
            ) to MichelinePrimitiveApplication(
                prim = "Some",
                args = listOf(it.second),
            )
        }
}