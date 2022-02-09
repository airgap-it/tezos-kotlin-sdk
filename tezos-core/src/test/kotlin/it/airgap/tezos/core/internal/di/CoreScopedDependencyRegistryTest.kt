package it.airgap.tezos.core.internal.di

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CoreScopedDependencyRegistryTest {

    @MockK(relaxed = true)
    private lateinit var dependencyRegistry: DependencyRegistry

    private lateinit var coreScopedDependencyRegistry: CoreScopedDependencyRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coreScopedDependencyRegistry = CoreScopedDependencyRegistry(dependencyRegistry)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `when asked to scope registry, should return itself if already scoped`() {
        val scoped = coreScopedDependencyRegistry.core()
        assertEquals(coreScopedDependencyRegistry, scoped)
    }

    @Test
    fun `when asked to scope registry, should use one already registered`() {
        every { dependencyRegistry.findScoped(CoreScopedDependencyRegistry::class) } returns coreScopedDependencyRegistry

        val scoped = dependencyRegistry.core()
        assertEquals(coreScopedDependencyRegistry, scoped)
    }

    @Test
    fun `when asked to scope registry and none is registered, should create new and register it`() {
        every { dependencyRegistry.findScoped(CoreScopedDependencyRegistry::class) } returns null

        val scoped = dependencyRegistry.core()
        verify { dependencyRegistry.addScoped(scoped) }
    }
}