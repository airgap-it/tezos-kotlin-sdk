package it.airgap.tezos.michelson.internal.normalizer

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.normalizer.normalized
import mockTezos
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class MichelineNormalizerTest {

    private lateinit var tezos: Tezos

    private lateinit var michelineNormalizer: MichelineNormalizer
    private lateinit var michelinePrimitiveApplicationNormalizer: MichelinePrimitiveApplicationNormalizer
    private lateinit var michelineSequenceNormalizer: MichelineSequenceNormalizer

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()

        michelineNormalizer = MichelineNormalizer()
        michelinePrimitiveApplicationNormalizer = MichelinePrimitiveApplicationNormalizer(michelineNormalizer)
        michelineSequenceNormalizer = MichelineSequenceNormalizer(michelineNormalizer)
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
            assertEquals(it.first, michelineNormalizer.normalize(it.second))
            assertEquals(it.first, it.second.normalized(tezos))
            assertEquals(it.first, it.second.normalized(michelineNormalizer))
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
            assertEquals(it.first, michelineNormalizer.normalize(it.second))
            assertEquals(it.first, it.second.normalized(tezos))
            assertEquals(it.first, it.second.normalized(michelineNormalizer))
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
            assertEquals(it.first, michelineNormalizer.normalize(it.second))
            assertEquals(it.first, it.second.normalized(tezos))
            assertEquals(it.first, it.second.normalized(michelineNormalizer))
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