package it.airgap.tezos.core.internal.coder

import io.mockk.unmockkAll
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.Timestamp
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
    fun `should encode Timestamp to BigInt`() {
        val timestampsWithBigInts = listOf(
            Timestamp.Rfc3339("2022-01-20T10:43:57.103Z") to BigInt.valueOf(1642675437103),
            Timestamp.Rfc3339("2018-11-20T00:24:27.203Z") to BigInt.valueOf(1542673467203),
            Timestamp.Rfc3339("2012-08-07T08:02:52Z") to BigInt.valueOf(1344326572000),
            Timestamp.Rfc3339("2003-05-08T06:11:07.231Z") to BigInt.valueOf(1052374267231),
            Timestamp.Rfc3339("1974-11-09T15:34:34.393Z") to BigInt.valueOf(153243274393),
            Timestamp.Rfc3339("1908-10-05T08:20:47.387Z") to BigInt.valueOf(-1932565152613),
            Timestamp.Millis(1642675437103) to BigInt.valueOf(1642675437103),
            Timestamp.Millis(1542673467203) to BigInt.valueOf(1542673467203),
            Timestamp.Millis(1344326572000) to BigInt.valueOf(1344326572000),
            Timestamp.Millis(1052374267231) to BigInt.valueOf(1052374267231),
            Timestamp.Millis(153243274393) to BigInt.valueOf(153243274393),
            Timestamp.Millis(-1932565152613) to BigInt.valueOf(-1932565152613),
        )

        timestampsWithBigInts.forEach {
            assertEquals(it.second, timestampBigIntCoder.encode(it.first))
        }
    }

    @Test
    fun `should decode Timestamp from BigInt`() {
        val bigIntsWithTimestamps = listOf(
            BigInt.valueOf(1642675437103) to Timestamp.Rfc3339("2022-01-20T10:43:57.103Z"),
            BigInt.valueOf(1542673467203) to Timestamp.Rfc3339("2018-11-20T00:24:27.203Z"),
            BigInt.valueOf(1344326572000) to Timestamp.Rfc3339("2012-08-07T08:02:52Z"),
            BigInt.valueOf(1052374267231) to Timestamp.Rfc3339("2003-05-08T06:11:07.231Z"),
            BigInt.valueOf(153243274393) to Timestamp.Rfc3339("1974-11-09T15:34:34.393Z"),
            BigInt.valueOf(-1932565152613) to Timestamp.Rfc3339("1908-10-05T08:20:47.387Z"),
        )

        bigIntsWithTimestamps.forEach {
            assertEquals(it.second, timestampBigIntCoder.decode(it.first))
        }
    }

    @Test
    fun `should fail to decode Timestamp from invalid BigInt`() {
        invalidBigInts.forEach {
            assertFailsWith<IllegalArgumentException> { timestampBigIntCoder.decode(it) }
        }
    }

    private val invalidBigInts: List<BigInt>
        get() = listOf(
             BigInt.valueOf(Long.MAX_VALUE) + 1,
             BigInt.valueOf(Long.MIN_VALUE) - 1,
        )
}