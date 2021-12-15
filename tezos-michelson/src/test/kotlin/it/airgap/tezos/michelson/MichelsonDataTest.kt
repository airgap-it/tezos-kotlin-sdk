package it.airgap.tezos.michelson

import inRange
import it.airgap.tezos.core.internal.type.HexString
import it.airgap.tezos.core.internal.utils.asHexString
import org.junit.Test
import kotlin.test.*

class MichelsonDataTest {
    @Test
    fun `recognizes valid and invalid IntConstants`() {
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
            assertTrue(MichelsonData.IntConstant.isValid(it), "Expected `$it` to be recognized as valid Michelson IntConstant.")
        }

        invalid.forEach {
            assertFalse(MichelsonData.IntConstant.isValid(it), "Expected `$it` to be recognized as invalid Michelson IntConstant.")
        }
    }

    @Test
    fun `creates IntConstant from valid string`() {
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
            assertEquals(it, MichelsonData.IntConstant(it).value)
        }
    }

    @Test
    fun `creates IntConstant from Number`() {
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
                assertEquals(it, MichelsonData.IntConstant(it.toByte()).value)
            }

            if (it.inRange(Short.MIN_VALUE..Short.MAX_VALUE)) {
                assertEquals(it, MichelsonData.IntConstant(it.toShort()).value)
            }

            if (it.inRange(Int.MIN_VALUE..Int.MAX_VALUE)) {
                assertEquals(it, MichelsonData.IntConstant(it.toInt()).value)
            }

            if (it.inRange(Long.MIN_VALUE..Long.MAX_VALUE)) {
                assertEquals(it, MichelsonData.IntConstant(it.toLong()).value)
            }
        }
    }

    @Test
    fun `fails to create IntConstant from invalid string`() {
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
            assertFailsWith<IllegalArgumentException> {
                MichelsonData.IntConstant(it)
            }
        }
    }

    @Test
    fun `converts IntConstant to specified Number type or fails if out of range`() {
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
            val integer = MichelsonData.IntConstant(it)

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
    fun `recognizes valid and invalid NaturalNumberConstant`() {
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
            "-9223372036854775809",
            "-9223372036854775808",
            "-2147483648",
            "-32768",
            "-128",
            "-1",
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
            assertTrue(MichelsonData.NaturalNumberConstant.isValid(it), "Expected `$it` to be recognized as valid Michelson IntConstant.")
        }

        invalid.forEach {
            assertFalse(MichelsonData.NaturalNumberConstant.isValid(it), "Expected `$it` to be recognized as invalid Michelson IntConstant.")
        }
    }

    @Test
    fun `creates NaturalNumberConstant from valid string`() {
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
            assertEquals(it, MichelsonData.NaturalNumberConstant(it).value)
        }
    }

    @Test
    fun `creates NaturalNumberConstant from Number`() {
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
            if (it.inRange(UByte.MIN_VALUE..UByte.MAX_VALUE)) {
                assertEquals(it, MichelsonData.NaturalNumberConstant(it.toUByte()).value)
            }

            if (it.inRange(UShort.MIN_VALUE..UShort.MAX_VALUE)) {
                assertEquals(it, MichelsonData.NaturalNumberConstant(it.toUShort()).value)
            }

            if (it.inRange(UInt.MIN_VALUE..UInt.MAX_VALUE)) {
                assertEquals(it, MichelsonData.NaturalNumberConstant(it.toUInt()).value)
            }

            if (it.inRange(ULong.MIN_VALUE..ULong.MAX_VALUE)) {
                assertEquals(it, MichelsonData.NaturalNumberConstant(it.toULong()).value)
            }
        }
    }

    @Test
    fun `fails to create NaturalNumberConstant from invalid string`() {
        val invalid = listOf(
            "-9223372036854775809",
            "-9223372036854775808",
            "-2147483648",
            "-32768",
            "-128",
            "-1",
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
            assertFailsWith<IllegalArgumentException> {
                MichelsonData.NaturalNumberConstant(it)
            }
        }
    }

    @Test
    fun `converts NaturalNumberConstant to specified Number type or fails if out of range`() {
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
            val naturalNumber = MichelsonData.NaturalNumberConstant(it)

            if (it.inRange(UByte.MIN_VALUE..UByte.MAX_VALUE)) {
                assertEquals(it.toUByte(), naturalNumber.toUByte())
            } else {
                assertFailsWith<NumberFormatException> { naturalNumber.toUByte() }
            }

            if (it.inRange(UShort.MIN_VALUE..UShort.MAX_VALUE)) {
                println("$it, ${Short.MIN_VALUE}, ${Short.MAX_VALUE}")
                assertEquals(it.toUShort(), naturalNumber.toUShort())
            } else {
                assertFailsWith<NumberFormatException> { naturalNumber.toUShort() }
            }

            if (it.inRange(UInt.MIN_VALUE..UInt.MAX_VALUE)) {
                assertEquals(it.toUInt(), naturalNumber.toUInt())
            } else {
                assertFailsWith<NumberFormatException> { naturalNumber.toUInt() }
            }

            if (it.inRange(ULong.MIN_VALUE..ULong.MAX_VALUE)) {
                assertEquals(it.toULong(), naturalNumber.toULong())
            } else {
                assertFailsWith<NumberFormatException> { naturalNumber.toULong() }
            }
        }
    }

    @Test
    fun `recognizes valid and invalid StringConstants`() {
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
            assertTrue(MichelsonData.StringConstant.isValid(it), "Expected `$it` to be recognized as valid Michelson StringConstant.")
        }

        invalid.forEach {
            assertFalse(MichelsonData.StringConstant.isValid(it), "Expected `$it` to be recognized as invalid Michelson StringConstant.")
        }
    }

    @Test
    fun `creates StringConstant from valid string`() {
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
            assertEquals(it, MichelsonData.StringConstant(it).value)
        }
    }

    @Test
    fun `fails to create StringConstant from invalid string`() {
        val invalid = listOf<String>()

        invalid.forEach {
            assertFailsWith<IllegalArgumentException> {
                MichelsonData.StringConstant(it)
            }
        }
    }

    @Test
    fun `recognizes valid and invalid ByteSequenceConstants`() {
        val valid = listOf(
            "0x00",
            "0x32ffb703ac"
        )

        val invalid = listOf(
            "",
            "0x",
            "00",
            "0x0",
            "abc",
        )

        valid.forEach {
            assertTrue(MichelsonData.ByteSequenceConstant.isValid(it), "Expected `$it` to be recognized as valid Michelson ByteSequenceConstant.")
        }

        invalid.forEach {
            assertFalse(MichelsonData.ByteSequenceConstant.isValid(it), "Expected `$it` to be recognized as invalid Michelson ByteSequenceConstant.")
        }
    }

    @Test
    fun `creates ByteSequenceConstant from valid string`() {
        val valid = listOf(
            "0x00",
            "0x32ffb703ac"
        )

        valid.forEach {
            assertEquals(it, MichelsonData.ByteSequenceConstant(it).value)
        }
    }

    @Test
    fun `creates ByteSequenceConstant from ByteArray`() {
        val valid = listOf(
            "0x00",
            "0x32ffb703ac"
        )

        valid.forEach {
            assertEquals(it, MichelsonData.ByteSequenceConstant(it.asHexString().toByteArray()).value)
        }
    }

    @Test
    fun `fails to create ByteSequenceConstant from invalid string`() {
        val invalid = listOf(
            "",
            "0x",
            "00",
            "0x0",
            "abc",
        )

        invalid.forEach {
            assertFailsWith<IllegalArgumentException> {
                MichelsonData.ByteSequenceConstant(it)
            }
        }
    }

    @Test
    fun `converts ByteSequenceConstant to ByteArray`() {
        val valid = listOf(
            "0x00",
            "0x32ffb703ac"
        )

        valid.forEach {
            assertContentEquals(HexString(it).toByteArray(), MichelsonData.ByteSequenceConstant(it).toByteArray())
        }
    }
}