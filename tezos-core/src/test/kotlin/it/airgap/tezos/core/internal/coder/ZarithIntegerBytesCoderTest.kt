package it.airgap.tezos.core.internal.coder

import io.mockk.unmockkAll
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.asHexString
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
        val zarithNaturalNumberBytesCoder = ZarithNaturalNumberBytesCoder()

        zarithIntegerBytesCoder = ZarithIntegerBytesCoder(zarithNaturalNumberBytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode Zarith integer to bytes`() {
        integersWithBytes.forEach {
            assertContentEquals(it.second, zarithIntegerBytesCoder.encode(it.first))
        }
    }

    @Test
    fun `should decode Zarith integer from bytes`() {
        integersWithBytes.forEach {
            assertEquals(it.first, zarithIntegerBytesCoder.decode(it.second))
            assertEquals(it.first, zarithIntegerBytesCoder.decodeConsuming(it.second.toMutableList()))
        }
    }

    @Test
    fun `should fail to decode Zarith integer from invalid bytes`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> {
                zarithIntegerBytesCoder.decode(it)
            }

            assertFailsWith<IllegalArgumentException> {
                zarithIntegerBytesCoder.decodeConsuming(it.toMutableList())
            }
        }
    }

    private val integersWithBytes: List<Pair<BigInt, ByteArray>>
        get() = listOf(
            BigInt.valueOf("-41547452475632687683489977342365486797893454355756867843") to "c384efcfc7dac2f5849995afab9fa7c48b8fa4c0d9b5ca908dc70d".asHexString().toByteArray(),
            BigInt.valueOf("-54576326575686358562454576456764") to "fc90d3c2e6a3b9c4c0b7fbf3b3b6d802".asHexString().toByteArray(),
            BigInt.valueOf(-6852352674543413768) to "c8a8dd9df89cb998be01".asHexString().toByteArray(),
            BigInt.valueOf(-18756523543673) to "f9b1e2fee2c308".asHexString().toByteArray(),
            BigInt.valueOf(-128) to "c002".asHexString().toByteArray(),
            BigInt.valueOf(-127) to "ff01".asHexString().toByteArray(),
            BigInt.valueOf(-64) to "c001".asHexString().toByteArray(),
            BigInt.valueOf(-42) to "6a".asHexString().toByteArray(),
            BigInt.valueOf(-10) to "4a".asHexString().toByteArray(),
            BigInt.valueOf(-1) to "41".asHexString().toByteArray(),
            BigInt.valueOf(0) to "00".asHexString().toByteArray(),
            BigInt.valueOf(1) to "01".asHexString().toByteArray(),
            BigInt.valueOf(10) to "0a".asHexString().toByteArray(),
            BigInt.valueOf(42) to "2a".asHexString().toByteArray(),
            BigInt.valueOf(64) to "8001".asHexString().toByteArray(),
            BigInt.valueOf(127) to "bf01".asHexString().toByteArray(),
            BigInt.valueOf(128) to "8002".asHexString().toByteArray(),
            BigInt.valueOf(18756523543673) to "b9b1e2fee2c308".asHexString().toByteArray(),
            BigInt.valueOf(6852352674543413768) to "88a8dd9df89cb998be01".asHexString().toByteArray(),
            BigInt.valueOf("54576326575686358562454576456764") to "bc90d3c2e6a3b9c4c0b7fbf3b3b6d802".asHexString().toByteArray(),
            BigInt.valueOf("41547452475632687683489977342365486797893454355756867843") to "8384efcfc7dac2f5849995afab9fa7c48b8fa4c0d9b5ca908dc70d".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
        )
}