package it.airgap.tezos.core.internal.delegate

import it.airgap.tezos.core.internal.context.TezosCoreContext.default
import org.junit.Test
import kotlin.test.assertEquals

class DefaultTest {
    @Test
    fun `returns default value if no value set`() {
        val defaultValue = "default"
        val value by default { defaultValue }

        assertEquals(defaultValue, value)
    }

    @Test
    fun `returns value if set`() {
        val defaultValue = "default"
        val newValue = "value"
        var value by default { defaultValue }
        value = newValue

        assertEquals(newValue, value)
    }

    @Test
    fun `returns initial value if set`() {
        val defaultValue = "default"
        val initialValue = "value"
        val value by default(initialValue) { defaultValue }

        assertEquals(initialValue, value)
    }
}