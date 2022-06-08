package it.airgap.tezos.core.internal.module

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.DependencyRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModuleRegistryTest {

    @MockK
    private lateinit var dependencyRegistry: DependencyRegistry

    private lateinit var moduleRegistry: ModuleRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should return already built module`() {
        val module = TestModule()
        moduleRegistry = ModuleRegistry(modules = listOf(module))

        var defaultBuilderCalled = false
        val found = moduleRegistry.module(dependencyRegistry) {
            defaultBuilderCalled = true
            TestModule.Builder()
        }

        assertEquals(module, found, "Expected found module to be the same as the module registry was initialized with.")
        assertFalse(defaultBuilderCalled, "Expected the module builder not to be created.")
    }

    @Test
    fun `should build module using registered builder`() {
        val builder = spyk(TestModule.Builder())
        moduleRegistry = ModuleRegistry(builders = mapOf(TestModule::class to builder))

        var defaultBuilderCalled = false
        moduleRegistry.module(dependencyRegistry) {
            defaultBuilderCalled = true
            TestModule.Builder()
        }

        verify(exactly = 1) { builder.build(dependencyRegistry, moduleRegistry) }
        assertFalse(defaultBuilderCalled, "Expected the module builder not to be created.")
    }

    @Test
    fun `should build module using provided builder creator`() {
        val builder = spyk(TestModule.Builder())
        moduleRegistry = ModuleRegistry()

        var defaultBuilderCalled = false
        moduleRegistry.module(dependencyRegistry) {
            defaultBuilderCalled = true
            builder
        }

        verify(exactly = 1) { builder.build(dependencyRegistry, moduleRegistry) }
        assertTrue(defaultBuilderCalled, "Expected the module builder to be created.")
    }

    private class TestModule : TezosModule {
        class Builder : TezosModule.Builder<TestModule> {
            @InternalTezosSdkApi
            override fun build(dependencyRegistry: DependencyRegistry, moduleRegistry: ModuleRegistry): TestModule =
                TestModule()
        }
    }
}