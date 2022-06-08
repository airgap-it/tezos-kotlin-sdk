package it.airgap.tezos.michelson.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.michelson.converter.toMicheline
import it.airgap.tezos.michelson.internal.context.withTezosContext
import michelsonComparableTypeMichelinePairs
import michelsonDataMichelinePairs
import michelsonInstructionMichelinePairs
import michelsonTypeMichelinePairs
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelsonToMichelineConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var michelsonToMichelineConverter: MichelsonToMichelineConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        michelsonToMichelineConverter = MichelsonToMichelineConverter()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert Michelson Data to Micheline`() = withTezosContext {
        val expectedWithMichelson = michelsonDataMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, michelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline(tezos))
            assertEquals(it.first, it.second.toMicheline(michelsonToMichelineConverter))
        }
    }

    @Test
    fun `should convert Michelson Instruction to Micheline`() = withTezosContext {
        val expectedWithMichelson = michelsonInstructionMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, michelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline(tezos))
            assertEquals(it.first, it.second.toMicheline(michelsonToMichelineConverter))
        }
    }

    @Test
    fun `should convert Michelson Type to Micheline`() = withTezosContext {
        val expectedWithMichelson = michelsonTypeMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, michelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline(tezos))
            assertEquals(it.first, it.second.toMicheline(michelsonToMichelineConverter))
        }
    }

    @Test
    fun `should convert Michelson Comparable Type to Micheline`() = withTezosContext {
        val expectedWithMichelson = michelsonComparableTypeMichelinePairs.map { it.second to it.first }

        expectedWithMichelson.forEach {
            assertEquals(it.first, michelsonToMichelineConverter.convert(it.second))
            assertEquals(it.first, it.second.toMicheline(tezos))
            assertEquals(it.first, it.second.toMicheline(michelsonToMichelineConverter))
        }
    }
}