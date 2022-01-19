package it.airgap.tezos.michelson.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.michelson.internal.static.*
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class StringToMichelsonGrammarTypeConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var stringToMichelsonGrammarTypeConverter: StringToMichelsonGrammarTypeConverter
    private lateinit var stringToMichelsonDataGrammarTypeConverter: StringToMichelsonDataGrammarTypeConverter
    private lateinit var stringToMichelsonInstructionGrammarTypeConverter: StringToMichelsonInstructionGrammarTypeConverter
    private lateinit var stringToMichelsonTypeGrammarTypeConverter: StringToMichelsonTypeGrammarTypeConverter
    private lateinit var stringToMichelsonComparableTypeGrammarTypeConverter: StringToMichelsonComparableTypeGrammarTypeConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        stringToMichelsonGrammarTypeConverter = StringToMichelsonGrammarTypeConverter()
        stringToMichelsonDataGrammarTypeConverter = StringToMichelsonDataGrammarTypeConverter()
        stringToMichelsonInstructionGrammarTypeConverter = StringToMichelsonInstructionGrammarTypeConverter()
        stringToMichelsonTypeGrammarTypeConverter = StringToMichelsonTypeGrammarTypeConverter()
        stringToMichelsonComparableTypeGrammarTypeConverter = StringToMichelsonComparableTypeGrammarTypeConverter()

        every { dependencyRegistry.stringToMichelsonGrammarTypeConverter } returns stringToMichelsonGrammarTypeConverter
        every { dependencyRegistry.stringToMichelsonDataGrammarTypeConverter } returns stringToMichelsonDataGrammarTypeConverter
        every { dependencyRegistry.stringToMichelsonInstructionGrammarTypeConverter } returns stringToMichelsonInstructionGrammarTypeConverter
        every { dependencyRegistry.stringToMichelsonTypeGrammarTypeConverter } returns stringToMichelsonTypeGrammarTypeConverter
        every { dependencyRegistry.stringToMichelsonComparableTypeGrammarTypeConverter } returns stringToMichelsonComparableTypeGrammarTypeConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert name to Michelson GrammarType`() {
        val valuesWithExpected = grammarTypes.map {
            when (it) {
                MichelsonComparableType.Option -> it.name to MichelsonType.Option
                MichelsonComparableType.Pair -> it.name to MichelsonType.Pair
                MichelsonComparableType.Or -> it.name to MichelsonType.Or
                else -> it.name to it
            }
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, stringToMichelsonGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, Michelson.GrammarType.fromStringOrNull(it.first))
            assertEquals(it.second, Michelson.GrammarType.fromStringOrNull(it.first, stringToMichelsonGrammarTypeConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonData GrammarType`() {
        val valuesWithExpected = dataGrammarTypes.map { it.name to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, stringToMichelsonDataGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonData.GrammarType.fromStringOrNull(it.first))
            assertEquals(it.second, MichelsonData.GrammarType.fromStringOrNull(it.first, stringToMichelsonDataGrammarTypeConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonInstruction GrammarType`() {
        val valuesWithExpected = instructionGrammarTypes.map { it.name to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, stringToMichelsonInstructionGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonInstruction.GrammarType.fromStringOrNull(it.first))
            assertEquals(it.second, MichelsonInstruction.GrammarType.fromStringOrNull(it.first, stringToMichelsonInstructionGrammarTypeConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonType GrammarType`() {
        val valuesWithExpected = typeGrammarTypes.map {
            when (it) {
                MichelsonComparableType.Option -> it.name to MichelsonType.Option
                MichelsonComparableType.Pair -> it.name to MichelsonType.Pair
                MichelsonComparableType.Or -> it.name to MichelsonType.Or
                else -> it.name to it
            }
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, stringToMichelsonTypeGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonType.GrammarType.fromStringOrNull(it.first))
            assertEquals(it.second, MichelsonType.GrammarType.fromStringOrNull(it.first, stringToMichelsonTypeGrammarTypeConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonComparableType GrammarType`() {
        val valuesWithExpected = comparableTypeGrammarTypes.map { it.name to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, stringToMichelsonComparableTypeGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonComparableType.GrammarType.fromStringOrNull(it.first))
            assertEquals(it.second, MichelsonComparableType.GrammarType.fromStringOrNull(it.first, stringToMichelsonComparableTypeGrammarTypeConverter))
        }
    }

    @Test
    fun `should fail to convert unknown string to Michelson GrammarType`() {
        val unknownStrings = listOf("unknown")

        unknownStrings.forEach {
            val message = "Unknown Michelson grammar type: \"$it\"."

            assertFailsWith<IllegalArgumentException>(message) {
                stringToMichelsonGrammarTypeConverter.convert(it)
            }
            assertNull(Michelson.GrammarType.fromStringOrNull(it))
            assertNull(Michelson.GrammarType.fromStringOrNull(it, stringToMichelsonGrammarTypeConverter))

            assertFailsWith<IllegalArgumentException>(message) {
                stringToMichelsonDataGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonData.GrammarType.fromStringOrNull(it))
            assertNull(MichelsonData.GrammarType.fromStringOrNull(it, stringToMichelsonDataGrammarTypeConverter))

            assertFailsWith<IllegalArgumentException>(message) {
                stringToMichelsonInstructionGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonInstruction.GrammarType.fromStringOrNull(it))
            assertNull(MichelsonInstruction.GrammarType.fromStringOrNull(it, stringToMichelsonInstructionGrammarTypeConverter))

            assertFailsWith<IllegalArgumentException>(message) {
                stringToMichelsonTypeGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonType.GrammarType.fromStringOrNull(it))
            assertNull(MichelsonType.GrammarType.fromStringOrNull(it, stringToMichelsonTypeGrammarTypeConverter))

            assertFailsWith<IllegalArgumentException>(message) {
                stringToMichelsonComparableTypeGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonComparableType.GrammarType.fromStringOrNull(it))
            assertNull(MichelsonComparableType.GrammarType.fromStringOrNull(it, stringToMichelsonComparableTypeGrammarTypeConverter))
        }
    }
}