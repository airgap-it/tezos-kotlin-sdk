package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.decodeConsumingBoolean
import it.airgap.tezos.core.internal.context.TezosCoreContext.decodeConsumingHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.decodeConsumingInt32
import it.airgap.tezos.core.internal.context.TezosCoreContext.decodeConsumingInt64
import it.airgap.tezos.core.internal.context.TezosCoreContext.decodeConsumingList
import it.airgap.tezos.core.internal.context.TezosCoreContext.decodeConsumingString
import it.airgap.tezos.core.internal.context.TezosCoreContext.decodeConsumingUInt16
import it.airgap.tezos.core.internal.context.TezosCoreContext.decodeConsumingUInt8
import it.airgap.tezos.core.internal.context.TezosCoreContext.encodeToBytes
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class CodingTest {

    @Test
    fun `should encode string to [ByteArray]`() {
        val stringsWithExpected = listOf(
            "LG" to "4c47",
            "LMU28" to "4c4d553238",
            "n67j9P" to "6e36376a3950",
            "yHu7ZlJHp" to "794875375a6c4a4870",
            "kGwGjywiI3" to "6b4777476a7977694933",
            "C9Vjzijftyge" to "4339566a7a696a6674796765",
        )

        stringsWithExpected.forEach {
            val string = it.first
            val bytes = it.second.asHexString().toByteArray()

            assertContentEquals(bytes, string.encodeToBytes())
        }
    }

    @Test
    fun `should decode consuming string from bytes`() {
        val bytesWithExpected = listOf(
            Pair("4c47", 2) to "LG",
            Pair("4c4d553238", 5) to "LMU28",
            Pair("6e36376a3950", 6) to "n67j9P",
            Pair("794875375a6c4a4870", 9) to "yHu7ZlJHp",
            Pair("6b4777476a7977694933", 10) to "kGwGjywiI3",
            Pair("4339566a7a696a6674796765", 12) to "C9Vjzijftyge",
            Pair("4c47e2a836", 2) to "LG",
            Pair("4c4d553238250d51", 5) to "LMU28",
            Pair("6e36376a395000", 6) to "n67j9P",
            Pair("794875375a6c4a4870bd21", 9) to "yHu7ZlJHp",
            Pair("6b4777476a7977694933", 10) to "kGwGjywiI3",
            Pair("4339566a7a696a6674796765b2", 12) to "C9Vjzijftyge",
        )

        bytesWithExpected.forEach {
            val string = it.second
            val bytes = it.first.first.asHexString().toByteArray()
            val size = it.first.second

            assertEquals(string, bytes.toMutableList().decodeConsumingString(size))
        }
    }

    @Test
    fun `should encode HexString to [ByteArray]`() {
        val stringsWithExpected = listOf(
            "4c47",
            "4c4d553238",
            "6e36376a3950",
            "794875375a6c4a4870",
            "6b4777476a7977694933",
            "4339566a7a696a6674796765",
        ).map { it to it }

        stringsWithExpected.forEach {
            val string = it.first.asHexString()
            val bytes = it.second.asHexString().toByteArray()

            assertContentEquals(bytes, string.encodeToBytes())
        }
    }

    @Test
    fun `should decode consuming HexString from bytes`() {
        val bytesWithExpected = listOf(
            Pair("4c47", 2) to "4c47",
            Pair("4c4d553238", 5) to "4c4d553238",
            Pair("6e36376a3950", 6) to "6e36376a3950",
            Pair("794875375a6c4a4870", 9) to "794875375a6c4a4870",
            Pair("6b4777476a7977694933", 10) to "6b4777476a7977694933",
            Pair("4339566a7a696a6674796765", 12) to "4339566a7a696a6674796765",
            Pair("4c47e2a836", 2) to "4c47",
            Pair("4c4d553238250d51", 5) to "4c4d553238",
            Pair("6e36376a395000", 6) to "6e36376a3950",
            Pair("794875375a6c4a4870bd21", 9) to "794875375a6c4a4870",
            Pair("6b4777476a7977694933", 10) to "6b4777476a7977694933",
            Pair("4339566a7a696a6674796765b2", 12) to "4339566a7a696a6674796765",
        )

        bytesWithExpected.forEach {
            val string = it.second.asHexString()
            val bytes = it.first.first.asHexString().toByteArray()
            val size = it.first.second

            assertEquals(string, bytes.toMutableList().decodeConsumingHexString(size))
        }
    }

    @Test
    fun `should encode UInt8 to [ByteArray]`() {
        val uint8sWithExpected = listOf(
            0 to "00",
            1 to "01",
            42 to "2a",
            127 to "7f",
            128 to "80",
            255 to "ff",
        )

        uint8sWithExpected.forEach {
            val uint8 = it.first.toUByte()
            val bytes = it.second.asHexString().toByteArray()

            assertContentEquals(bytes, uint8.encodeToBytes())
        }
    }

    @Test
    fun `should decode consuming UInt8 from bytes`() {
        val bytesWithExpected = listOf(
            "00" to 0,
            "01" to 1,
            "2a" to 42,
            "7f" to 127,
            "80" to 128,
            "ff" to 255,
            "000b24" to 0,
            "0101" to 1,
            "2a5261a9" to 42,
            "7f96e1739dab" to 127,
            "80c15a3e" to 128,
            "ffffffff00" to 255,
        )

        bytesWithExpected.forEach {
            val uint8 = it.second.toUByte()
            val bytes = it.first.asHexString().toByteArray()

            assertEquals(uint8, bytes.toMutableList().decodeConsumingUInt8())
        }
    }

    @Test
    fun `should encode UInt16 to [ByteArray]`() {
        val uint16sWithExpected = listOf(
            0 to "0000",
            1 to "0001",
            42 to "002a",
            127 to "007f",
            128 to "0080",
            255 to "00ff",
            352 to "0160",
            65535 to "ffff",
        )

        uint16sWithExpected.forEach {
            val uint16 = it.first.toUShort()
            val bytes = it.second.asHexString().toByteArray()

            assertContentEquals(bytes, uint16.encodeToBytes())
        }
    }

    @Test
    fun `should decode consuming UInt16 from bytes`() {
        val bytesWithExpected = listOf(
            "0000" to 0,
            "0001" to 1,
            "002a" to 42,
            "007f" to 127,
            "0080" to 128,
            "00ff" to 255,
            "0160" to 352,
            "ffff" to 65535,
            "00000b24" to 0,
            "000101" to 1,
            "002a5261a9" to 42,
            "007f96e1739dab" to 127,
            "0080c15a3e" to 128,
            "00ffffffff00" to 255,
            "016042" to 352,
            "ffff0b6a21" to 65535,
        )

        bytesWithExpected.forEach {
            val uint16 = it.second.toUShort()
            val bytes = it.first.asHexString().toByteArray()

            assertEquals(uint16, bytes.toMutableList().decodeConsumingUInt16())
        }
    }

    @Test
    fun `should encode Int32 to [ByteArray]`() {
        val int32sWithExpected = listOf(
            -2147483648 to "80000000",
            -72406251 to "fbaf2b15",
            -242810 to "fffc4b86",
            -65535 to "ffff0001",
            -352 to "fffffea0",
            -255 to "ffffff01",
            -128 to "ffffff80",
            -127 to "ffffff81",
            -42 to "ffffffd6",
            -1 to "ffffffff",
            0 to "00000000",
            1 to "00000001",
            42 to "0000002a",
            127 to "0000007f",
            128 to "00000080",
            255 to "000000ff",
            352 to "00000160",
            65535 to "0000ffff",
            242810 to "0003b47a",
            72406251 to "0450d4eb",
            2147483647 to "7fffffff",
        )

        int32sWithExpected.forEach {
            val int32 = it.first
            val bytes = it.second.asHexString().toByteArray()

            assertContentEquals(bytes, int32.encodeToBytes())
        }
    }

    @Test
    fun `should decode consuming Int32 from bytes`() {
        val bytesWithExpected = listOf(
            "80000000" to -2147483648,
            "fbaf2b15" to -72406251,
            "fffc4b86" to -242810,
            "ffff0001" to -65535,
            "fffffea0" to -352,
            "ffffff01" to -255,
            "ffffff80" to -128,
            "ffffff81" to -127,
            "ffffffd6" to -42,
            "ffffffff" to -1,
            "00000000" to 0,
            "00000001" to 1,
            "0000002a" to 42,
            "0000007f" to 127,
            "00000080" to 128,
            "000000ff" to 255,
            "00000160" to 352,
            "0000ffff" to 65535,
            "0003b47a" to 242810,
            "0450d4eb" to 72406251,
            "7fffffff" to 2147483647,
            "8000000063" to -2147483648,
            "fbaf2b15b72a" to -72406251,
            "fffc4b8693620b" to -242810,
            "ffff0001627ab2e560" to -65535,
            "fffffea00071dc" to -352,
            "ffffff01bc52" to -255,
            "ffffff80916482be5d" to -128,
            "ffffff818174" to -127,
            "ffffffd6dc53ea" to -42,
            "ffffffff91" to -1,
            "000000006281e673" to 0,
            "00000001271b390a52be" to 1,
            "0000002affffffff" to 42,
            "0000007f62f72bea" to 127,
            "000000808262de" to 128,
            "000000ff0292" to 255,
            "00000160eb834a74" to 352,
            "0000ffff00" to 65535,
            "0003b47aff" to 242810,
            "0450d4eb82fba2" to 72406251,
            "7fffffff84" to 2147483647,
        )

        bytesWithExpected.forEach {
            val int32 = it.second
            val bytes = it.first.asHexString().toByteArray()

            assertEquals(int32, bytes.toMutableList().decodeConsumingInt32())
        }
    }

    @Test
    fun `should encode Int64 to [ByteArray]`() {
        val int32sWithExpected = listOf(
            (-9223372036854775807 - 1) to "8000000000000000",
            -4294967254 to "ffffffff0000002a",
            -2147483648 to "ffffffff80000000",
            -72406251 to "fffffffffbaf2b15",
            -242810 to "fffffffffffc4b86",
            -65535 to "ffffffffffff0001",
            -352 to "fffffffffffffea0",
            -255 to "ffffffffffffff01",
            -128 to "ffffffffffffff80",
            -127 to "ffffffffffffff81",
            -42 to "ffffffffffffffd6",
            -1 to "ffffffffffffffff",
            0 to "0000000000000000",
            1 to "0000000000000001",
            42 to "000000000000002a",
            127 to "000000000000007f",
            128 to "0000000000000080",
            255 to "00000000000000ff",
            352 to "0000000000000160",
            65535 to "000000000000ffff",
            242810 to "000000000003b47a",
            72406251 to "000000000450d4eb",
            2147483647 to "000000007fffffff",
            4294967254 to "00000000ffffffd6",
            9223372036854775807 to "7fffffffffffffff",
        )

        int32sWithExpected.forEach {
            val int64 = it.first.toLong()
            val bytes = it.second.asHexString().toByteArray()

            assertContentEquals(bytes, int64.encodeToBytes())
        }
    }

    @Test
    fun `should decode consuming Int64 from bytes`() {
        val bytesWithExpected = listOf(
            "8000000000000000" to (-9223372036854775807 - 1),
            "ffffffff0000002a" to -4294967254,
            "ffffffff80000000" to -2147483648,
            "fffffffffbaf2b15" to -72406251,
            "fffffffffffc4b86" to -242810,
            "ffffffffffff0001" to -65535,
            "fffffffffffffea0" to -352,
            "ffffffffffffff01" to -255,
            "ffffffffffffff80" to -128,
            "ffffffffffffff81" to -127,
            "ffffffffffffffd6" to -42,
            "ffffffffffffffff" to -1,
            "0000000000000000" to 0,
            "0000000000000001" to 1,
            "000000000000002a" to 42,
            "000000000000007f" to 127,
            "0000000000000080" to 128,
            "00000000000000ff" to 255,
            "0000000000000160" to 352,
            "000000000000ffff" to 65535,
            "000000000003b47a" to 242810,
            "000000000450d4eb" to 72406251,
            "000000007fffffff" to 2147483647,
            "00000000ffffffd6" to 4294967254,
            "7fffffffffffffff" to 9223372036854775807,
            "8000000000000000bfac" to (-9223372036854775807 - 1),
            "ffffffff0000002ab412a0" to -4294967254,
            "ffffffff8000000063" to -2147483648,
            "fffffffffbaf2b15b72a" to -72406251,
            "fffffffffffc4b8693620b" to -242810,
            "ffffffffffff0001627ab2e560" to -65535,
            "fffffffffffffea00071dc" to -352,
            "ffffffffffffff01bc52" to -255,
            "ffffffffffffff80916482be5d" to -128,
            "ffffffffffffff818174" to -127,
            "ffffffffffffffd6dc53ea" to -42,
            "ffffffffffffffff91" to -1,
            "00000000000000006281e673" to 0,
            "0000000000000001271b390a52be" to 1,
            "000000000000002affffffff" to 42,
            "000000000000007f62f72bea" to 127,
            "00000000000000808262de" to 128,
            "00000000000000ff0292" to 255,
            "0000000000000160eb834a74" to 352,
            "000000000000ffff00" to 65535,
            "000000000003b47aff" to 242810,
            "000000000450d4eb82fba2" to 72406251,
            "000000007fffffff84" to 2147483647,
            "00000000ffffffd6b3ac" to 4294967254,
            "7fffffffffffffff02060ab7" to 9223372036854775807,
        )

        bytesWithExpected.forEach {
            val int64 = it.second.toLong()
            val bytes = it.first.asHexString().toByteArray()

            assertEquals(int64, bytes.toMutableList().decodeConsumingInt64())
        }
    }

    @Test
    fun `should encode Boolean to [ByteArray]`() {
        val booleansWithExpected = listOf(
            true to "ff",
            false to "00",
        )

        booleansWithExpected.forEach {
            val boolean = it.first
            val bytes = it.second.asHexString().toByteArray()

            assertContentEquals(bytes, boolean.encodeToBytes())
        }
    }

    @Test
    fun `should decode consuming Boolean from bytes`() {
        val bytesWithExpected = listOf(
            "ff" to true,
            "00" to false,
            "ff0ba32f" to true,
            "00fc" to false,
            "01" to null,
        )

        bytesWithExpected.forEach {
            val boolean = it.second
            val bytes = it.first.asHexString().toByteArray()

            assertEquals(boolean, bytes.toMutableList().decodeConsumingBoolean())
        }
    }

    @Test
    fun `should encode List to [ByteArray]`() {
        assertContentEquals(
            "ffff00".asHexString().toByteArray(),
            listOf(true, true, false).encodeToBytes { it.encodeToBytes() }
        )

        assertContentEquals(
            "00010203".asHexString().toByteArray(),
            listOf(0, 1, 2, 3).map { it.toUByte() }.encodeToBytes { it.encodeToBytes() }
        )
    }

    @Test
    fun `should decode consuming List from bytes`() {
        assertEquals(
            listOf(true, true, false),
            "ffff00".asHexString().toByteArray().toMutableList().decodeConsumingList { it.decodeConsumingBoolean() }
        )

        assertEquals(
            listOf(0, 1, 2, 3).map { it.toUByte() },
            "00010203".asHexString().toByteArray().toMutableList().decodeConsumingList { it.decodeConsumingUInt8() }
        )
    }
}