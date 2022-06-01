package it.airgap.tezos.core.internal.coder.number

import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.type.number.TezosNatural
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TezosNaturalBytesCoderTest {

    private lateinit var tezos: Tezos
    private lateinit var tezosNaturalBytesCoder: TezosNaturalBytesCoder

    @Before
    fun setup() {
        tezos = mockTezos()
        tezosNaturalBytesCoder = TezosNaturalBytesCoder()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode TezosNatural to [ByteArray]`() = withTezosContext {
        naturalsWithBytes.forEach {
            assertContentEquals(it.second, tezosNaturalBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.encodeToBytes(tezosNaturalBytesCoder))
        }
    }

    @Test
    fun `should decode TezosNatural from bytes`() = withTezosContext {
        naturalsWithBytes.forEach {
            assertEquals(it.first, tezosNaturalBytesCoder.decode(it.second))
            assertEquals(it.first, tezosNaturalBytesCoder.decodeConsuming(it.second.toMutableList()))
            assertEquals(it.first, TezosNatural.decodeFromBytes(it.second, tezosNaturalBytesCoder))
            assertEquals(it.first, TezosNatural.decodeConsumingFromBytes(it.second.toMutableList(), tezosNaturalBytesCoder))
        }
    }

    @Test
    fun `should fail to decode Tezos natural number from invalid bytes`() = withTezosContext {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { tezosNaturalBytesCoder.decode(it) }
            assertFailsWith<IllegalArgumentException> { TezosNatural.decodeFromBytes(it, tezosNaturalBytesCoder) }
            assertFailsWith<IllegalArgumentException> { tezosNaturalBytesCoder.decodeConsuming(it.toMutableList()) }
            assertFailsWith<IllegalArgumentException> { TezosNatural.decodeConsumingFromBytes(it.toMutableList(), tezosNaturalBytesCoder) }
        }
    }

    private val naturalsWithBytes: List<Pair<TezosNatural, ByteArray>>
        get() = listOf(
            TezosNatural(0U) to "00".asHexString().toByteArray(),
            TezosNatural(1U) to "01".asHexString().toByteArray(),
            TezosNatural(10U) to "0a".asHexString().toByteArray(),
            TezosNatural(42U) to "2a".asHexString().toByteArray(),
            TezosNatural(64U) to "40".asHexString().toByteArray(),
            TezosNatural(127U) to "7f".asHexString().toByteArray(),
            TezosNatural(128U) to "8001".asHexString().toByteArray(),
            TezosNatural(18756523543673U) to "f998b1bff1a104".asHexString().toByteArray(),
            TezosNatural(6852352674543413768U) to "88d4ee8ebcce9c8c5f".asHexString().toByteArray(),
            TezosNatural("54576326575686358562454576456764") to "bcc8a9a1f3d19ca2e0dbfdf9999bac01".asHexString().toByteArray(),
            TezosNatural("41547452475632687683489977342365486797893454355756867843") to "83c2f7e7a3ade1bac2cccad7d5cf93e2c58792e0ec9aa5c8c6e306".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
        )
}