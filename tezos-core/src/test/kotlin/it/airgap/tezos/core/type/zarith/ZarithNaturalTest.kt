package it.airgap.tezos.core.type.zarith

import inRange
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ZarithNaturalTest {

    @Test
    fun `recognizes valid and invalid ZarithNatural`() {
        val valid = listOf(
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
            "-9223372036854775809",
            "-9223372036854775808",
            "-2147483648",
            "-32768",
            "-128",
            "-1",
        )

        valid.forEach {
            assertTrue(ZarithNatural.isValid(it), "Expected `$it` to be recognized as valid ZarithNatural.")
        }

        invalid.forEach {
            assertFalse(ZarithNatural.isValid(it), "Expected `$it` to be recognized as invalid ZarithNatural.")
        }
    }

    @Test
    fun `creates ZarithNatural from valid string`() {
        val valid = listOf(
            "0",
            "1",
            "127",
            "32767",
            "2147483647",
            "9223372036854775807",
            "9223372036854775808",
        )

        valid.forEach {
            assertEquals(it, ZarithNatural(it).int)
        }
    }

    @Test
    fun `creates ZarithNatural from Number`() {
        val valid = listOf(
            "0",
            "1",
            "127",
            "32767",
            "2147483647",
            "9223372036854775807",
            "9223372036854775808",
        )

        valid.forEach {
            if (it.inRange(UByte.MIN_VALUE..UByte.MAX_VALUE)) {
                assertEquals(it, ZarithNatural(it.toUByte()).int)
            }

            if (it.inRange(UShort.MIN_VALUE..UShort.MAX_VALUE)) {
                assertEquals(it, ZarithNatural(it.toUShort()).int)
            }

            if (it.inRange(UInt.MIN_VALUE..UInt.MAX_VALUE)) {
                assertEquals(it, ZarithNatural(it.toUInt()).int)
            }

            if (it.inRange(ULong.MIN_VALUE..ULong.MAX_VALUE)) {
                assertEquals(it, ZarithNatural(it.toULong()).int)
            }
        }
    }

    @Test
    fun `fails to create ZarithNatural from invalid string`() {
        val invalid = listOf(
            "",
            "abc",
            "1.",
            "1.0",
            " 10",
            " -10",
            "- 10",
            "10%",
            "-9223372036854775809",
            "-9223372036854775808",
            "-2147483648",
            "-32768",
            "-128",
            "-1",
        )

        invalid.forEach {
            assertFailsWith<IllegalArgumentException> { ZarithNatural(it) }
        }
    }

    @Test
    fun `converts ZarithNatural to specified Number type or fails if out of range`() {
        val numbers = listOf(
            "0",
            "1",
            "127",
            "32767",
            "2147483647",
            "9223372036854775807",
            "9223372036854775808",
        )

        numbers.forEach {
            val natural = ZarithNatural(it)

            if (it.inRange(UByte.MIN_VALUE..UByte.MAX_VALUE)) {
                assertEquals(it.toUByte(), natural.toUByte())
            } else {
                assertFailsWith<NumberFormatException> { natural.toUByte() }
            }

            if (it.inRange(UShort.MIN_VALUE..UShort.MAX_VALUE)) {
                assertEquals(it.toUShort(), natural.toUShort())
            } else {
                assertFailsWith<NumberFormatException> { natural.toUShort() }
            }

            if (it.inRange(UInt.MIN_VALUE..UInt.MAX_VALUE)) {
                assertEquals(it.toUInt(), natural.toUInt())
            } else {
                assertFailsWith<NumberFormatException> { natural.toUInt() }
            }

            if (it.inRange(ULong.MIN_VALUE..ULong.MAX_VALUE)) {
                assertEquals(it.toULong(), natural.toULong())
            } else {
                assertFailsWith<NumberFormatException> { natural.toULong() }
            }
        }
    }
}