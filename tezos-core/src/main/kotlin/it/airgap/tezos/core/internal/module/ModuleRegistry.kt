package it.airgap.tezos.core.internal.module

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.DependencyRegistry
import kotlin.reflect.KClass

@InternalTezosSdkApi
public class ModuleRegistry(
    builders: Map<KClass<out TezosModule>, TezosModule.Builder<*>> = emptyMap(),
    modules: List<TezosModule> = emptyList(),
) {
    private val builders: MutableMap<String, TezosModule.Builder<*>> = mutableMapOf()
    private val modules: MutableMap<String, TezosModule> = mutableMapOf()

    init {
        builders.forEach { registerBuilder(it.value, it.key) }
        modules.forEach { registerModule(it) }
    }

    @InternalTezosSdkApi
    public inline fun <reified T : TezosModule> module(dependencyRegistry: DependencyRegistry, noinline createBuilder: () -> TezosModule.Builder<T>): T =
        module(T::class, dependencyRegistry, createBuilder)

    @PublishedApi
    internal fun <T : TezosModule> module(targetClass: KClass<T>, dependencyRegistry: DependencyRegistry, createBuilder: () -> TezosModule.Builder<T>): T =
        findModule(targetClass) ?: buildModule(targetClass, dependencyRegistry, createBuilder).also { registerModule(it) }

    private fun <T : TezosModule> buildModule(targetClass: KClass<T>, dependencyRegistry: DependencyRegistry, createBuilder: () -> TezosModule.Builder<T>): T {
        val builder = findBuilder(targetClass) ?: createBuilder()
        return builder.build(dependencyRegistry, this).also { removeBuilder(targetClass) }
    }

    private fun registerModule(module: TezosModule) {
        val key = module::class.qualifiedName ?: return
        modules[key] = module
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : TezosModule> findModule(targetClass: KClass<T>): T? {
        val key = targetClass.qualifiedName ?: return null
        return modules[key] as T?
    }

    private fun registerBuilder(builder: TezosModule.Builder<*>, moduleClass: KClass<out TezosModule>) {
        val key = moduleClass.qualifiedName ?: return
        builders[key] = builder
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : TezosModule> findBuilder(targetClass: KClass<T>): TezosModule.Builder<T>? {
        val key = targetClass.qualifiedName ?: return null
        return builders[key] as TezosModule.Builder<T>?
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : TezosModule> removeBuilder(targetClass: KClass<T>) {
        val key = targetClass.qualifiedName ?: return
        builders.remove(key)
    }
}