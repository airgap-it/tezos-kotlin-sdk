package it.airgap.tezos.rpc.internal.converter.tez

import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.rpc.internal.type.Nanotez
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
            Mutez(0) to Nanotez(0),
            Mutez(1) to Nanotez(1_000),
            Mutez(1_000) to Nanotez(1_000_000),
            Mutez(674_235_765) to Nanotez(674_235_765_000),
            Mutez(3_214_543_786_232_456) to Nanotez(3_214_543_786_232_456_000),
        )
}