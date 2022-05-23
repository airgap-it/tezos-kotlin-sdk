package it.airgap.tezos.core.internal.coder.tez

import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.tez.decodeConsumingFromBytes
import it.airgap.tezos.core.coder.tez.decodeFromBytes
import it.airgap.tezos.core.coder.tez.encodeToBytes
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.type.tez.Nanotez
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NanotezBytesCoderTest {

    private lateinit var tezos: Tezos
    private lateinit var nanotezBytesCoder: NanotezBytesCoder

    @Before
    fun setup() {
        tezos = mockTezos()
        nanotezBytesCoder = NanotezBytesCoder(tezos.coreModule.dependencyRegistry.tezosNaturalBytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode Nanotez to bytes`() {
        integersWithBytes.forEach {
            assertContentEquals(it.second, nanotezBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.encodeToBytes(tezos))
            assertContentEquals(it.second, it.first.encodeToBytes(nanotezBytesCoder))
        }
    }

    @Test
    fun `should decode Nanotez from bytes`() {
        integersWithBytes.forEach {
            assertEquals(it.first, nanotezBytesCoder.decode(it.second))
            assertEquals(it.first, nanotezBytesCoder.decodeConsuming(it.second.toMutableList()))
            assertEquals(it.first, Nanotez.decodeFromBytes(it.second, tezos))
            assertEquals(it.first, Nanotez.decodeFromBytes(it.second, nanotezBytesCoder))
            assertEquals(it.first, Nanotez.decodeConsumingFromBytes(it.second.toMutableList(), nanotezBytesCoder))
        }
    }

    @Test
    fun `should fail to decode Nanotez from invalid bytes`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { nanotezBytesCoder.decode(it) }
            assertFailsWith<IllegalArgumentException> { Nanotez.decodeFromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { Nanotez.decodeFromBytes(it, nanotezBytesCoder) }
            assertFailsWith<IllegalArgumentException> { nanotezBytesCoder.decodeConsuming(it.toMutableList()) }
            assertFailsWith<IllegalArgumentException> { Nanotez.decodeConsumingFromBytes(it.toMutableList(), nanotezBytesCoder) }
        }
    }

    private val integersWithBytes: List<Pair<Nanotez, ByteArray>>
        get() = listOf(
            Nanotez(0U) to "00".asHexString().toByteArray(),
            Nanotez(1U) to "01".asHexString().toByteArray(),
            Nanotez(10U) to "0a".asHexString().toByteArray(),
            Nanotez(42U) to "2a".asHexString().toByteArray(),
            Nanotez(64U) to "40".asHexString().toByteArray(),
            Nanotez(127U) to "7f".asHexString().toByteArray(),
            Nanotez(128U) to "8001".asHexString().toByteArray(),
            Nanotez(18756523543673U) to "f998b1bff1a104".asHexString().toByteArray(),
            Nanotez(6852352674543413768U) to "88d4ee8ebcce9c8c5f".asHexString().toByteArray(),
            Nanotez("54576326575686358562454576456764") to "bcc8a9a1f3d19ca2e0dbfdf9999bac01".asHexString().toByteArray(),
            Nanotez("41547452475632687683489977342365486797893454355756867843") to "83c2f7e7a3ade1bac2cccad7d5cf93e2c58792e0ec9aa5c8c6e306".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
        )
}