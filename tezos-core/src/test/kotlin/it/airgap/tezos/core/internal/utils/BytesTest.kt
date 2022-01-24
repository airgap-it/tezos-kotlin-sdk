package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.type.BigInt
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BytesTest {
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
    fun `verifies if string is valid hex string`() {
        val allValid = validHexStrings.map(String::isHex).fold(true, Boolean::and)
        val allInvalid = invalidHexStrings.map(String::isHex).fold(false, Boolean::or)

        assertTrue(allValid)
        assertFalse(allInvalid)
    }

    @Test
    fun `parses byte array as hex string`() {
        val bytes: List<ByteArray> = listOf(
            byteArrayOf(1, 2, 3, 4, 5)
        )
        val hexStrings: List<String> = bytes.map { it.toHexString().asString(withPrefix = true) }

        assertEquals(listOf("0x0102030405"), hexStrings)
    }

    @Test
    fun `parses list of bytes as hex string`() {
        val bytes: List<List<Byte>> = listOf(
            listOf(1, 2, 3, 4, 5)
        )
        val hexStrings: List<String> = bytes.map { it.toHexString().asString(withPrefix = true) }

        assertEquals(listOf("0x0102030405"), hexStrings)
    }

    @Test
    fun `creates HexString from Byte`() {
        val bytesWithHexStrings: List<Pair<Byte, String>> = listOf(
            (-128).toByte() to "80",
            (-1).toByte() to "ff",
            (0).toByte() to "00",
            (127).toByte() to "7f",
        )

        bytesWithHexStrings
            .map { it.first.toHexString() to it.second }
            .forEach {
                assertEquals(it.second, it.first.asString(withPrefix = false))
            }
    }

    @Test
    fun `creates HexString from Short`() {
        val shortsWithHexStrings: List<Pair<Short, String>> = listOf(
            (-32768).toShort() to "8000",
            (-128).toShort() to "ff80",
            (-1).toShort() to "ffff",
            (0).toShort() to "00",
            (127).toShort() to "7f",
            (32767).toShort() to "7fff",
        )

        shortsWithHexStrings
            .map { it.first.toHexString() to it.second }
            .forEach {
                assertEquals(it.second, it.first.asString(withPrefix = false))
            }
    }

    @Test
    fun `creates HexString from Int`() {
        val intsWithHexStrings: List<Pair<Int, String>> = listOf(
            -2147483648 to "80000000",
            -32768 to "ffff8000",
            -128 to "ffffff80",
            -1 to "ffffffff",
            0 to "00",
            127 to "7f",
            32767 to "7fff",
            2147483647 to "7fffffff",
        )

        intsWithHexStrings
            .map { it.first.toHexString() to it.second }
            .forEach {
                assertEquals(it.second, it.first.asString(withPrefix = false))
            }
    }

    @Test
    fun `creates HexString from Long`() {
        val longsWithHexStrings: List<Pair<Long, String>> = listOf(
            -9223372036854775807L - 1L to "8000000000000000",
            -2147483648L to "ffffffff80000000",
            -32768L to "ffffffffffff8000",
            -128L to "ffffffffffffff80",
            -1L to "ffffffffffffffff",
            0L to "00",
            127L to "7f",
            32767L to "7fff",
            2147483647L to "7fffffff",
            9223372036854775807 to "7fffffffffffffff"
        )

        longsWithHexStrings
            .map { it.first.toHexString() to it.second }
            .forEach {
                assertEquals(it.second, it.first.asString(withPrefix = false))
            }
    }

    @Test
    fun `creates HexString from BigInt`() {
        val bigIntegersWithExpected: List<Pair<BigInt, String>> = listOf(
            BigInt.valueOf(0),
            BigInt.valueOf(1),
            BigInt.valueOf(10),
            BigInt.valueOf(124),
            BigInt.valueOf(6346),
            BigInt.valueOf(Long.MAX_VALUE),
        ).map { it to it.toString(16).padStartEven('0') }

        bigIntegersWithExpected
            .map { it.first.toHexString() to it.second.asHexString() }
            .forEach {
                assertEquals(it.second, it.first)
            }
    }

    @Test
    fun `returns null when creating HexString from negative BigInteger and asked to`() {
        val negativeBigIntegers: List<BigInt> = listOf(
            Long.MIN_VALUE,
            -5236764366,
            -346657,
            -5265,
            -1,
        ).map(BigInt::valueOf)

        val hexStrings = negativeBigIntegers.mapNotNull(BigInt::toHexStringOrNull)

        assertEquals(0, hexStrings.size)
    }

    @Test
    fun `fails when creating HexString from negative BigInteger`() {
        val negativeBigIntegers: List<BigInt> = listOf(
            Long.MIN_VALUE,
            -5236764366,
            -346657,
            -5265,
            -1,
        ).map(BigInt::valueOf)

        negativeBigIntegers.forEach {
            assertFailsWith<IllegalArgumentException> {
                it.toHexString()
            }
        }
    }
}