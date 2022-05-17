package it.airgap.tezos.core.internal.module

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.utils.firstInstanceOfOrNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModuleResolverTest {

    @MockK
    private lateinit var dependencyRegistry: DependencyRegistry

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should build list of modules`() {
        val builders: Map<KClass<out TezosModule>, TezosModule.Builder<*>> = listOf(
            A::class to spyk(A.Builder()),
            B::class to spyk(B.Builder()),
            C::class to spyk(C.Builder()),
            D::class to spyk(D.Builder()),
        ).shuffled().toMap()

        val moduleResolver = ModuleResolver()
        val modules = with(moduleResolver) {
            builders.build(dependencyRegistry)
        }.filterIsInstance<TestModule>()

        assertEquals(listOf(A(), B(), C(), D()).sortedByName().map { it::class }, modules.sortedByName().map { it::class })

        builders.forEach { (_, builder) ->
            verify(exactly = 1) { builder.build(any(), any()) }
        }
    }

    @Test
    fun `should build list of modules using prebuilt list`() {
        val prebuilt = listOf(B())
        val builders: Map<KClass<out TezosModule>, TezosModule.Builder<*>> = listOf(
            A::class to spyk(A.Builder()),
            C::class to spyk(C.Builder()),
            D::class to spyk(D.Builder()),
        ).shuffled().toMap()

        val moduleResolver = ModuleResolver(prebuilt)
        val modules = with(moduleResolver) {
            builders.build(dependencyRegistry)
        }.filterIsInstance<TestModule>()

        assertEquals(listOf(A(), B(), C(), D()).sortedByName().map { it::class }, modules.sortedByName().map { it::class })
        assertTrue(modules.containsAll(prebuilt))

        builders.forEach { (_, builder) ->
            verify(exactly = 1) { builder.build(any(), any()) }
        }
    }

    @Test
    fun `should prefer builder over prebuilt module`() {
        val prebuilt = listOf(B())
        val builders: Map<KClass<out TezosModule>, TezosModule.Builder<*>> = listOf(
            A::class to spyk(A.Builder()),
            B::class to spyk(B.Builder()),
            C::class to spyk(C.Builder()),
            D::class to spyk(D.Builder()),
        ).shuffled().toMap()

        val moduleResolver = ModuleResolver(prebuilt)
        val modules = with(moduleResolver) {
            builders.build(dependencyRegistry)
        }.filterIsInstance<TestModule>()

        assertEquals(listOf(A(), B(), C(), D()).sortedByName().map { it::class }, modules.sortedByName().map { it::class })
        assertFalse(modules.containsAll(prebuilt.filter { builders.containsKey(it::class) }))

        builders.forEach { (_, builder) ->
            verify(exactly = 1) { builder.build(any(), any()) }
        }
    }

    @Test
    fun `should fail on cyclic dependency`() {
        val builders: Map<KClass<out TezosModule>, TezosModule.Builder<*>> = listOf(
            CyclicA::class to spyk(CyclicA.Builder()),
            CyclicB::class to spyk(CyclicB.Builder()),
        ).shuffled().toMap()

        val moduleResolver = ModuleResolver()

        assertFailsWith<java.lang.IllegalStateException> {
            with(moduleResolver) {
                builders.build(dependencyRegistry)
            }
        }
    }

    private fun List<TestModule>.sortedByName(): List<TestModule> = sortedBy { it.name}

    private class A : TestModule() {
        override val name: String = "A"

        class Builder : TezosModule.Builder<A> {
            @InternalTezosSdkApi
            override val moduleDependencies: Set<KClass<out TezosModule>> = emptySet()

            @InternalTezosSdkApi
            override fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): A = A()
        }
    }

    private class B : TestModule() {
        override val name: String = "B"

        class Builder : TezosModule.Builder<B> {
            @InternalTezosSdkApi
            override val moduleDependencies: Set<KClass<out TezosModule>> = setOf(A::class)

            @InternalTezosSdkApi
            override fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): B {
                modules.firstInstanceOfOrNull<A>() ?: error("Dependency not found.")

                return B()
            }
        }
    }

    private class C : TestModule() {
        override val name: String = "C"

        class Builder : TezosModule.Builder<C> {
            @InternalTezosSdkApi
            override val moduleDependencies: Set<KClass<out TezosModule>> = setOf(A::class, B::class)

            @InternalTezosSdkApi
            override fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): C {
                modules.firstInstanceOfOrNull<A>() ?: error("Dependency not found.")
                modules.firstInstanceOfOrNull<B>() ?: error("Dependency not found.")

                return C()
            }
        }
    }

    private class D : TestModule() {
        override val name: String = "D"

        class Builder : TezosModule.Builder<D> {
            @InternalTezosSdkApi
            override val moduleDependencies: Set<KClass<out TezosModule>> = setOf(B::class, C::class)

            @InternalTezosSdkApi
            override fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): D {
                modules.firstInstanceOfOrNull<B>() ?: error("Dependency not found.")
                modules.firstInstanceOfOrNull<C>() ?: error("Dependency not found.")

                return D()
            }
        }
    }

    private class CyclicA : TestModule() {
        override val name: String = "CyclicA"

        class Builder : TezosModule.Builder<CyclicA> {
            @InternalTezosSdkApi
            override val moduleDependencies: Set<KClass<out TezosModule>> = setOf(CyclicB::class)

            @InternalTezosSdkApi
            override fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): CyclicA {
                modules.firstInstanceOfOrNull<CyclicB>() ?: error("Dependency not found.")

                return CyclicA()
            }
        }
    }

    private class CyclicB : TestModule() {
        override val name: String = "CyclicB"

        class Builder : TezosModule.Builder<CyclicB> {
            @InternalTezosSdkApi
            override val moduleDependencies: Set<KClass<out TezosModule>> = setOf(CyclicA::class)

            @InternalTezosSdkApi
            override fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): CyclicB {
                modules.firstInstanceOfOrNull<CyclicA>() ?: error("Dependency not found.")

                return CyclicB()
            }
        }
    }

    private abstract class TestModule : TezosModule {
        abstract val name: String
    }
}