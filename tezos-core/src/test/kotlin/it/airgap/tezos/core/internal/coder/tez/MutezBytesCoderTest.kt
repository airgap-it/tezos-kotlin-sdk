package it.airgap.tezos.core.internal.coder.tez

import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.tez.decodeConsumingFromBytes
import it.airgap.tezos.core.coder.tez.decodeFromBytes
import it.airgap.tezos.core.coder.tez.encodeToBytes
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.internal.utils.asHexString
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
    fun `should encode Mutez to bytes`() {
        mutezWithBytes.forEach {
            assertContentEquals(it.second, mutezBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.encodeToBytes(tezos))
            assertContentEquals(it.second, it.first.encodeToBytes(mutezBytesCoder))
        }
    }

    @Test
    fun `should decode Mutez from bytes`() {
        mutezWithBytes.forEach {
            assertEquals(it.first, mutezBytesCoder.decode(it.second))
            assertEquals(it.first, mutezBytesCoder.decodeConsuming(it.second.toMutableList()))
            assertEquals(it.first, Mutez.decodeFromBytes(it.second, tezos))
            assertEquals(it.first, Mutez.decodeFromBytes(it.second, mutezBytesCoder))
            assertEquals(it.first, Mutez.decodeConsumingFromBytes(it.second.toMutableList(), mutezBytesCoder))
        }
    }

    @Test
    fun `should fail to decode Mutez from invalid bytes`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { mutezBytesCoder.decode(it) }
            assertFailsWith<IllegalArgumentException> { Mutez.decodeFromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { Mutez.decodeFromBytes(it, mutezBytesCoder) }
            assertFailsWith<IllegalArgumentException> { mutezBytesCoder.decodeConsuming(it.toMutableList()) }
            assertFailsWith<IllegalArgumentException> { Mutez.decodeConsumingFromBytes(it.toMutableList(), mutezBytesCoder) }
        }
    }

    private val mutezWithBytes: List<Pair<Mutez, ByteArray>>
        get() = listOf(
            Mutez(0U) to "00".asHexString().toByteArray(),
            Mutez(1U) to "01".asHexString().toByteArray(),
            Mutez(10U) to "0a".asHexString().toByteArray(),
            Mutez(42U) to "2a".asHexString().toByteArray(),
            Mutez(64U) to "40".asHexString().toByteArray(),
            Mutez(127U) to "7f".asHexString().toByteArray(),
            Mutez(128U) to "8001".asHexString().toByteArray(),
            Mutez(18756523543673U) to "f998b1bff1a104".asHexString().toByteArray(),
            Mutez(6852352674543413768U) to "88d4ee8ebcce9c8c5f".asHexString().toByteArray(),
            Mutez("54576326575686358562454576456764") to "bcc8a9a1f3d19ca2e0dbfdf9999bac01".asHexString().toByteArray(),
            Mutez("41547452475632687683489977342365486797893454355756867843") to "83c2f7e7a3ade1bac2cccad7d5cf93e2c58792e0ec9aa5c8c6e306".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
        )
}