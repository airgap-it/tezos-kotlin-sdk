package it.airgap.tezos.core.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.TezosModule
import kotlin.reflect.KClass

public class TezosCoreModule private constructor(public val dependencyRegistry: CoreDependencyRegistry) : TezosModule {

    public class Builder : TezosModule.Builder<TezosCoreModule> {

        @InternalTezosSdkApi
        override val moduleDependencies: Set<KClass<out TezosModule>> = emptySet()

        @InternalTezosSdkApi
        override fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): TezosCoreModule =
            TezosCoreModule(CoreDependencyRegistry(dependencyRegistry))
    }
}

@InternalTezosSdkApi
public fun Tezos.core(): TezosCoreModule = moduleRegistry.findModule()
    ?: TezosCoreModule.Builder().build(dependencyRegistry, emptyList()).also { moduleRegistry.registerModule(it) }