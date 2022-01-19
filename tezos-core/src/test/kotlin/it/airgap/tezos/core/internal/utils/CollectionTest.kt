package it.airgap.tezos.core.internal.utils

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollectionTest {

    @Test
    fun `checks if all elements are of specified instance`() {
        assertTrue(listOf("1", "2", "3").allIsInstance<String>())
        assertFalse(listOf(1, "2", "3").allIsInstance<String>())
        assertFalse(listOf("1", 2, "3").allIsInstance<String>())
        assertFalse(listOf("1", "2", 3).allIsInstance<String>())
        assertFalse(listOf(1, 2, 3).allIsInstance<String>())
    }

    @Test
    fun `checks if any element is of specified instance`() {
        assertFalse(listOf("1", "2", "3").anyIsInstance<Int>())
        assertTrue(listOf(1, "2", "3").anyIsInstance<Int>())
        assertTrue(listOf("1", 2, "3").anyIsInstance<Int>())
        assertTrue(listOf("1", "2", 3).anyIsInstance<Int>())
        assertTrue(listOf(1, 2, 3).anyIsInstance<Int>())
    }

    @Test
    fun `replaces or inserts element at specified index`() {
        assertEquals(
            mutableListOf<Int?>(1),
            mutableListOf<Int?>().apply { replaceOrAdd(0, 1) },
        )

        assertEquals(
            mutableListOf(null, 2),
            mutableListOf<Int?>().apply { replaceOrAdd(1, 2) },
        )

        assertEquals(
            mutableListOf<Int?>(1, 0, 3),
            mutableListOf<Int?>(1, 2, 3).apply { replaceOrAdd(1, 0) },
        )

        assertEquals(
            mutableListOf<Int?>(1, 2, 0),
            mutableListOf<Int?>(1, 2, 3).apply { replaceOrAdd(2, 0) },
        )

        assertEquals(
            mutableListOf<Int?>(1, 2, 3, 4),
            mutableListOf<Int?>(1, 2, 3).apply { replaceOrAdd(3, 4) },
        )
    }

    @Test
    fun `consumes element from MutableList at specified index`() {
        assertEquals(
            mutableListOf(),
            mutableListOf(1).apply { consumeAt(0) },
        )

        assertEquals(
            mutableListOf(2, 3),
            mutableListOf(1, 2, 3).apply { consumeAt(0) },
        )

        assertEquals(
            mutableListOf(1, 3),
            mutableListOf(1, 2, 3).apply { consumeAt(1) },
        )

        assertEquals(
            mutableListOf(1, 2),
            mutableListOf(1, 2, 3).apply { consumeAt(2) },
        )
    }

    @Test
    fun `does not consume any element from MutableList if index is out of range`() {
        assertEquals(
            mutableListOf(),
            mutableListOf<Int>().apply { consumeAt(0) },
        )

        assertEquals(
            mutableListOf(1, 2, 3),
            mutableListOf(1, 2, 3).apply { consumeAt(3) },
        )
    }

    @Test
    fun `consumes slice from MutableList at specified indices`() {
        val list1 = mutableListOf(1)
        val slice1 = list1.consumeAt(0 until 1)

        assertEquals(mutableListOf(), list1)
        assertEquals(listOf(1), slice1)

        val list2 = mutableListOf(1, 2, 3)
        val slice2 = list2.consumeAt(1..2)

        assertEquals(mutableListOf(1), list2)
        assertEquals(listOf(2, 3), slice2)

        val list3 = mutableListOf(1, 2, 3)
        val slice3 = list3.consumeAt(1 until 2)

        assertEquals(mutableListOf(1, 3), list3)
        assertEquals(listOf(2), slice3)

        val list4 = mutableListOf(1, 2, 3)
        val slice4 = list4.consumeAt(2..3)

        assertEquals(mutableListOf(1, 2), list4)
        assertEquals(listOf(3), slice4)

        val list5 = mutableListOf(1, 2, 3)
        val slice5 = list5.consumeAt(3..10)

        assertEquals(mutableListOf(1, 2, 3), list5)
        assertEquals(listOf(), slice5)
    }
}