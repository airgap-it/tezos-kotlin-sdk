package it.airgap.tezos.michelson.internal.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CollectionTest {

    @Test
    fun `returns second element from list if enough elements`() {
        assertEquals(2, listOf(1, 2, 3).second())
    }

    @Test
    fun `fails to return second element from list if not enough elements`() {
        assertFailsWith<NoSuchElementException> { listOf<Int>().second() }
        assertFailsWith<NoSuchElementException> { listOf(1).second() }
    }

    @Test
    fun `returns third element from list if enough elements`() {
        assertEquals(3, listOf(1, 2, 3).third())
    }

    @Test
    fun `fails to return third element from list if not enough elements`() {
        assertFailsWith<NoSuchElementException> { listOf<Int>().third() }
        assertFailsWith<NoSuchElementException> { listOf(1).third() }
        assertFailsWith<NoSuchElementException> { listOf(1, 2).third() }
    }
}