package it.airgap.tezos.core.type

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexStringOrNull
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HexStringTest {
    private val validHexStrings: List<String> = listOf(
        "",
        "0x",
        "9434dc98",
        "0x7b1ea2cb",
        "e40476d7",
        "c47320abdd31",
        "0x5786dac9eaf4",
    )

    private val invalidHexStrings: List<String> = listOf(
        "9434dc98az",
        "0xe40476d77t",
        "0x1",
    )

    @Test
    fun `creates HexString from valid string`() {
        val hexStrings = validHexStrings.map { it.asHexString() }

        assertEquals(validHexStrings.map(this::withoutHexPrefix),
            hexStrings.map(HexString::asString))
    }

    @Test
    fun `returns null when creating HexString from invalid string and asked to`() {
        val hexStrings = invalidHexStrings.mapNotNull { it.asHexStringOrNull() }

        assertEquals(0, hexStrings.size)
    }

    @Test
    fun `fails when creating HexString form invalid string`() {
        invalidHexStrings.forEach {
            assertFailsWith<IllegalArgumentException> { it.asHexString() }
        }
    }

    @Test
    fun `returns HexString length with and without prefix`() {
        val string = validHexStrings.first()
        val hexString = string.asHexString()

        assertEquals(withHexPrefix(string).length, hexString.length(withPrefix = true))
        assertEquals(withoutHexPrefix(string).length, hexString.length(withPrefix = false))
    }

    @Test
    fun `returns HexString value with and without prefix`() {
        val string = validHexStrings.first()
        val hexString = string.asHexString()

        assertEquals(withHexPrefix(string), hexString.asString(withPrefix = true))
        assertEquals(withoutHexPrefix(string), hexString.asString())
    }

    @Test
    fun `creates byte array from HexString`() {
        val hexStringsWithExpected: List<Pair<String, ByteArray>> = listOf(
            "9434dc98" to byteArrayOf(-108, 52, -36, -104),
            "0x7b1ea2cb" to byteArrayOf(123, 30, -94, -53),
            "e40476d7" to byteArrayOf(-28, 4, 118, -41),
            "c47320abdd31" to byteArrayOf(-60, 115, 32, -85, -35, 49),
            "0x5786dac9eaf4" to byteArrayOf(87, -122, -38, -55, -22, -12),
        )

        hexStringsWithExpected
            .map { it.first.asHexString().toByteArray() to it.second }
            .forEach {
                assertContentEquals(it.second, it.first)
            }
    }

    @Test
    fun `creates Byte from HexString`() {
        val hexStringsWithExpected: List<Pair<String, Byte>> = listOf(
            "80" to -128,
            "ff" to -1,
            "00" to 0,
            "7f" to 127,
        )

        hexStringsWithExpected
            .map { it.first.asHexString().toByte() to it.second }
            .forEach {
                assertEquals(it.second, it.first)
            }
    }

    @Test
    fun `creates Short from HexString`() {
        val hexStringsWithExpected: List<Pair<String, Short>> = listOf(
            "8000" to -32768,
            "ff80" to -128,
            "ffff" to -1,
            "00" to 0,
            "7f" to 127,
            "7fff" to 32767,
        )

        hexStringsWithExpected
            .map { it.first.asHexString().toShort() to it.second }
            .forEach {
                assertEquals(it.second, it.first)
            }
    }

    @Test
    fun `creates Int from HexString`() {
        val hexStringsWithExpected: List<Pair<String, Int>> = listOf(
            "80000000" to -2147483648,
            "ffff8000" to -32768,
            "ffffff80" to -128,
            "ffffffff" to -1,
            "00" to 0,
            "7f" to 127,
            "7fff" to 32767,
            "7fffffff" to 2147483647,
        )

        hexStringsWithExpected
            .map { it.first.asHexString().toInt() to it.second }
            .forEach {
                assertEquals(it.second, it.first)
            }
    }

    @Test
    fun `creates Long from HexString`() {
        val hexStringsWithExpected: List<Pair<String, Long>> = listOf(
            "8000000000000000" to -9223372036854775807 - 1,
            "ffffffff80000000" to -2147483648,
            "ffffffffffff8000" to -32768,
            "ffffffffffffff80" to -128,
            "ffffffffffffffff" to -1,
            "00" to 0,
            "7f" to 127,
            "7fff" to 32767,
            "7fffffff" to 2147483647,
            "7fffffffffffffff" to 9223372036854775807,
        )

        hexStringsWithExpected
            .map { it.first.asHexString().toLong() to it.second }
            .forEach {
                assertEquals(it.second, it.first)
            }
    }

    private fun withHexPrefix(string: String): String = if (string.startsWith(HexString.PREFIX)) string else "${HexString.PREFIX}${string}"
    private fun withoutHexPrefix(string: String): String = string.removePrefix(HexString.PREFIX)

}