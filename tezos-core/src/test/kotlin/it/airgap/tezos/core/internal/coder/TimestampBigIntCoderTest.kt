package it.airgap.tezos.core.internal.coder

import io.mockk.unmockkAll
import it.airgap.tezos.core.internal.type.BigInt
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TimestampBigIntCoderTest {

    private lateinit var timestampBigIntCoder: TimestampBigIntCoder

    @Before
    fun setup() {
        timestampBigIntCoder = TimestampBigIntCoder()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode timestamp to BigInt`() {
        timestampWithBigInts.forEach {
            assertEquals(it.second, timestampBigIntCoder.encode(it.first))
        }
    }

    @Test
    fun `should fail to encode invalid timestamp to BigInt`() {
        invalidTimestamps.forEach {
            assertFailsWith<IllegalArgumentException> {
                timestampBigIntCoder.encode(it)
            }
        }
    }

    @Test
    fun `should decode timestamp from BigInt`() {
        timestampWithBigInts.forEach {
            assertEquals(it.first, timestampBigIntCoder.decode(it.second))
        }
    }

    @Test
    fun `should fail to decode timestamp from invalid BigInt`() {
        invalidBigInts.forEach {
            assertFailsWith<IllegalArgumentException> {
                timestampBigIntCoder.decode(it)
            }
        }
    }

    private val timestampWithBigInts: List<Pair<String, BigInt>>
        get() = listOf(
            "2022-01-20T10:43:57.103Z" to BigInt.valueOf(1642675437103),
            "2018-11-20T00:24:27.203Z" to BigInt.valueOf(1542673467203),
            "2012-08-07T08:02:52Z" to BigInt.valueOf(1344326572000),
            "2003-05-08T06:11:07.231Z" to BigInt.valueOf(1052374267231),
            "1974-11-09T15:34:34.393Z" to BigInt.valueOf(153243274393),
            "1908-10-05T08:20:47.387Z" to BigInt.valueOf(-1932565152613),
        )

    private val invalidTimestamps: List<String>
        get() = listOf(
            "",
            "invalidTimestamp",
            "2022-01-20T10:43:57.103",
            "2018-11-20",
            "2012-08-07T08:02",
            "06:11:07.231Z",
            "1974-11-0915:34:34.393",
            "1908-10-05T08:20:47.387",
        )

    private val invalidBigInts: List<BigInt>
        get() = listOf(
             BigInt.valueOf(Long.MAX_VALUE) + 1,
             BigInt.valueOf(Long.MIN_VALUE) - 1,
        )
}