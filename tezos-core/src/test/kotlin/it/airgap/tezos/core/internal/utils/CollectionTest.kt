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
}