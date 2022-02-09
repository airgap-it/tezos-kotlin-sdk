package it.airgap.tezos.operation.internal.di

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

class OperationScopedDependencyRegistryTest {

    @MockK(relaxed = true)
    private lateinit var dependencyRegistry: DependencyRegistry

    private lateinit var operationScopedDependencyRegistry: OperationScopedDependencyRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        operationScopedDependencyRegistry = OperationScopedDependencyRegistry(dependencyRegistry)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `when asked to scope registry, should return itself if already scoped`() {
        val scoped = operationScopedDependencyRegistry.operation()
        assertEquals(operationScopedDependencyRegistry, scoped)
    }

    @Test
    fun `when asked to scope registry, should use one already registered`() {
        every { dependencyRegistry.findScoped(OperationScopedDependencyRegistry::class) } returns operationScopedDependencyRegistry

        val scoped = dependencyRegistry.operation()
        assertEquals(operationScopedDependencyRegistry, scoped)
    }

    @Test
    fun `when asked to scope registry and none is registered, should create new and register it`() {
        every { dependencyRegistry.findScoped(OperationScopedDependencyRegistry::class) } returns null

        val scoped = dependencyRegistry.operation()
        verify { dependencyRegistry.addScoped(scoped) }
    }
}