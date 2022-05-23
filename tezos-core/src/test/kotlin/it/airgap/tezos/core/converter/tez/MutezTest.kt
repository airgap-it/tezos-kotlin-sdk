package it.airgap.tezos.core.converter.tez

import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import org.junit.Test
import kotlin.test.assertEquals

class MutezTest {

    @Test
    fun `should convert Mutez to Nanotez`() {
        mutezWithNanotez.forEach {
            assertEquals(it.second, it.first.toNanotez())
        }
    }

    private val mutezWithNanotez: List<Pair<Mutez, Nanotez>>
        get() = listOf(
            Mutez(0U) to Nanotez(0U),
            Mutez(1U) to Nanotez(1_000U),
            Mutez(1_000U) to Nanotez(1_000_000U),
            Mutez(674_235_765U) to Nanotez(674_235_765_000U),
            Mutez(3_214_543_786_232_456U) to Nanotez(3_214_543_786_232_456_000U),
        )
}