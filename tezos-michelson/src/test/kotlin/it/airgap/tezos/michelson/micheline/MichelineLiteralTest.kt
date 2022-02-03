package it.airgap.tezos.michelson.micheline

import inRange
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.type.HexString
import org.junit.Test
import kotlin.test.*

class MichelineLiteralTest {

    @Test
    fun `recognizes valid and invalid Integers`() {
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
            assertTrue(MichelineLiteral.Integer.isValid(it), "Expected `$it` to be recognized as valid Micheline Integer.")
        }

        invalid.forEach {
            assertFalse(MichelineLiteral.Integer.isValid(it), "Expected `$it` to be recognized as invalid Micheline Integer.")
        }
    }

    @Test
    fun `creates Integer from valid string`() {
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
            assertEquals(it, MichelineLiteral.Integer(it).int)
        }
    }

    @Test
    fun `creates Integer from Number`() {
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
                assertEquals(it, MichelineLiteral.Integer(it.toByte()).int)
            }

            if (it.inRange(Short.MIN_VALUE..Short.MAX_VALUE)) {
                assertEquals(it, MichelineLiteral.Integer(it.toShort()).int)
            }

            if (it.inRange(Int.MIN_VALUE..Int.MAX_VALUE)) {
                assertEquals(it, MichelineLiteral.Integer(it.toInt()).int)
            }

            if (it.inRange(Long.MIN_VALUE..Long.MAX_VALUE)) {
                assertEquals(it, MichelineLiteral.Integer(it.toLong()).int)
            }
        }
    }

    @Test
    fun `fails to create Integer from invalid string`() {
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
            assertFailsWith<IllegalArgumentException> { MichelineLiteral.Integer(it) }
        }
    }

    @Test
    fun `converts Integer to specified Number type or fails if out of range`() {
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
            val integer = MichelineLiteral.Integer(it)

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

    @Test
    fun `recognizes valid and invalid Strings`() {
        val valid = listOf(
            "",
            "abc",
            "!abc",
            "@abc",
            "#abc",
            "\$abc",
            "%abc",
            "^abc",
            "&abc",
            "*abc",
            "(abc)",
            "[abc]",
            "{ abc }",
        )

        val invalid = listOf<String>()

        valid.forEach {
            assertTrue(MichelineLiteral.String.isValid(it), "Expected `$it` to be recognized as valid Micheline String.")
        }

        invalid.forEach {
            assertFalse(MichelineLiteral.String.isValid(it), "Expected `$it` to be recognized as invalid Micheline String.")
        }
    }

    @Test
    fun `creates String from valid string`() {
        val valid = listOf(
            "",
            "abc",
            "!abc",
            "@abc",
            "#abc",
            "\$abc",
            "%abc",
            "^abc",
            "&abc",
            "*abc",
            "(abc)",
            "[abc]",
            "{ abc }",
        )

        valid.forEach {
            assertEquals(it, MichelineLiteral.String(it).string)
        }
    }

    @Test
    fun `fails to create String from invalid string`() {
        val invalid = listOf<String>()

        invalid.forEach {
            assertFailsWith<IllegalArgumentException> { MichelineLiteral.String(it) }
        }
    }

    @Test
    fun `recognizes valid and invalid Bytes`() {
        val valid = listOf(
            "0x",
            "0x00",
            "0x32ffb703ac"
        )

        val invalid = listOf(
            "",
            "00",
            "0x0",
            "abc",
        )

        valid.forEach {
            assertTrue(MichelineLiteral.Bytes.isValid(it), "Expected `$it` to be recognized as valid Micheline Bytes.")
        }

        invalid.forEach {
            assertFalse(MichelineLiteral.Bytes.isValid(it), "Expected `$it` to be recognized as invalid Micheline Bytes.")
        }
    }

    @Test
    fun `creates Bytes from valid string`() {
        val valid = listOf(
            "0x",
            "0x00",
            "0x32ffb703ac"
        )

        valid.forEach {
            assertEquals(it, MichelineLiteral.Bytes(it).bytes)
        }
    }

    @Test
    fun `creates Bytes from ByteArray`() {
        val valid = listOf(
            "0x00",
            "0x32ffb703ac"
        )

        valid.forEach {
            assertEquals(it, MichelineLiteral.Bytes(it.asHexString().toByteArray()).bytes)
        }
    }

    @Test
    fun `fails to create Bytes from invalid string`() {
        val invalid = listOf(
            "",
            "00",
            "0x0",
            "abc",
        )

        invalid.forEach {
            assertFailsWith<IllegalArgumentException> { MichelineLiteral.Bytes(it) }
        }
    }

    @Test
    fun `converts Bytes to ByteArray`() {
        val valid = listOf(
            "0x",
            "0x00",
            "0x32ffb703ac"
        )

        valid.forEach {
            assertContentEquals(if (it == "0x") byteArrayOf() else HexString(it).toByteArray(), MichelineLiteral.Bytes(it).toByteArray())
        }
    }
}