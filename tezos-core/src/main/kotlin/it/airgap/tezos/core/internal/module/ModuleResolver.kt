package it.airgap.tezos.core.internal.module

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.utils.flatten
import kotlin.reflect.KClass

internal class ModuleResolver(private val modules: List<TezosModule> = emptyList()) {

    fun Map<KClass<out TezosModule>, TezosModule.Builder<*>>.build(dependencyRegistry: DependencyRegistry): List<TezosModule> {
        val distinctModules = modules.associateBy { it::class }.filterNot { containsKey(it.key) }
        val builtModules = buildModules(dependencyRegistry, this, distinctModules)

        return builtModules.values.toList()
    }

    private fun buildModules(
        dependencyRegistry: DependencyRegistry,
        builders: Map<KClass<out TezosModule>, TezosModule.Builder<*>>,
        modules: Map<KClass<out TezosModule>, TezosModule>,
    ): Map<KClass<out TezosModule>, TezosModule> {
        if (builders.isEmpty()) return modules

        val (head, tail) = builders.deconstruct()
        val (updatedBuilders, updatedModules) = buildModule(head.value, head.key, dependencyRegistry, tail, modules)

        return buildModules(dependencyRegistry, updatedBuilders, updatedModules)
    }

    private fun buildModule(
        builder: TezosModule.Builder<*>,
        moduleClass: KClass<out TezosModule>,
        dependencyRegistry: DependencyRegistry,
        otherBuilders: Map<KClass<out TezosModule>, TezosModule.Builder<*>>,
        otherModules: Map<KClass<out TezosModule>, TezosModule>,
    ): Pair<Map<KClass<out TezosModule>, TezosModule.Builder<*>>, Map<KClass<out TezosModule>, TezosModule>> {
        if (builder.moduleDependencies.isEmpty()) {
            return Pair(otherBuilders, otherModules + Pair(moduleClass, builder.build(dependencyRegistry, emptyList())))
        }

        val builderDependencies = otherBuilders.dependenciesOf(builder).apply { assertNoCycles(moduleClass) }
        val moduleDependencies = otherModules.dependenciesOf(builder).filterNot { builderDependencies.contains(it.key) }

        val dependenciesCount = (moduleDependencies.map { it::class } + builderDependencies.keys).size
        if (dependenciesCount < builder.moduleDependencies.size) failWithMissingDependencies(moduleClass)

        val updatedModules = if (moduleDependencies.size == builder.moduleDependencies.size) {
            val module = builder.build(dependencyRegistry, moduleDependencies.values.toList())

            otherModules + Pair(moduleClass, module)
        } else {
            val builtModules = buildModules(dependencyRegistry, builderDependencies, otherModules)
            val module = builder.build(dependencyRegistry, builtModules.values.toList())

            builtModules + Pair(moduleClass, module)
        }
        val updatedBuilders = otherBuilders.filterNot { updatedModules.containsKey(it.key) }

        return Pair(updatedBuilders, updatedModules)
    }

    private fun <K, V> Map<K, V>.deconstruct(): Pair<Map.Entry<K, V>, Map<K, V>> {
        val head = entries.first()
        val tail = this - head.key

        return Pair(head, tail)
    }

    @JvmName("moduleDependenciesOf")
    private fun Map<KClass<out TezosModule>, TezosModule>.dependenciesOf(builder: TezosModule.Builder<*>): Map<KClass<out TezosModule>, TezosModule> =
        filterKeys { builder.moduleDependencies.contains(it) }

    @JvmName("builderDependenciesOf")
    private fun Map<KClass<out TezosModule>, TezosModule.Builder<*>>.dependenciesOf(builder: TezosModule.Builder<*>): Map<KClass<out TezosModule>, TezosModule.Builder<*>> {
        if (builder.moduleDependencies.isEmpty()) return emptyMap()

        val directDependencies = filterKeys { builder.moduleDependencies.contains(it) }
        return (directDependencies.map { dependenciesOf(it.value) } + directDependencies).flatten()
    }

    private fun Map<KClass<out TezosModule>, TezosModule.Builder<*>>.assertNoCycles(targetClass: KClass<out TezosModule>) {
        val found = entries.firstOrNull { it.value.moduleDependencies.contains(targetClass) }?.key ?: return
        failWithCycleDependency(targetClass, found)
    }

    private fun failWithCycleDependency(first: KClass<out TezosModule>, second: KClass<out TezosModule>): Nothing =
        error("A cycle dependency between module ${first.qualifiedName} and ${second.qualifiedName} has been found.")

    private fun failWithMissingDependencies(moduleClass: KClass<out TezosModule>): Nothing =
        error("Some dependencies of module ${moduleClass.qualifiedName} could not be found.")
}

internal fun <T> withModuleResolver(action: ModuleResolver.() -> T): T = with(ModuleResolver(), action)