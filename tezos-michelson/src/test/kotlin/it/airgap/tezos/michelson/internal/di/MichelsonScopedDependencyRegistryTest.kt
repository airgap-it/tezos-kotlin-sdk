package it.airgap.tezos.michelson.internal.di

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import it.airgap.tezos.core.internal.di.DependencyRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelsonScopedDependencyRegistryTest {

    @MockK(relaxed = true)
    private lateinit var dependencyRegistry: DependencyRegistry

    private lateinit var michelsonScopedDependencyRegistry: MichelsonScopedDependencyRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        michelsonScopedDependencyRegistry = MichelsonScopedDependencyRegistry(dependencyRegistry)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `when asked to scope registry, should return itself if already scoped`() {
        val scoped = michelsonScopedDependencyRegistry.michelson()
        assertEquals(michelsonScopedDependencyRegistry, scoped)
    }

    @Test
    fun `when asked to scope registry, should use one already registered`() {
        every { dependencyRegistry.findScoped(MichelsonScopedDependencyRegistry::class) } returns michelsonScopedDependencyRegistry

        val scoped = dependencyRegistry.michelson()
        assertEquals(michelsonScopedDependencyRegistry, scoped)
    }

    @Test
    fun `when asked to scope registry and none is registered, should create new and register it`() {
        every { dependencyRegistry.findScoped(MichelsonScopedDependencyRegistry::class) } returns null

        val scoped = dependencyRegistry.michelson()
        verify { dependencyRegistry.addScoped(scoped) }
    }
}