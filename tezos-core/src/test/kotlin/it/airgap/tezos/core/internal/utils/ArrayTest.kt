package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.context.TezosCoreContext.asInt16Encoded
import it.airgap.tezos.core.internal.context.TezosCoreContext.asInt32Encoded
import it.airgap.tezos.core.internal.context.TezosCoreContext.asInt64Encoded
import it.airgap.tezos.core.internal.context.TezosCoreContext.asInt8Encoded
import it.airgap.tezos.core.internal.context.TezosCoreContext.padEnd
import it.airgap.tezos.core.internal.context.TezosCoreContext.padStart
import it.airgap.tezos.core.internal.context.TezosCoreContext.splitAt
import it.airgap.tezos.core.internal.context.TezosCoreContext.startsWith
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArrayTest {

    @Test
    fun `prepends ByteArray with fill value to match Int8 byte size`() {
        assertContentEquals(byteArrayOf(0), byteArrayOf().asInt8Encoded())
        assertContentEquals(byteArrayOf((255).toByte()), byteArrayOf().asInt8Encoded(fillValue = 255U))
        assertContentEquals(byteArrayOf(1), byteArrayOf(1).asInt8Encoded())
    }

    @Test
    fun `fails if requested to prepend ByteArray with fill value to match Int8 byte size but holds too big value`() {
        assertFailsWith<IllegalArgumentException> { byteArrayOf(1, 1).asInt8Encoded() }
    }

    @Test
    fun `prepends ByteArray with fill value to match Int16 byte size`() {
        assertContentEquals(byteArrayOf(0, 0), byteArrayOf().asInt16Encoded())
        assertContentEquals(byteArrayOf(0, 1), byteArrayOf(1).asInt16Encoded())
        assertContentEquals(byteArrayOf((255).toByte(), 1), byteArrayOf(1).asInt16Encoded(fillValue = 255U))
        assertContentEquals(byteArrayOf(1, 1), byteArrayOf(1, 1).asInt16Encoded())
    }

    @Test
    fun `fails if requested to prepend ByteArray with fill value to match Int16 byte size but holds too big value`() {
        assertFailsWith<IllegalArgumentException> { byteArrayOf(1, 1, 1).asInt16Encoded() }
    }

    @Test
    fun `prepends ByteArray with fill value to match Int32 byte size`() {
        assertContentEquals(byteArrayOf(0, 0, 0, 0), byteArrayOf().asInt32Encoded())
        assertContentEquals(byteArrayOf(0, 0, 0, 1), byteArrayOf(1).asInt32Encoded())
        assertContentEquals(byteArrayOf(0, 0, 1, 1), byteArrayOf(1, 1).asInt32Encoded())
        assertContentEquals(byteArrayOf((255).toByte(), (255).toByte(), 1, 1), byteArrayOf(1, 1).asInt32Encoded(fillValue = 255U))
        assertContentEquals(byteArrayOf(0, 1, 1, 1), byteArrayOf(1, 1, 1).asInt32Encoded())
        assertContentEquals(byteArrayOf(1, 1, 1, 1), byteArrayOf(1, 1, 1, 1).asInt32Encoded())
    }

    @Test
    fun `fails if requested to prepend ByteArray with fill value to match Int32 byte size but holds too big value`() {
        assertFailsWith<IllegalArgumentException> { byteArrayOf(1, 1, 1, 1, 1).asInt32Encoded() }
    }

    @Test
    fun `prepends ByteArray with fill value to match Int64 byte size`() {
        assertContentEquals(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0), byteArrayOf().asInt64Encoded())
        assertContentEquals(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 1), byteArrayOf(1).asInt64Encoded())
        assertContentEquals(byteArrayOf(0, 0, 0, 0, 0, 0, 1, 1), byteArrayOf(1, 1).asInt64Encoded())
        assertContentEquals(byteArrayOf(0, 0, 0, 0, 0, 1, 1, 1), byteArrayOf(1, 1, 1).asInt64Encoded())
        assertContentEquals(byteArrayOf(0, 0, 0, 0, 1, 1, 1, 1), byteArrayOf(1, 1, 1, 1).asInt64Encoded())
        assertContentEquals(byteArrayOf(0, 0, 0, 1, 1, 1, 1, 1), byteArrayOf(1, 1, 1, 1, 1).asInt64Encoded())
        assertContentEquals(byteArrayOf(0, 0, 1, 1, 1, 1, 1, 1), byteArrayOf(1, 1, 1, 1, 1, 1).asInt64Encoded())
        assertContentEquals(byteArrayOf(0, 1, 1, 1, 1, 1, 1, 1), byteArrayOf(1, 1, 1, 1, 1, 1, 1).asInt64Encoded())
        assertContentEquals(byteArrayOf((255).toByte(), 1, 1, 1, 1, 1, 1, 1), byteArrayOf(1, 1, 1, 1, 1, 1, 1).asInt64Encoded(fillValue = 255U))
        assertContentEquals(byteArrayOf(1, 1, 1, 1, 1, 1, 1, 1), byteArrayOf(1, 1, 1, 1, 1, 1, 1, 1).asInt64Encoded())
    }

    @Test
    fun `fails if requested to prepend ByteArray with fill value to match Int64 byte size but holds too big value`() {
        assertFailsWith<IllegalArgumentException> { byteArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1).asInt64Encoded() }
    }

    @Test
    fun `prepends ByteArray with fill value if size constraint is not met`() {
        assertContentEquals(byteArrayOf(0, 0, 0, 0), byteArrayOf().padStart(4))
        assertContentEquals(byteArrayOf(0, 0, 0, 1), byteArrayOf(1).padStart(4))
        assertContentEquals(byteArrayOf(0, 0, 1, 1), byteArrayOf(1, 1).padStart(4))
        assertContentEquals(byteArrayOf(0, 1, 1, 1), byteArrayOf(1, 1, 1).padStart(4))
        assertContentEquals(byteArrayOf(0, 1, 1, 0), byteArrayOf(1, 1, 0).padStart(4))
        assertContentEquals(byteArrayOf(0, 1, 0, 0), byteArrayOf(1, 0, 0).padStart(4))
        assertContentEquals(byteArrayOf(0, 0, 1, 0), byteArrayOf(1, 0).padStart(4))
    }

    @Test
    fun `does not prepend ByteArray with fill value if size constraint is met`() {
        assertContentEquals(byteArrayOf(), byteArrayOf().padStart(0))
        assertContentEquals(byteArrayOf(1), byteArrayOf(1).padStart(1))
        assertContentEquals(byteArrayOf(1, 1, 1), byteArrayOf(1, 1, 1).padStart(2))
        assertContentEquals(byteArrayOf(1, 1, 1, 1), byteArrayOf(1, 1, 1, 1).padStart(2))
    }

    @Test
    fun `appends ByteArray with fill value if size constraint is not met`() {
        assertContentEquals(byteArrayOf(0, 0, 0, 0), byteArrayOf().padEnd(4))
        assertContentEquals(byteArrayOf(1, 0, 0, 0), byteArrayOf(1).padEnd(4))
        assertContentEquals(byteArrayOf(1, 1, 0, 0), byteArrayOf(1, 1).padEnd(4))
        assertContentEquals(byteArrayOf(1, 1, 1, 0), byteArrayOf(1, 1, 1).padEnd(4))
        assertContentEquals(byteArrayOf(1, 1, 0, 0), byteArrayOf(1, 1, 0).padEnd(4))
        assertContentEquals(byteArrayOf(1, 0, 0, 0), byteArrayOf(1, 0).padEnd(4))
        assertContentEquals(byteArrayOf(0, 1, 0, 0), byteArrayOf(0, 1, 0).padEnd(4))
    }

    @Test
    fun `does not append ByteArray with fill value if size constraint is met`() {
        assertContentEquals(byteArrayOf(), byteArrayOf().padEnd(0))
        assertContentEquals(byteArrayOf(1), byteArrayOf(1).padEnd(1))
        assertContentEquals(byteArrayOf(1, 1, 1), byteArrayOf(1, 1, 1).padEnd(2))
        assertContentEquals(byteArrayOf(1, 1, 1, 1), byteArrayOf(1, 1, 1, 1).padEnd(2))
    }

    @Test
    fun `splits ByteArray into two at calculated index`() {
        val expectedWithActual = listOf(
            Pair(byteArrayOf(), byteArrayOf(1, 2, 3, 4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt { 0 },
            Pair(byteArrayOf(1, 2, 3, 4, 5, 6), byteArrayOf()) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt { it.size },
            Pair(byteArrayOf(1, 2, 3), byteArrayOf(4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt { it.size / 2 },
            Pair(byteArrayOf(1, 2), byteArrayOf(3, 4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt { it.size / 3 },
            Pair(byteArrayOf(1), byteArrayOf(2, 3, 4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt { 1 },
        )

        expectedWithActual.forEach { (expected, actual) ->
            assertContentEquals(expected.first, actual.first)
            assertContentEquals(expected.second, actual.second)
        }
    }

    @Test
    fun `splits ByteArray into two at calculated index and includes split index in first list`() {
        val expectedWithActual = listOf(
            Pair(byteArrayOf(1), byteArrayOf(2, 3, 4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(firstInclusive = true) { 0 },
            Pair(byteArrayOf(1, 2, 3, 4, 5, 6), byteArrayOf()) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(firstInclusive = true) { it.size },
            Pair(byteArrayOf(1, 2, 3, 4), byteArrayOf(5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(firstInclusive = true) { it.size / 2 },
            Pair(byteArrayOf(1, 2, 3), byteArrayOf(4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(firstInclusive = true) { it.size / 3 },
            Pair(byteArrayOf(1, 2), byteArrayOf(3, 4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(firstInclusive = true) { 1 },
        )

        expectedWithActual.forEach { (expected, actual) ->
            assertContentEquals(expected.first, actual.first)
            assertContentEquals(expected.second, actual.second)
        }
    }

    @Test
    fun `splits ByteArray into two at specified index`() {
        val expectedWithActual = listOf(
            Pair(byteArrayOf(), byteArrayOf(1, 2, 3, 4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(0),
            Pair(byteArrayOf(1, 2, 3, 4, 5, 6), byteArrayOf()) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(6),
            Pair(byteArrayOf(1, 2, 3), byteArrayOf(4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(3),
            Pair(byteArrayOf(1, 2), byteArrayOf(3, 4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(2),
            Pair(byteArrayOf(1), byteArrayOf(2, 3, 4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(1),
        )

        expectedWithActual.forEach { (expected, actual) ->
            assertContentEquals(expected.first, actual.first)
            assertContentEquals(expected.second, actual.second)
        }
    }

    @Test
    fun `splits ByteArray into two at specified index and includes split index in first list`() {
        val expectedWithActual = listOf(
            Pair(byteArrayOf(1), byteArrayOf(2, 3, 4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(0, firstInclusive = true),
            Pair(byteArrayOf(1, 2, 3, 4, 5, 6), byteArrayOf()) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(6, firstInclusive = true),
            Pair(byteArrayOf(1, 2, 3, 4), byteArrayOf(5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(3, firstInclusive = true),
            Pair(byteArrayOf(1, 2, 3), byteArrayOf(4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(2, firstInclusive = true),
            Pair(byteArrayOf(1, 2), byteArrayOf(3, 4, 5, 6)) to byteArrayOf(1, 2, 3, 4, 5, 6).splitAt(1, firstInclusive = true),
        )

        expectedWithActual.forEach { (expected, actual) ->
            assertContentEquals(expected.first, actual.first)
            assertContentEquals(expected.second, actual.second)
        }
    }

    @Test
    fun `verifies if ByteArray starts with specified bytes`() {
        assertTrue(byteArrayOf(1, 2, 3).startsWith(byteArrayOf()))
        assertTrue(byteArrayOf(1, 2, 3).startsWith(byteArrayOf(1)))
        assertTrue(byteArrayOf(1, 2, 3).startsWith(byteArrayOf(1, 2)))
        assertTrue(byteArrayOf(1, 2, 3).startsWith(byteArrayOf(1, 2, 3)))
        assertFalse(byteArrayOf(1, 2, 3).startsWith(byteArrayOf(2, 3)))
    }
}