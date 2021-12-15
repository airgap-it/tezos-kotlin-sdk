package it.airgap.tezos.michelson.internal.converter

import it.airgap.tezos.michelson.toMicheline
import michelsonComparableTypeMichelinePairs
import michelsonDataMichelinePairs
import michelsonInstructionMichelinePairs
import michelsonTypeMichelinePairs
import org.junit.Test
import kotlin.test.assertEquals

class MichelsonToMichelineConverterTest {

    @Test
    fun `should convert Michelson Data to Micheline`() {
        val expectedWithMichelson = michelsonDataMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, MichelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, MichelsonDataToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline())
        }
    }

    @Test
    fun `should convert Michelson Instruction to Micheline`() {
        val expectedWithMichelson = michelsonInstructionMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, MichelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, MichelsonInstructionToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline())
        }
    }

    @Test
    fun `should convert Michelson Type to Micheline`() {
        val expectedWithMichelson = michelsonTypeMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, MichelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, MichelsonTypeToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline())
        }
    }

    @Test
    fun `should convert Michelson Comparable Type to Micheline`() {
        val expectedWithMichelson = michelsonComparableTypeMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, MichelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, MichelsonComparableTypeToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline())
        }
    }
}