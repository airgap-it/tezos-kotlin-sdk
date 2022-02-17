package it.airgap.tezos.michelson.internal.utils

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JsonTest {

    @Test
    fun `should check if JsonObject contains any of specified keys`() {
        val jsonObject = buildJsonObject {
            put("a", JsonPrimitive(1))
            put("b", JsonPrimitive(2))
        }

        assertTrue(jsonObject.containsOneOfKeys("a", "1"))
        assertTrue(jsonObject.containsOneOfKeys("1", "a"))
        assertTrue(jsonObject.containsOneOfKeys("b", "1"))
        assertTrue(jsonObject.containsOneOfKeys("1", "b"))
        assertTrue(jsonObject.containsOneOfKeys("a", "b"))
        assertFalse(jsonObject.containsOneOfKeys())
        assertFalse(jsonObject.containsOneOfKeys("1"))
        assertFalse(jsonObject.containsOneOfKeys("1", "2"))
    }
}