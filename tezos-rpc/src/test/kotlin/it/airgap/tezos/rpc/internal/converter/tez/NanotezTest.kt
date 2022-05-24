package it.airgap.tezos.rpc.internal.converter.tez

import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.rpc.internal.type.Nanotez
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
            Nanotez(0) to Mutez(0),
            Nanotez(1) to Mutez(1),
            Nanotez(999) to Mutez(1),
            Nanotez(1_000) to Mutez(1),
            Nanotez(1_001) to Mutez(2),
            Nanotez(999_999) to Mutez(1_000),
            Nanotez(1_000_000) to Mutez(1_000),
            Nanotez(1_000_010) to Mutez(1_001),
            Nanotez(674_235_764_632) to Mutez(674_235_765),
            Nanotez(674_235_765_000) to Mutez(674_235_765),
            Nanotez(674_235_765_457) to Mutez(674_235_766),
            Nanotez(3_214_543_786_232_455_600) to Mutez(3_214_543_786_232_456),
            Nanotez(3_214_543_786_232_456_000) to Mutez(3_214_543_786_232_456),
            Nanotez(3_214_543_786_232_456_103) to Mutez(3_214_543_786_232_457),
        )
}