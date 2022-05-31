package it.airgap.tezos.core.internal.coder.number

import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.number.decodeFromBytes
import it.airgap.tezos.core.coder.number.encodeToBytes
import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.number.TezosInteger
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TezosIntegerBytesCoderTest {

    private lateinit var tezos: Tezos
    private lateinit var tezosIntegerBytesCoder: TezosIntegerBytesCoder

    @Before
    fun setup() {
        tezos = mockTezos()
        tezosIntegerBytesCoder = TezosIntegerBytesCoder(tezos.coreModule.dependencyRegistry.tezosNaturalBytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode TezosInteger to bytes`() = withTezosContext {
        integersWithBytes.forEach {
            assertContentEquals(it.second, tezosIntegerBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.encodeToBytes(tezos))
            assertContentEquals(it.second, it.first.encodeToBytes(tezosIntegerBytesCoder))
        }
    }

    @Test
    fun `should decode TezosInteger from bytes`() = withTezosContext {
        integersWithBytes.forEach {
            assertEquals(it.first, tezosIntegerBytesCoder.decode(it.second))
            assertEquals(it.first, tezosIntegerBytesCoder.decodeConsuming(it.second.toMutableList()))
            assertEquals(it.first, TezosInteger.decodeFromBytes(it.second, tezos))
            assertEquals(it.first, TezosInteger.decodeFromBytes(it.second, tezosIntegerBytesCoder))
            assertEquals(it.first, TezosInteger.decodeConsumingFromBytes(it.second.toMutableList(), tezosIntegerBytesCoder))
        }
    }

    @Test
    fun `should fail to decode Tezos integer from invalid bytes`() = withTezosContext {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { tezosIntegerBytesCoder.decode(it) }
            assertFailsWith<IllegalArgumentException> { TezosInteger.decodeFromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { TezosInteger.decodeFromBytes(it, tezosIntegerBytesCoder) }
            assertFailsWith<IllegalArgumentException> { tezosIntegerBytesCoder.decodeConsuming(it.toMutableList()) }
            assertFailsWith<IllegalArgumentException> { TezosInteger.decodeConsumingFromBytes(it.toMutableList(), tezosIntegerBytesCoder) }
        }
    }

    private val integersWithBytes: List<Pair<TezosInteger, ByteArray>>
        get() = listOf(
            TezosInteger("-41547452475632687683489977342365486797893454355756867843") to "c384efcfc7dac2f5849995afab9fa7c48b8fa4c0d9b5ca908dc70d".asHexString().toByteArray(),
            TezosInteger("-54576326575686358562454576456764") to "fc90d3c2e6a3b9c4c0b7fbf3b3b6d802".asHexString().toByteArray(),
            TezosInteger(-6852352674543413768) to "c8a8dd9df89cb998be01".asHexString().toByteArray(),
            TezosInteger(-18756523543673) to "f9b1e2fee2c308".asHexString().toByteArray(),
            TezosInteger(-128) to "c002".asHexString().toByteArray(),
            TezosInteger(-127) to "ff01".asHexString().toByteArray(),
            TezosInteger(-64) to "c001".asHexString().toByteArray(),
            TezosInteger(-42) to "6a".asHexString().toByteArray(),
            TezosInteger(-10) to "4a".asHexString().toByteArray(),
            TezosInteger(-1) to "41".asHexString().toByteArray(),
            TezosInteger(0) to "00".asHexString().toByteArray(),
            TezosInteger(1) to "01".asHexString().toByteArray(),
            TezosInteger(10) to "0a".asHexString().toByteArray(),
            TezosInteger(42) to "2a".asHexString().toByteArray(),
            TezosInteger(64) to "8001".asHexString().toByteArray(),
            TezosInteger(127) to "bf01".asHexString().toByteArray(),
            TezosInteger(128) to "8002".asHexString().toByteArray(),
            TezosInteger(18756523543673) to "b9b1e2fee2c308".asHexString().toByteArray(),
            TezosInteger(6852352674543413768) to "88a8dd9df89cb998be01".asHexString().toByteArray(),
            TezosInteger("54576326575686358562454576456764") to "bc90d3c2e6a3b9c4c0b7fbf3b3b6d802".asHexString().toByteArray(),
            TezosInteger("41547452475632687683489977342365486797893454355756867843") to "8384efcfc7dac2f5849995afab9fa7c48b8fa4c0d9b5ca908dc70d".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
        )
}