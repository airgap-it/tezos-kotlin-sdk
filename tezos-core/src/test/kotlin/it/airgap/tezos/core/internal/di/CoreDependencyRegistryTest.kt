package it.airgap.tezos.core.internal.di

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import it.airgap.tezos.core.crypto.CryptoProvider
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CoreDependencyRegistryTest {

    @MockK
    private lateinit var cryptoProvider: CryptoProvider

    private lateinit var coreDependencyRegistry: CoreDependencyRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coreDependencyRegistry = CoreDependencyRegistry(cryptoProvider)
    }

    @Test
    fun `saves scoped registry`() {
        val scoped = TestScopedDependencyRegistry(coreDependencyRegistry)
        coreDependencyRegistry.addScoped(scoped)

        assertEquals(scoped, coreDependencyRegistry.scoped[scoped::class.qualifiedName])
    }

    @Test
    fun `retrieves saved scoped registry`() {
        val scoped = TestScopedDependencyRegistry(coreDependencyRegistry)
        coreDependencyRegistry.addScoped(scoped)

        assertEquals(scoped, coreDependencyRegistry.findScoped(TestScopedDependencyRegistry::class))
        assertEquals(scoped, coreDependencyRegistry.findScoped())
    }

    @Test
    fun `does not find unsaved scoped registry`() {
        assertNull(coreDependencyRegistry.findScoped(TestScopedDependencyRegistry::class))
        assertNull(coreDependencyRegistry.findScoped<TestScopedDependencyRegistry>())
    }

    private class TestScopedDependencyRegistry(dependencyRegistry: DependencyRegistry) : DependencyRegistry by dependencyRegistry
}