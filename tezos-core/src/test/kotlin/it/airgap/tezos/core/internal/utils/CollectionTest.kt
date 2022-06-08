package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.context.TezosCoreContext.allIsInstance
import it.airgap.tezos.core.internal.context.TezosCoreContext.anyIsInstance
import it.airgap.tezos.core.internal.context.TezosCoreContext.consume
import it.airgap.tezos.core.internal.context.TezosCoreContext.consumeAll
import it.airgap.tezos.core.internal.context.TezosCoreContext.consumeAt
import it.airgap.tezos.core.internal.context.TezosCoreContext.containsAny
import it.airgap.tezos.core.internal.context.TezosCoreContext.firstInstanceOfOrNull
import it.airgap.tezos.core.internal.context.TezosCoreContext.flatten
import it.airgap.tezos.core.internal.context.TezosCoreContext.replaceOrAdd
import it.airgap.tezos.core.internal.context.TezosCoreContext.replacingAt
import it.airgap.tezos.core.internal.context.TezosCoreContext.secondInstanceOfOrNull
import it.airgap.tezos.core.internal.context.TezosCoreContext.startsWith
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CollectionTest {

    @Test
    fun `checks if all elements are of specified instance`() {
        assertTrue(listOf("1", "2", "3").allIsInstance(String::class))
        assertFalse(listOf(1, "2", "3").allIsInstance(String::class))
        assertFalse(listOf("1", 2, "3").allIsInstance(String::class))
        assertFalse(listOf("1", "2", 3).allIsInstance(String::class))
        assertFalse(listOf(1, 2, 3).allIsInstance(String::class))
    }

    @Test
    fun `checks if any element is of specified instance`() {
        assertFalse(listOf("1", "2", "3").anyIsInstance(Int::class))
        assertTrue(listOf(1, "2", "3").anyIsInstance(Int::class))
        assertTrue(listOf("1", 2, "3").anyIsInstance(Int::class))
        assertTrue(listOf("1", "2", 3).anyIsInstance(Int::class))
        assertTrue(listOf(1, 2, 3).anyIsInstance(Int::class))
    }

    @Test
    fun `returns first element of specified type or null`() {
        assertNull(listOf(1, 2, 3).firstInstanceOfOrNull(String::class))
        assertEquals(2, listOf("1", 2, 3, 4, "5").firstInstanceOfOrNull(Int::class))
    }

    @Test
    fun `returns second element of specified type or null`() {
        assertNull(listOf(1, 2, 3).secondInstanceOfOrNull(String::class))
        assertEquals(3, listOf("1", 2, 3, 4, "5").secondInstanceOfOrNull(Int::class))
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
    fun `consumes first element from MutableList that fulfills predicate`() {
        assertEquals(
            mutableListOf(),
            mutableListOf(1).apply { consume { it > 0 } },
        )

        assertEquals(
            mutableListOf(2, 3),
            mutableListOf(1, 2, 3).apply { consume { it == 1 } },
        )

        assertEquals(
            mutableListOf(1, 3),
            mutableListOf(1, 2, 3).apply { consume { it == 2 } },
        )

        assertEquals(
            mutableListOf(1, 2),
            mutableListOf(1, 2, 3).apply { consume { it == 3 } },
        )
    }

    @Test
    fun `consumes all elements from MutableList that fulfill predicate`() {
        assertEquals(
            mutableListOf(),
            mutableListOf(1).apply { consumeAll { it > 0 } },
        )

        assertEquals(
            mutableListOf(2, 3),
            mutableListOf(1, 2, 1, 3, 1).apply { consumeAll { it == 1 } },
        )

        assertEquals(
            mutableListOf(1, 3),
            mutableListOf(1, 2, 2, 3).apply { consumeAll { it == 2 } },
        )

        assertEquals(
            mutableListOf(1, 2),
            mutableListOf(1, 3, 2, 3, 3).apply { consumeAll { it == 3 } },
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

    @Test
    fun `replaces element at specified index in List`() {
        assertEquals(
            listOf(4, 2, 3),
            listOf(1, 2, 3).replacingAt(0, 4),
        )

        assertEquals(
            listOf(1, 4, 3),
            listOf(1, 2, 3).replacingAt(1, 4),
        )

        assertEquals(
            listOf(1, 2, 4),
            listOf(1, 2, 3).replacingAt(2, 4),
        )
    }

    @Test
    fun `does not replace element at specified index in List if index is out of range`() {
        assertEquals(
            listOf(),
            listOf<Int>().replacingAt(0, 1),
        )

        assertEquals(
            listOf(1, 2, 3),
            listOf(1, 2, 3).replacingAt(3, 4),
        )
    }

    @Test
    fun `verifies if List starts with specified elements`() {
        assertTrue(listOf(1, 2, 3).startsWith(listOf()))
        assertTrue(listOf(1, 2, 3).startsWith(listOf(1)))
        assertTrue(listOf(1, 2, 3).startsWith(listOf(1, 2)))
        assertTrue(listOf(1, 2, 3).startsWith(listOf(1, 2, 3)))
        assertFalse(listOf(1, 2, 3).startsWith(listOf(2, 3)))
    }

    @Test
    fun `verifies if List of bytes starts with specified bytes`() {
        assertTrue(listOf<Byte>(1, 2, 3).startsWith(byteArrayOf()))
        assertTrue(listOf<Byte>(1, 2, 3).startsWith(byteArrayOf(1)))
        assertTrue(listOf<Byte>(1, 2, 3).startsWith(byteArrayOf(1, 2)))
        assertTrue(listOf<Byte>(1, 2, 3).startsWith(byteArrayOf(1, 2, 3)))
        assertFalse(listOf<Byte>(1, 2, 3).startsWith(byteArrayOf(2, 3)))
    }

    @Test
    fun `flattens list of maps`() {
        assertEquals(
            mapOf(
                "a" to 1,
                "b" to 2,
                "c" to 3,
            ),
            listOf(
                mapOf("a" to 1),
                mapOf("b" to 2),
                mapOf("c" to 3),
            ).flatten()
        )

        assertEquals(
            mapOf(
                "a" to 1,
                "b" to 2,
                "c" to 4,
            ),
            listOf(
                mapOf("a" to 1),
                mapOf("b" to 2),
                mapOf("c" to 3),
                mapOf("c" to 4),
            ).flatten()
        )
    }

    @Test
    fun `checks if set contains any element of other set`() {
        assertTrue(setOf(1, 2, 3).containsAny(setOf(1)))
        assertTrue(setOf(1, 2, 3).containsAny(setOf(2)))
        assertTrue(setOf(1, 2, 3).containsAny(setOf(3)))
        assertTrue(setOf(1, 2, 3).containsAny(setOf(2, 3)))
        assertTrue(setOf(1, 2, 3).containsAny(setOf(3, 4)))
        assertFalse(setOf(1, 2, 3).containsAny(setOf(4)))
        assertFalse(setOf(1, 2, 3).containsAny(setOf(4, 5)))
    }
}