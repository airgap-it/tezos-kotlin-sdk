package it.airgap.tezos.rpc.internal.cache

import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class CachedTest {

    @Test
    fun `should cache resource`() {
        val resource = TestResource(1)
        var fetchCounter = 0
        val cached = Cached {
            fetchCounter++
            resource
        }

        val value1 = runBlocking { cached.get() }
        val value2 = runBlocking { cached.get() }

        assertEquals(resource, value1)
        assertEquals(resource, value2)
        assertEquals(1, fetchCounter)
    }

    @Test
    fun `should map cached resource`() {
        val resource = TestResource(1)
        val cached = Cached { resource }

        val mapped = cached.map { TestResource(it.data + 1) }
        val value = runBlocking { mapped.get() }

        assertEquals(TestResource(resource.data + 1), value)
    }

    @Test
    fun `should combine cached resources`() {
        val resource1 = TestResource(1)
        val resource2 = TestResource(2)

        val cached1 = Cached { resource1 }
        val cached2 = Cached { resource2 }

        val combined = cached1.combine(cached2)
        val value = runBlocking { combined.get() }

        assertEquals(Pair(resource1, resource2), value)
    }

    @Test
    fun `should cache keyed resource`() {
        val resources = mapOf(
            "a" to TestResource(1),
            "b" to TestResource(2),
        )
        val fetchCounters = mutableMapOf(
            "a" to 0,
            "b" to 0,
        )
        val cached = CachedMap<String, TestResource> { key, _ ->
            fetchCounters[key] = fetchCounters.getOrDefault(key, 0) + 1
            resources[key]!!
        }

        val a = runBlocking { cached.get("a") }
        val b = runBlocking { cached.get("b") }

        assertEquals(resources["a"], a)
        assertEquals(resources["b"], b)
        assertEquals(1, fetchCounters["a"])
        assertEquals(1, fetchCounters["b"])
    }

    private data class TestResource(val data: Int)
}