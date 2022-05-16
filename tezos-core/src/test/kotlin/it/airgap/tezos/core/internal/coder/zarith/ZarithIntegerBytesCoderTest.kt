package it.airgap.tezos.core.internal.coder.zarith

import io.mockk.unmockkAll
import it.airgap.tezos.core.coder.zarith.decodeConsumingFromBytes
import it.airgap.tezos.core.coder.zarith.decodeFromBytes
import it.airgap.tezos.core.coder.zarith.encodeToBytes
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.type.zarith.ZarithInteger
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ZarithIntegerBytesCoderTest {

    private lateinit var zarithIntegerBytesCoder: ZarithIntegerBytesCoder

    @Before
    fun setup() {
        val zarithNaturalBytesCoder = ZarithNaturalBytesCoder()

        zarithIntegerBytesCoder = ZarithIntegerBytesCoder(zarithNaturalBytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode ZarithInteger to bytes`() {
        integersWithBytes.forEach {
            assertContentEquals(it.second, zarithIntegerBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.encodeToBytes(zarithIntegerBytesCoder))
        }
    }

    @Test
    fun `should decode ZarithInteger from bytes`() {
        integersWithBytes.forEach {
            assertEquals(it.first, zarithIntegerBytesCoder.decode(it.second))
            assertEquals(it.first, zarithIntegerBytesCoder.decodeConsuming(it.second.toMutableList()))
            assertEquals(it.first, ZarithInteger.decodeFromBytes(it.second, zarithIntegerBytesCoder))
            assertEquals(it.first, ZarithInteger.decodeConsumingFromBytes(it.second.toMutableList(), zarithIntegerBytesCoder))
        }
    }

    @Test
    fun `should fail to decode Zarith integer from invalid bytes`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { zarithIntegerBytesCoder.decode(it) }
            assertFailsWith<IllegalArgumentException> { ZarithInteger.decodeFromBytes(it, zarithIntegerBytesCoder) }
            assertFailsWith<IllegalArgumentException> { zarithIntegerBytesCoder.decodeConsuming(it.toMutableList()) }
            assertFailsWith<IllegalArgumentException> { ZarithInteger.decodeConsumingFromBytes(it.toMutableList(), zarithIntegerBytesCoder) }
        }
    }

    private val integersWithBytes: List<Pair<ZarithInteger, ByteArray>>
        get() = listOf(
            ZarithInteger("-41547452475632687683489977342365486797893454355756867843") to "c384efcfc7dac2f5849995afab9fa7c48b8fa4c0d9b5ca908dc70d".asHexString().toByteArray(),
            ZarithInteger("-54576326575686358562454576456764") to "fc90d3c2e6a3b9c4c0b7fbf3b3b6d802".asHexString().toByteArray(),
            ZarithInteger(-6852352674543413768) to "c8a8dd9df89cb998be01".asHexString().toByteArray(),
            ZarithInteger(-18756523543673) to "f9b1e2fee2c308".asHexString().toByteArray(),
            ZarithInteger(-128) to "c002".asHexString().toByteArray(),
            ZarithInteger(-127) to "ff01".asHexString().toByteArray(),
            ZarithInteger(-64) to "c001".asHexString().toByteArray(),
            ZarithInteger(-42) to "6a".asHexString().toByteArray(),
            ZarithInteger(-10) to "4a".asHexString().toByteArray(),
            ZarithInteger(-1) to "41".asHexString().toByteArray(),
            ZarithInteger(0) to "00".asHexString().toByteArray(),
            ZarithInteger(1) to "01".asHexString().toByteArray(),
            ZarithInteger(10) to "0a".asHexString().toByteArray(),
            ZarithInteger(42) to "2a".asHexString().toByteArray(),
            ZarithInteger(64) to "8001".asHexString().toByteArray(),
            ZarithInteger(127) to "bf01".asHexString().toByteArray(),
            ZarithInteger(128) to "8002".asHexString().toByteArray(),
            ZarithInteger(18756523543673) to "b9b1e2fee2c308".asHexString().toByteArray(),
            ZarithInteger(6852352674543413768) to "88a8dd9df89cb998be01".asHexString().toByteArray(),
            ZarithInteger("54576326575686358562454576456764") to "bc90d3c2e6a3b9c4c0b7fbf3b3b6d802".asHexString().toByteArray(),
            ZarithInteger("41547452475632687683489977342365486797893454355756867843") to "8384efcfc7dac2f5849995afab9fa7c48b8fa4c0d9b5ca908dc70d".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
        )
}