package it.airgap.tezos.michelson.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.di.ScopedDependencyRegistry
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class StringToMichelsonPrimConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var stringToMichelsonPrimConverter: StringToMichelsonPrimConverter
    private lateinit var stringToMichelsonDataPrimConverter: StringToMichelsonDataPrimConverter
    private lateinit var stringToMichelsonInstructionPrimConverter: StringToMichelsonInstructionPrimConverter
    private lateinit var stringToMichelsonTypePrimConverter: StringToMichelsonTypePrimConverter
    private lateinit var stringToMichelsonComparableTypePrimConverter: StringToMichelsonComparableTypePrimConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        stringToMichelsonPrimConverter = StringToMichelsonPrimConverter()
        stringToMichelsonDataPrimConverter = StringToMichelsonDataPrimConverter()
        stringToMichelsonInstructionPrimConverter = StringToMichelsonInstructionPrimConverter()
        stringToMichelsonTypePrimConverter = StringToMichelsonTypePrimConverter()
        stringToMichelsonComparableTypePrimConverter = StringToMichelsonComparableTypePrimConverter()

        every { dependencyRegistry.stringToMichelsonPrimConverter } returns stringToMichelsonPrimConverter
        every { dependencyRegistry.stringToMichelsonDataPrimConverter } returns stringToMichelsonDataPrimConverter
        every { dependencyRegistry.stringToMichelsonInstructionPrimConverter } returns stringToMichelsonInstructionPrimConverter
        every { dependencyRegistry.stringToMichelsonTypePrimConverter } returns stringToMichelsonTypePrimConverter
        every { dependencyRegistry.stringToMichelsonComparableTypePrimConverter } returns stringToMichelsonComparableTypePrimConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert name to Michelson Prim`() {
        val valuesWithExpected = Michelson.Prim.values.map {
            when (it) {
                MichelsonComparableType.Option -> it.name to MichelsonType.Option
                MichelsonComparableType.Pair -> it.name to MichelsonType.Pair
                MichelsonComparableType.Or -> it.name to MichelsonType.Or
                else -> it.name to it
            }
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, stringToMichelsonPrimConverter.convert(it.first))
            assertEquals(it.second, Michelson.Prim.fromStringOrNull(it.first))
            assertEquals(it.second, Michelson.Prim.fromStringOrNull(it.first, stringToMichelsonPrimConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonData Prim`() {
        val valuesWithExpected = MichelsonData.Prim.values.map { it.name to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, stringToMichelsonDataPrimConverter.convert(it.first))
            assertEquals(it.second, MichelsonData.Prim.fromStringOrNull(it.first))
            assertEquals(it.second, MichelsonData.Prim.fromStringOrNull(it.first, stringToMichelsonDataPrimConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonInstruction Prim`() {
        val valuesWithExpected = MichelsonInstruction.Prim.values.map { it.name to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, stringToMichelsonInstructionPrimConverter.convert(it.first))
            assertEquals(it.second, MichelsonInstruction.Prim.fromStringOrNull(it.first))
            assertEquals(it.second, MichelsonInstruction.Prim.fromStringOrNull(it.first, stringToMichelsonInstructionPrimConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonType Prim`() {
        val valuesWithExpected = MichelsonType.Prim.values.map {
            when (it) {
                MichelsonComparableType.Option -> it.name to MichelsonType.Option
                MichelsonComparableType.Pair -> it.name to MichelsonType.Pair
                MichelsonComparableType.Or -> it.name to MichelsonType.Or
                else -> it.name to it
            }
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, stringToMichelsonTypePrimConverter.convert(it.first))
            assertEquals(it.second, MichelsonType.Prim.fromStringOrNull(it.first))
            assertEquals(it.second, MichelsonType.Prim.fromStringOrNull(it.first, stringToMichelsonTypePrimConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonComparableType Prim`() {
        val valuesWithExpected = MichelsonComparableType.Prim.values.map { it.name to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, stringToMichelsonComparableTypePrimConverter.convert(it.first))
            assertEquals(it.second, MichelsonComparableType.Prim.fromStringOrNull(it.first))
            assertEquals(it.second, MichelsonComparableType.Prim.fromStringOrNull(it.first, stringToMichelsonComparableTypePrimConverter))
        }
    }

    @Test
    fun `should fail to convert unknown string to Michelson Prim`() {
        val unknownStrings = listOf("unknown")

        unknownStrings.forEach {
            assertFailsWith<IllegalArgumentException> { stringToMichelsonPrimConverter.convert(it) }
            assertNull(Michelson.Prim.fromStringOrNull(it))
            assertNull(Michelson.Prim.fromStringOrNull(it, stringToMichelsonPrimConverter))

            assertFailsWith<IllegalArgumentException> { stringToMichelsonDataPrimConverter.convert(it) }
            assertNull(MichelsonData.Prim.fromStringOrNull(it))
            assertNull(MichelsonData.Prim.fromStringOrNull(it, stringToMichelsonDataPrimConverter))

            assertFailsWith<IllegalArgumentException> { stringToMichelsonInstructionPrimConverter.convert(it) }
            assertNull(MichelsonInstruction.Prim.fromStringOrNull(it))
            assertNull(MichelsonInstruction.Prim.fromStringOrNull(it, stringToMichelsonInstructionPrimConverter))

            assertFailsWith<IllegalArgumentException> { stringToMichelsonTypePrimConverter.convert(it) }
            assertNull(MichelsonType.Prim.fromStringOrNull(it))
            assertNull(MichelsonType.Prim.fromStringOrNull(it, stringToMichelsonTypePrimConverter))

            assertFailsWith<IllegalArgumentException> { stringToMichelsonComparableTypePrimConverter.convert(it) }
            assertNull(MichelsonComparableType.Prim.fromStringOrNull(it))
            assertNull(MichelsonComparableType.Prim.fromStringOrNull(it, stringToMichelsonComparableTypePrimConverter))
        }
    }
}