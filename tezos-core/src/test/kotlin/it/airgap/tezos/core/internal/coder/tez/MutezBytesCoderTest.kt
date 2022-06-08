package it.airgap.tezos.core.internal.coder.tez

import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.tez.Mutez
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MutezBytesCoderTest {

    private lateinit var tezos: Tezos
    private lateinit var mutezBytesCoder: MutezBytesCoder

    @Before
    fun setup() {
        tezos = mockTezos()
        mutezBytesCoder = MutezBytesCoder(tezos.coreModule.dependencyRegistry.tezosNaturalBytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode Mutez to bytes`() = withTezosContext {
        mutezWithBytes.forEach {
            assertContentEquals(it.second, mutezBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.encodeToBytes(mutezBytesCoder))
        }
    }

    @Test
    fun `should decode Mutez from bytes`() = withTezosContext {
        mutezWithBytes.forEach {
            assertEquals(it.first, mutezBytesCoder.decode(it.second))
            assertEquals(it.first, mutezBytesCoder.decodeConsuming(it.second.toMutableList()))
            assertEquals(it.first, Mutez.decodeFromBytes(it.second, mutezBytesCoder))
            assertEquals(it.first, Mutez.decodeConsumingFromBytes(it.second.toMutableList(), mutezBytesCoder))
        }
    }

    @Test
    fun `should fail to decode Mutez from invalid bytes`() = withTezosContext {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { mutezBytesCoder.decode(it) }
            assertFailsWith<IllegalArgumentException> { Mutez.decodeFromBytes(it, mutezBytesCoder) }
            assertFailsWith<IllegalArgumentException> { mutezBytesCoder.decodeConsuming(it.toMutableList()) }
            assertFailsWith<IllegalArgumentException> { Mutez.decodeConsumingFromBytes(it.toMutableList(), mutezBytesCoder) }
        }
    }

    private val mutezWithBytes: List<Pair<Mutez, ByteArray>>
        get() = listOf(
            Mutez(0) to "00".asHexString().toByteArray(),
            Mutez(1) to "01".asHexString().toByteArray(),
            Mutez(10) to "0a".asHexString().toByteArray(),
            Mutez(42) to "2a".asHexString().toByteArray(),
            Mutez(64) to "40".asHexString().toByteArray(),
            Mutez(127) to "7f".asHexString().toByteArray(),
            Mutez(128) to "8001".asHexString().toByteArray(),
            Mutez(18756523543673) to "f998b1bff1a104".asHexString().toByteArray(),
            Mutez(6852352674543413768) to "88d4ee8ebcce9c8c5f".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
        )
}