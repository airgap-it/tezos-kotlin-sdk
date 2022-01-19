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

class TagToMichelsonGrammarTypeConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var tagToMichelsonGrammarTypeConverter: TagToMichelsonGrammarTypeConverter
    private lateinit var tagToMichelsonDataGrammarTypeConverter: TagToMichelsonDataGrammarTypeConverter
    private lateinit var tagToMichelsonInstructionGrammarTypeConverter: TagToMichelsonInstructionGrammarTypeConverter
    private lateinit var tagToMichelsonTypeGrammarTypeConverter: TagToMichelsonTypeGrammarTypeConverter
    private lateinit var tagToMichelsonComparableTypeGrammarTypeConverter: TagToMichelsonComparableTypeGrammarTypeConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        tagToMichelsonGrammarTypeConverter = TagToMichelsonGrammarTypeConverter()
        tagToMichelsonDataGrammarTypeConverter = TagToMichelsonDataGrammarTypeConverter()
        tagToMichelsonInstructionGrammarTypeConverter = TagToMichelsonInstructionGrammarTypeConverter()
        tagToMichelsonTypeGrammarTypeConverter = TagToMichelsonTypeGrammarTypeConverter()
        tagToMichelsonComparableTypeGrammarTypeConverter = TagToMichelsonComparableTypeGrammarTypeConverter()

        every { dependencyRegistry.tagToMichelsonGrammarTypeConverter } returns tagToMichelsonGrammarTypeConverter
        every { dependencyRegistry.tagToMichelsonDataGrammarTypeConverter } returns tagToMichelsonDataGrammarTypeConverter
        every { dependencyRegistry.tagToMichelsonInstructionGrammarTypeConverter } returns tagToMichelsonInstructionGrammarTypeConverter
        every { dependencyRegistry.tagToMichelsonTypeGrammarTypeConverter } returns tagToMichelsonTypeGrammarTypeConverter
        every { dependencyRegistry.tagToMichelsonComparableTypeGrammarTypeConverter } returns tagToMichelsonComparableTypeGrammarTypeConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert tag to Michelson GrammarType`() {
        val valuesWithExpected = grammarTypes.map {
            when (it) {
                MichelsonComparableType.Option -> it.tag to MichelsonType.Option
                MichelsonComparableType.Pair -> it.tag to MichelsonType.Pair
                MichelsonComparableType.Or -> it.tag to MichelsonType.Or
                else -> it.tag to it
            }
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToMichelsonGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, Michelson.GrammarType.fromTagOrNull(it.first))
            assertEquals(it.second, Michelson.GrammarType.fromTagOrNull(it.first, tagToMichelsonGrammarTypeConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonData GrammarType`() {
        val valuesWithExpected = dataGrammarTypes.map { it.tag to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToMichelsonDataGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonData.GrammarType.fromTagOrNull(it.first))
            assertEquals(it.second, MichelsonData.GrammarType.fromTagOrNull(it.first, tagToMichelsonDataGrammarTypeConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonInstruction GrammarType`() {
        val valuesWithExpected = instructionGrammarTypes.map { it.tag to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToMichelsonInstructionGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonInstruction.GrammarType.fromTagOrNull(it.first))
            assertEquals(it.second, MichelsonInstruction.GrammarType.fromTagOrNull(it.first, tagToMichelsonInstructionGrammarTypeConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonType GrammarType`() {
        val valuesWithExpected = typeGrammarTypes.map {
            when (it) {
                MichelsonComparableType.Option -> it.tag to MichelsonType.Option
                MichelsonComparableType.Pair -> it.tag to MichelsonType.Pair
                MichelsonComparableType.Or -> it.tag to MichelsonType.Or
                else -> it.tag to it
            }
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToMichelsonTypeGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonType.GrammarType.fromTagOrNull(it.first))
            assertEquals(it.second, MichelsonType.GrammarType.fromTagOrNull(it.first, tagToMichelsonTypeGrammarTypeConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonComparableType GrammarType`() {
        val valuesWithExpected = comparableTypeGrammarTypes.map { it.tag to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToMichelsonComparableTypeGrammarTypeConverter.convert(it.first))
            assertEquals(it.second, MichelsonComparableType.GrammarType.fromTagOrNull(it.first))
            assertEquals(it.second, MichelsonComparableType.GrammarType.fromTagOrNull(it.first, tagToMichelsonComparableTypeGrammarTypeConverter))
        }
    }

    @Test
    fun `should fail to convert unknown string to Michelson GrammarType`() {
        val unknownTags = listOf(-1)

        unknownTags.forEach {
            val message = "Unknown Michelson grammar type: \"$it\"."

            assertFailsWith<IllegalArgumentException>(message) {
                tagToMichelsonGrammarTypeConverter.convert(it)
            }
            assertNull(Michelson.GrammarType.fromTagOrNull(it))
            assertNull(Michelson.GrammarType.fromTagOrNull(it, tagToMichelsonGrammarTypeConverter))

            assertFailsWith<IllegalArgumentException>(message) {
                tagToMichelsonDataGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonData.GrammarType.fromTagOrNull(it))
            assertNull(MichelsonData.GrammarType.fromTagOrNull(it, tagToMichelsonDataGrammarTypeConverter))

            assertFailsWith<IllegalArgumentException>(message) {
                tagToMichelsonInstructionGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonInstruction.GrammarType.fromTagOrNull(it))
            assertNull(MichelsonInstruction.GrammarType.fromTagOrNull(it, tagToMichelsonInstructionGrammarTypeConverter))

            assertFailsWith<IllegalArgumentException>(message) {
                tagToMichelsonTypeGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonType.GrammarType.fromTagOrNull(it))
            assertNull(MichelsonType.GrammarType.fromTagOrNull(it, tagToMichelsonTypeGrammarTypeConverter))

            assertFailsWith<IllegalArgumentException>(message) {
                tagToMichelsonComparableTypeGrammarTypeConverter.convert(it)
            }
            assertNull(MichelsonComparableType.GrammarType.fromTagOrNull(it))
            assertNull(MichelsonComparableType.GrammarType.fromTagOrNull(it, tagToMichelsonComparableTypeGrammarTypeConverter))
        }
    }
}