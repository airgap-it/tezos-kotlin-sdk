package it.airgap.tezos.core.converter.tez

import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import org.junit.Test
import kotlin.test.assertEquals

class NanotezTest {

    @Test
    fun `should convert Nanotez to Mutez`() {
        nanotezWithMutez.forEach {
            assertEquals(it.second, it.first.toMutez())
        }
    }

    private val nanotezWithMutez: List<Pair<Nanotez, Mutez>>
        get() = listOf(
            Nanotez(0U) to Mutez(0U),
            Nanotez(1U) to Mutez(1U),
            Nanotez(999U) to Mutez(1U),
            Nanotez(1_000U) to Mutez(1U),
            Nanotez(1_001U) to Mutez(2U),
            Nanotez(999_999U) to Mutez(1_000U),
            Nanotez(1_000_000U) to Mutez(1_000U),
            Nanotez(1_000_010U) to Mutez(1_001U),
            Nanotez(674_235_764_632U) to Mutez(674_235_765U),
            Nanotez(674_235_765_000U) to Mutez(674_235_765U),
            Nanotez(674_235_765_457U) to Mutez(674_235_766U),
            Nanotez(3_214_543_786_232_455_600U) to Mutez(3_214_543_786_232_456U),
            Nanotez(3_214_543_786_232_456_000U) to Mutez(3_214_543_786_232_456U),
            Nanotez(3_214_543_786_232_456_103U) to Mutez(3_214_543_786_232_457U),
        )
}