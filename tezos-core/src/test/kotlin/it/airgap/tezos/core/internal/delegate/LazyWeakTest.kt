package it.airgap.tezos.core.internal.delegate

import it.airgap.tezos.core.internal.context.TezosCoreContext.lazyWeak
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class LazyWeakTest {

    @Test
    fun `initializes value`() {
        var initCounter = 0
        val value by lazyWeak {
            initCounter++
            TestReference()
        }

        val h1 = value.hashCode()
        System.gc()
        val h2 = value.hashCode()

        assertNotEquals(h1, h2, "Expected lazy weak values hash codes to be different.")
        assertEquals(2, initCounter)
    }

    @Test
    fun `keeps value reference`() {
        var initCounter = 0
        val value by lazyWeak {
            initCounter++
            TestReference()
        }

        val v = value

        val h1 = value.hashCode()
        System.gc()
        val h2 = value.hashCode()

        assertEquals(h1, h2, "Expected lazy weak values hash codes to be the same.")
        assertEquals(1, initCounter)
    }

    @Test
    fun `checks if value is initialized`() {
        val delegate = lazyWeak { TestReference() }
        val value by delegate

        assertFalse(delegate.isInitialized())

        value.hashCode()
        assertTrue(delegate.isInitialized())

        System.gc()
        assertFalse(delegate.isInitialized())
    }

    @Test
    fun `returns value's String value if initialized or uninitialized message otherwise`() {
        val reference = TestReference()
        val delegate = lazyWeak { reference }
        val value by delegate

        assertEquals("LazyWeak has no value reference.", delegate.toString())
        value.hashCode()
        assertEquals(reference.toString(), delegate.toString())
    }

    private class TestReference
}