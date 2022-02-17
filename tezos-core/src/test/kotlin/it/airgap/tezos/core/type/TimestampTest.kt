package it.airgap.tezos.core.type

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimestampTest {

    @Test
    fun `recognizes valid and invalid Rfc3339 date strings`() {
        validDateStrings.forEach {
            assertTrue(Timestamp.Rfc3339.isValid(it))
        }

        invalidDateStrings.forEach {
            assertFalse(Timestamp.Rfc3339.isValid(it))
        }
    }

    @Test
    fun `creates Rfc3339 from valid string`() {
        validDateStrings.forEach {
            assertEquals(Timestamp.Rfc3339(it), Timestamp.fromString(it))
        }
    }

    @Test
    fun `creates Millis from long values`() {
        val validDateStrings = listOf(
            1642675437103,
            1542673467203,
            1344326572000,
            1052374267231,
            153243274393,
            -1932565152613,
        )

        validDateStrings.forEach {
            assertEquals(Timestamp.Millis(it), Timestamp.fromMillis(it))
        }
    }

    @Test
    fun `returns null when creating Rfc3339 from invalid string and asked to`() {
        val timestamps = invalidDateStrings.mapNotNull(Timestamp.Companion::fromStringOrNull)

        assertEquals(0, timestamps.size)
    }

    @Test
    fun `fails when creating Rfc3339 form invalid string`() {
        invalidDateStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Timestamp.fromString(it) }
            assertFailsWith<IllegalArgumentException> { Timestamp.Rfc3339(it) }
        }
    }

    @Test
    fun `converts to Rfc3339`() {
        val millisWithRfc3339 = listOf(
            Timestamp.Rfc3339("2022-01-20T10:43:57.103Z") to Timestamp.Rfc3339("2022-01-20T10:43:57.103Z"),
            Timestamp.Rfc3339("2018-11-20T00:24:27.203Z") to Timestamp.Rfc3339("2018-11-20T00:24:27.203Z"),
            Timestamp.Rfc3339("2012-08-07T08:02:52Z") to Timestamp.Rfc3339("2012-08-07T08:02:52Z"),
            Timestamp.Rfc3339("2003-05-08T06:11:07.231Z") to Timestamp.Rfc3339("2003-05-08T06:11:07.231Z"),
            Timestamp.Rfc3339("1974-11-09T15:34:34.393Z") to Timestamp.Rfc3339("1974-11-09T15:34:34.393Z"),
            Timestamp.Rfc3339("1908-10-05T08:20:47.387Z") to Timestamp.Rfc3339("1908-10-05T08:20:47.387Z"),
            Timestamp.Millis(1642675437103) to Timestamp.Rfc3339("2022-01-20T10:43:57.103Z"),
            Timestamp.Millis(1542673467203) to Timestamp.Rfc3339("2018-11-20T00:24:27.203Z"),
            Timestamp.Millis(1344326572000) to Timestamp.Rfc3339("2012-08-07T08:02:52Z"),
            Timestamp.Millis(1052374267231) to Timestamp.Rfc3339("2003-05-08T06:11:07.231Z"),
            Timestamp.Millis(153243274393) to Timestamp.Rfc3339("1974-11-09T15:34:34.393Z"),
            Timestamp.Millis(-1932565152613) to Timestamp.Rfc3339("1908-10-05T08:20:47.387Z"),
        )

        millisWithRfc3339.forEach {
            assertEquals(it.second, it.first.toRfc3339())
        }
    }

    @Test
    fun `converts to Millis`() {
        val millisWithRfc3339 = listOf(
            Timestamp.Rfc3339("2022-01-20T10:43:57.103Z") to Timestamp.Millis(1642675437103),
            Timestamp.Rfc3339("2018-11-20T00:24:27.203Z") to Timestamp.Millis(1542673467203),
            Timestamp.Rfc3339("2012-08-07T08:02:52Z") to Timestamp.Millis(1344326572000),
            Timestamp.Rfc3339("2003-05-08T06:11:07.231Z") to Timestamp.Millis(1052374267231),
            Timestamp.Rfc3339("1974-11-09T15:34:34.393Z") to Timestamp.Millis(153243274393),
            Timestamp.Rfc3339("1908-10-05T08:20:47.387Z") to Timestamp.Millis(-1932565152613),
            Timestamp.Millis(1642675437103) to Timestamp.Millis(1642675437103),
            Timestamp.Millis(1542673467203) to Timestamp.Millis(1542673467203),
            Timestamp.Millis(1344326572000) to Timestamp.Millis(1344326572000),
            Timestamp.Millis(1052374267231) to Timestamp.Millis(1052374267231),
            Timestamp.Millis(153243274393) to Timestamp.Millis(153243274393),
            Timestamp.Millis(-1932565152613) to Timestamp.Millis(-1932565152613),
        )

        millisWithRfc3339.forEach {
            assertEquals(it.second, it.first.toMillis())
        }
    }

    private val validDateStrings: List<String>
        get() = listOf(
            "2022-01-20T10:43:57.103Z",
            "2018-11-20T00:24:27.203Z",
            "2012-08-07T08:02:52Z",
            "2003-05-08T06:11:07.231Z",
            "1974-11-09T15:34:34.393Z",
            "1908-10-05T08:20:47.387Z",
        )

    private val invalidDateStrings: List<String>
        get() = listOf(
            "",
            "invalidTimestamp",
            "2018-11-20",
            "2012-08-07T08:02",
            "06:11:07.231Z",
            "1974-11-0915:34:34.393",
        )
}