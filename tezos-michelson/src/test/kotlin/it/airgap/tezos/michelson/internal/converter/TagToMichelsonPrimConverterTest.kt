package it.airgap.tezos.michelson.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.converter.fromTagOrNull
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TagToMichelsonPrimConverterTest {

    private lateinit var tezos: Tezos

    private lateinit var tagToMichelsonPrimConverter: TagToMichelsonPrimConverter
    private lateinit var tagToMichelsonDataPrimConverter: TagToMichelsonDataPrimConverter
    private lateinit var tagToMichelsonInstructionPrimConverter: TagToMichelsonInstructionPrimConverter
    private lateinit var tagToMichelsonTypePrimConverter: TagToMichelsonTypePrimConverter
    private lateinit var tagToMichelsonComparableTypePrimConverter: TagToMichelsonComparableTypePrimConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()

        tagToMichelsonPrimConverter = TagToMichelsonPrimConverter()
        tagToMichelsonDataPrimConverter = TagToMichelsonDataPrimConverter()
        tagToMichelsonInstructionPrimConverter = TagToMichelsonInstructionPrimConverter()
        tagToMichelsonTypePrimConverter = TagToMichelsonTypePrimConverter()
        tagToMichelsonComparableTypePrimConverter = TagToMichelsonComparableTypePrimConverter()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert tag to Michelson Prim`() {
        val valuesWithExpected = Michelson.Prim.values.map {
            when (it) {
                MichelsonComparableType.Option -> it.tag to MichelsonType.Option
                MichelsonComparableType.Pair -> it.tag to MichelsonType.Pair
                MichelsonComparableType.Or -> it.tag to MichelsonType.Or
                else -> it.tag to it
            }
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToMichelsonPrimConverter.convert(it.first))
            assertEquals(it.second, Michelson.Prim.fromTagOrNull(it.first, tezos))
            assertEquals(it.second, Michelson.Prim.fromTagOrNull(it.first, tagToMichelsonPrimConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonData Prim`() {
        val valuesWithExpected = MichelsonData.Prim.values.map { it.tag to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToMichelsonDataPrimConverter.convert(it.first))
            assertEquals(it.second, MichelsonData.Prim.fromTagOrNull(it.first, tezos))
            assertEquals(it.second, MichelsonData.Prim.fromTagOrNull(it.first, tagToMichelsonDataPrimConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonInstruction Prim`() {
        val valuesWithExpected = MichelsonInstruction.Prim.values.map { it.tag to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToMichelsonInstructionPrimConverter.convert(it.first))
            assertEquals(it.second, MichelsonInstruction.Prim.fromTagOrNull(it.first, tezos))
            assertEquals(it.second, MichelsonInstruction.Prim.fromTagOrNull(it.first, tagToMichelsonInstructionPrimConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonType Prim`() {
        val valuesWithExpected = MichelsonType.Prim.values.map {
            when (it) {
                MichelsonComparableType.Option -> it.tag to MichelsonType.Option
                MichelsonComparableType.Pair -> it.tag to MichelsonType.Pair
                MichelsonComparableType.Or -> it.tag to MichelsonType.Or
                else -> it.tag to it
            }
        }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToMichelsonTypePrimConverter.convert(it.first))
            assertEquals(it.second, MichelsonType.Prim.fromTagOrNull(it.first, tezos))
            assertEquals(it.second, MichelsonType.Prim.fromTagOrNull(it.first, tagToMichelsonTypePrimConverter))
        }
    }

    @Test
    fun `should convert name to MichelsonComparableType Prim`() {
        val valuesWithExpected = MichelsonComparableType.Prim.values.map { it.tag to it }

        valuesWithExpected.forEach {
            assertEquals(it.second, tagToMichelsonComparableTypePrimConverter.convert(it.first))
            assertEquals(it.second, MichelsonComparableType.Prim.fromTagOrNull(it.first, tezos))
            assertEquals(it.second, MichelsonComparableType.Prim.fromTagOrNull(it.first, tagToMichelsonComparableTypePrimConverter))
        }
    }

    @Test
    fun `should fail to convert unknown string to Michelson Prim`() {
        val unknownTags = listOf(byteArrayOf( -1))

        unknownTags.forEach {
            assertFailsWith<IllegalArgumentException> { tagToMichelsonPrimConverter.convert(it) }
            assertNull(Michelson.Prim.fromTagOrNull(it, tezos))
            assertNull(Michelson.Prim.fromTagOrNull(it, tagToMichelsonPrimConverter))

            assertFailsWith<IllegalArgumentException> { tagToMichelsonDataPrimConverter.convert(it) }
            assertNull(MichelsonData.Prim.fromTagOrNull(it, tezos))
            assertNull(MichelsonData.Prim.fromTagOrNull(it, tagToMichelsonDataPrimConverter))

            assertFailsWith<IllegalArgumentException> { tagToMichelsonInstructionPrimConverter.convert(it) }
            assertNull(MichelsonInstruction.Prim.fromTagOrNull(it, tezos))
            assertNull(MichelsonInstruction.Prim.fromTagOrNull(it, tagToMichelsonInstructionPrimConverter))

            assertFailsWith<IllegalArgumentException> { tagToMichelsonTypePrimConverter.convert(it) }
            assertNull(MichelsonType.Prim.fromTagOrNull(it, tezos))
            assertNull(MichelsonType.Prim.fromTagOrNull(it, tagToMichelsonTypePrimConverter))

            assertFailsWith<IllegalArgumentException> { tagToMichelsonComparableTypePrimConverter.convert(it) }
            assertNull(MichelsonComparableType.Prim.fromTagOrNull(it, tezos))
            assertNull(MichelsonComparableType.Prim.fromTagOrNull(it, tagToMichelsonComparableTypePrimConverter))
        }
    }
}