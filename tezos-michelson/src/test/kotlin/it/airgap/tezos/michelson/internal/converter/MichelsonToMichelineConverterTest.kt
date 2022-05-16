package it.airgap.tezos.michelson.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.toMicheline
import michelsonComparableTypeMichelinePairs
import michelsonDataMichelinePairs
import michelsonInstructionMichelinePairs
import michelsonTypeMichelinePairs
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelsonToMichelineConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var michelsonToMichelineConverter: MichelsonToMichelineConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        michelsonToMichelineConverter = MichelsonToMichelineConverter()

        every { dependencyRegistry.michelsonToMichelineConverter } returns michelsonToMichelineConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert Michelson Data to Micheline`() {
        val expectedWithMichelson = michelsonDataMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, michelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline())
            assertEquals(it.first, it.second.toMicheline(michelsonToMichelineConverter))
        }
    }

    @Test
    fun `should convert Michelson Instruction to Micheline`() {
        val expectedWithMichelson = michelsonInstructionMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, michelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline())
            assertEquals(it.first, it.second.toMicheline(michelsonToMichelineConverter))
        }
    }

    @Test
    fun `should convert Michelson Type to Micheline`() {
        val expectedWithMichelson = michelsonTypeMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, michelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline())
            assertEquals(it.first, it.second.toMicheline(michelsonToMichelineConverter))
        }
    }

    @Test
    fun `should convert Michelson Comparable Type to Micheline`() {
        val expectedWithMichelson = michelsonComparableTypeMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, michelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline())
            assertEquals(it.first, it.second.toMicheline(michelsonToMichelineConverter))
        }
    }
}