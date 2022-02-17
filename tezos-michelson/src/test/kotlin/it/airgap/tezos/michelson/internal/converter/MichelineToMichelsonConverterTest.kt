package it.airgap.tezos.michelson.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.toMichelson
import michelsonComparableTypeMichelinePairs
import michelsonMichelinePairs
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MichelineToMichelsonConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var michelineToMichelsonConverter: MichelineToMichelsonConverter

    private lateinit var stringToMichelsonPrimConverter: StringToMichelsonPrimConverter
    private lateinit var michelineToCompactStringConverter: MichelineToCompactStringConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        stringToMichelsonPrimConverter = spyk(StringToMichelsonPrimConverter())
        michelineToCompactStringConverter = MichelineToCompactStringConverter()

        michelineToMichelsonConverter = MichelineToMichelsonConverter(stringToMichelsonPrimConverter, michelineToCompactStringConverter)

        every { dependencyRegistry.michelineToMichelsonConverter } returns michelineToMichelsonConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert Micheline Literal to Michelson`() {
        val expectedWithMicheline = listOf(
            MichelsonData.IntConstant(1) to MichelineLiteral.Integer(1),
            MichelsonData.StringConstant("string") to MichelineLiteral.String("string"),
            MichelsonData.ByteSequenceConstant("0x00") to MichelineLiteral.Bytes("0x00"),
        )

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToMichelsonConverter.convert(it.second))
            assertEquals(it.first, it.second.toMichelson())
            assertEquals(it.first, it.second.toMichelson(michelineToMichelsonConverter))
        }
    }

    @Test
    fun `should convert Micheline Primitive Application to Michelson`() {
        @Suppress("UNCHECKED_CAST")
        val expectedWithMicheline1 = michelsonMichelinePairs.filter { it.second is MichelinePrimitiveApplication }

        expectedWithMicheline1.forEach {
            assertEquals(it.first, michelineToMichelsonConverter.convert(it.second))
            assertEquals(it.first, it.second.toMichelson())
            assertEquals(it.first, it.second.toMichelson(michelineToMichelsonConverter))
        }

        val expectedWithMicheline2 = michelsonComparableTypeMichelinePairs.filter { it.second is MichelinePrimitiveApplication }

        every { stringToMichelsonPrimConverter.convert("option") } returns MichelsonComparableType.Option
        every { stringToMichelsonPrimConverter.convert("or") } returns MichelsonComparableType.Or
        every { stringToMichelsonPrimConverter.convert("pair") } returns MichelsonComparableType.Pair

        expectedWithMicheline2.forEach {
            assertEquals(it.first, michelineToMichelsonConverter.convert(it.second))
            assertEquals(it.first, it.second.toMichelson())
            assertEquals(it.first, it.second.toMichelson(michelineToMichelsonConverter))
        }
    }

    @Test
    fun `should fail to convert invalid Micheline Primitive Application`() {
        val unknownMicheline = listOf(
            MichelinePrimitiveApplication("unknown_prim")
        )

        unknownMicheline.forEach {
            assertFailsWith<IllegalArgumentException> { michelineToMichelsonConverter.convert(it) }
        }

        val invalidMicheline: List<MichelinePrimitiveApplication> = listOf(

            // -- MichelsonData --

            MichelinePrimitiveApplication("Pair") /* empty args */,
            MichelinePrimitiveApplication("Pair", listOf(MichelinePrimitiveApplication("Unit"))) /* not enough args */,
            MichelinePrimitiveApplication("Pair", listOf(MichelinePrimitiveApplication("Unit"), MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("Left") /* empty args */,
            MichelinePrimitiveApplication("Left", listOf(MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("Right") /* empty args */,
            MichelinePrimitiveApplication("Right", listOf(MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("Some") /* empty args */,
            MichelinePrimitiveApplication("Some", listOf(MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("Elt") /* empty args */,
            MichelinePrimitiveApplication("Elt", listOf(MichelinePrimitiveApplication("Unit"))) /* not enough args */,
            MichelinePrimitiveApplication("Elt", listOf(MichelinePrimitiveApplication("Unit"), MichelinePrimitiveApplication("unit"))) /* invalid arg types*/,

            // -- MichelsonInstruction --

            MichelinePrimitiveApplication("DROP", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("DUB", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("DIG") /* empty args */,
            MichelinePrimitiveApplication("DIG", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("DUG") /* empty args */,
            MichelinePrimitiveApplication("DUG", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("PUSH") /* empty args */,
            MichelinePrimitiveApplication("PUSH", listOf(MichelinePrimitiveApplication("unit"))) /* not enough args */,
            MichelinePrimitiveApplication("PUSH", listOf(MichelinePrimitiveApplication("Unit"), MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("NONE") /* empty args */,
            MichelinePrimitiveApplication("NONE", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("IF_NONE") /* empty args */,
            MichelinePrimitiveApplication("IF_NONE", listOf(MichelinePrimitiveApplication("UNIT"))) /* not enough args */,
            MichelinePrimitiveApplication("IF_NONE", listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("PAIR", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("UNPAIR", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("LEFT") /* empty args */,
            MichelinePrimitiveApplication("LEFT", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("RIGHT") /* empty args */,
            MichelinePrimitiveApplication("RIGHT", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("IF_LEFT") /* empty args */,
            MichelinePrimitiveApplication("IF_LEFT", listOf(MichelinePrimitiveApplication("UNIT"))) /* not enough args */,
            MichelinePrimitiveApplication("IF_LEFT", listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("NIL") /* empty args */,
            MichelinePrimitiveApplication("NIL", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("IF_CONS") /* empty args */,
            MichelinePrimitiveApplication("IF_CONS", listOf(MichelinePrimitiveApplication("UNIT"))) /* not enough args */,
            MichelinePrimitiveApplication("IF_CONS", listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("EMPTY_SET") /* empty args */,
            MichelinePrimitiveApplication("EMPTY_SET", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("EMPTY_MAP") /* empty args */,
            MichelinePrimitiveApplication("EMPTY_MAP", listOf(MichelinePrimitiveApplication("unit"))) /* not enough args */,
            MichelinePrimitiveApplication("EMPTY_MAP", listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("EMPTY_BIG_MAP") /* empty args */,
            MichelinePrimitiveApplication("EMPTY_BIG_MAP", listOf(MichelinePrimitiveApplication("unit"))) /* not enough args */,
            MichelinePrimitiveApplication("EMPTY_BIG_MAP", listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("MAP") /* empty args */,
            MichelinePrimitiveApplication("MAP", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("ITER") /* empty args */,
            MichelinePrimitiveApplication("ITER", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("GET", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("UPDATE", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("IF") /* empty args */,
            MichelinePrimitiveApplication("IF", listOf(MichelinePrimitiveApplication("UNIT"))) /* not enough args */,
            MichelinePrimitiveApplication("IF", listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("LOOP") /* empty args */,
            MichelinePrimitiveApplication("LOOP", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("LOOP_LEFT") /* empty args */,
            MichelinePrimitiveApplication("LOOP_LEFT", listOf(MichelinePrimitiveApplication("Unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("LAMBDA") /* empty args */,
            MichelinePrimitiveApplication("LAMBDA", listOf(MichelinePrimitiveApplication("unit"))) /* not enough args */,
            MichelinePrimitiveApplication(
                "LAMBDA",
                listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit")
                ),
            ) /* invalid arg types */,

            MichelinePrimitiveApplication("DIP") /* empty args */,
            MichelinePrimitiveApplication("DIP", listOf(MichelineLiteral.Integer(1))) /* not enough args */,
            MichelinePrimitiveApplication("DIP", listOf(MichelinePrimitiveApplication("unit"))) /* invalid arg types */,
            MichelinePrimitiveApplication("DIP", listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("UNPACK") /* empty args */,
            MichelinePrimitiveApplication("UNPACK", listOf(MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("CONTRACT") /* empty args */,
            MichelinePrimitiveApplication("CONTRACT", listOf(MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("CREATE_CONTRACT") /* empty args */,
            MichelinePrimitiveApplication("CREATE_CONTRACT", listOf(MichelinePrimitiveApplication("unit"))) /* not enough args */,
            MichelinePrimitiveApplication(
                "CREATE_CONTRACT",
                listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit")
                ),
            ) /* invalid arg types */,

            MichelinePrimitiveApplication("SAPLING_EMPTY_STATE") /* empty args */,
            MichelinePrimitiveApplication("SAPLING_EMPTY_STATE", listOf(MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            // -- MichelsonType --

            MichelinePrimitiveApplication("parameter") /* empty args */,
            MichelinePrimitiveApplication("parameter", listOf(MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("storage") /* empty args */,
            MichelinePrimitiveApplication("storage", listOf(MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("code") /* empty args */,
            MichelinePrimitiveApplication("code", listOf(MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("option") /* empty args */,
            MichelinePrimitiveApplication("option", listOf(MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("list") /* empty args */,
            MichelinePrimitiveApplication("list", listOf(MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("set") /* empty args */,
            MichelinePrimitiveApplication("set", listOf(MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("contract") /* empty args */,
            MichelinePrimitiveApplication("contract", listOf(MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("ticket") /* empty args */,
            MichelinePrimitiveApplication("ticket", listOf(MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("pair") /* empty args */,
            MichelinePrimitiveApplication("pair", listOf(MichelinePrimitiveApplication("unit"))) /* not enough args */,
            MichelinePrimitiveApplication("pair", listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("or") /* empty args */,
            MichelinePrimitiveApplication("or", listOf(MichelinePrimitiveApplication("unit"))) /* not enough args */,
            MichelinePrimitiveApplication("or", listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("lambda") /* empty args */,
            MichelinePrimitiveApplication("lambda", listOf(MichelinePrimitiveApplication("unit"))) /* not enough args */,
            MichelinePrimitiveApplication("lambda", listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("UNIT"))) /* invalid arg types */,

            MichelinePrimitiveApplication("map") /* empty args */,
            MichelinePrimitiveApplication("map", listOf(MichelinePrimitiveApplication("unit"))) /* not enough args */,
            MichelinePrimitiveApplication("map", listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("big_map") /* empty args */,
            MichelinePrimitiveApplication("big_map", listOf(MichelinePrimitiveApplication("unit"))) /* not enough args */,
            MichelinePrimitiveApplication("big_map", listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("sapling_transaction") /* empty args */,
            MichelinePrimitiveApplication("sapling_transaction", listOf(MichelinePrimitiveApplication("unit"))) /* invalid arg types */,

            MichelinePrimitiveApplication("sapling_state") /* empty args */,
            MichelinePrimitiveApplication("sapling_state", listOf(MichelinePrimitiveApplication("unit"))) /* invalid arg types */,
        )

        invalidMicheline.forEach {
            assertFailsWith<IllegalArgumentException> { michelineToMichelsonConverter.convert(it) }
        }
    }

    @Test
    fun `should convert Micheline Sequence to Michelson`() {
        @Suppress("UNCHECKED_CAST")
        val expectedWithMicheline = michelsonMichelinePairs.filter { it.second is MichelineSequence }

        expectedWithMicheline.forEach {
            assertEquals(it.first, michelineToMichelsonConverter.convert(it.second))
            assertEquals(it.first, it.second.toMichelson())
            assertEquals(it.first, it.second.toMichelson(michelineToMichelsonConverter))
        }
    }

    @Test
    fun `should fail to convert invalid Micheline Sequence`() {
        val unknownMicheline = listOf(
            MichelineSequence(MichelinePrimitiveApplication("unit"))
        )

        unknownMicheline.forEach {
            assertFailsWith<IllegalArgumentException> { michelineToMichelsonConverter.convert(it) }
        }

        val invalidMicheline: List<MichelineSequence> = listOf(
            MichelineSequence(
                MichelinePrimitiveApplication(
                    "Elt",
                    listOf(
                        MichelinePrimitiveApplication("Unit"),
                        MichelinePrimitiveApplication("Unit"),
                    ),
                ),
                MichelineLiteral.Integer(0),
            ),
        )

        invalidMicheline.forEach {
            assertFailsWith<IllegalArgumentException> { michelineToMichelsonConverter.convert(it) }
        }
    }
}