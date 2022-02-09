package it.airgap.tezos.core.type.zarith

import inRange
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ZarithIntegerTest {

    @Test
    fun `recognizes valid and invalid ZarithInteger`() {
        val valid = listOf(
            "-9223372036854775809",
            "-9223372036854775808",
            "-2147483648",
            "-32768",
            "-128",
            "-1",
            "0",
            "1",
            "127",
            "32767",
            "2147483647",
            "9223372036854775807",
            "9223372036854775808",
        )

        val invalid = listOf(
            "",
            "abc",
            "1.",
            "1.0",
            " 10",
            " -10",
            "- 10",
            "10%",
        )

        valid.forEach {
            assertTrue(ZarithInteger.isValid(it), "Expected `$it` to be recognized as valid ZarithInteger.")
        }

        invalid.forEach {
            assertFalse(ZarithInteger.isValid(it), "Expected `$it` to be recognized as invalid ZarithInteger.")
        }
    }

    @Test
    fun `creates ZarithInteger from valid string`() {
        val valid = listOf(
            "-9223372036854775809",
            "-9223372036854775808",
            "-2147483648",
            "-32768",
            "-128",
            "-1",
            "0",
            "1",
            "127",
            "32767",
            "2147483647",
            "9223372036854775807",
            "9223372036854775808",
        )

        valid.forEach {
            assertEquals(it, ZarithInteger(it).int)
        }
    }

    @Test
    fun `creates ZarithInteger from Number`() {
        val valid = listOf(
            "-9223372036854775809",
            "-9223372036854775808",
            "-2147483648",
            "-32768",
            "-128",
            "-1",
            "0",
            "1",
            "127",
            "32767",
            "2147483647",
            "9223372036854775807",
            "9223372036854775808",
        )

        valid.forEach {
            if (it.inRange(Byte.MIN_VALUE..Byte.MAX_VALUE)) {
                assertEquals(it, ZarithInteger(it.toByte()).int)
            }

            if (it.inRange(Short.MIN_VALUE..Short.MAX_VALUE)) {
                assertEquals(it, ZarithInteger(it.toShort()).int)
            }

            if (it.inRange(Int.MIN_VALUE..Int.MAX_VALUE)) {
                assertEquals(it, ZarithInteger(it.toInt()).int)
            }

            if (it.inRange(Long.MIN_VALUE..Long.MAX_VALUE)) {
                assertEquals(it, ZarithInteger(it.toLong()).int)
            }
        }
    }

    @Test
    fun `fails to create ZarithInteger from invalid string`() {
        val invalid = listOf(
            "",
            "abc",
            "1.",
            "1.0",
            " 10",
            " -10",
            "- 10",
            "10%",
        )

        invalid.forEach {
            assertFailsWith<IllegalArgumentException> { ZarithInteger(it) }
        }
    }

    @Test
    fun `converts ZarithInteger to specified Number type or fails if out of range`() {
        val numbers = listOf(
            "-9223372036854775809",
            "-9223372036854775808",
            "-2147483648",
            "-32768",
            "-128",
            "-1",
            "0",
            "1",
            "127",
            "32767",
            "2147483647",
            "9223372036854775807",
            "9223372036854775808",
        )

        numbers.forEach {
            val integer = ZarithInteger(it)

            if (it.inRange(Byte.MIN_VALUE..Byte.MAX_VALUE)) {
                assertEquals(it.toByte(), integer.toByte())
            } else {
                assertFailsWith<NumberFormatException> { integer.toByte() }
            }

            if (it.inRange(Short.MIN_VALUE..Short.MAX_VALUE)) {
                assertEquals(it.toShort(), integer.toShort())
            } else {
                assertFailsWith<NumberFormatException> { integer.toShort() }
            }

            if (it.inRange(Int.MIN_VALUE..Int.MAX_VALUE)) {
                assertEquals(it.toInt(), integer.toInt())
            } else {
                assertFailsWith<NumberFormatException> { integer.toInt() }
            }

            if (it.inRange(Long.MIN_VALUE..Long.MAX_VALUE)) {
                assertEquals(it.toLong(), integer.toLong())
            } else {
                assertFailsWith<NumberFormatException> { integer.toLong() }
            }
        }
    }
}