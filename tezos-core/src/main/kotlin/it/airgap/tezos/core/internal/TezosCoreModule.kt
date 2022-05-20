package it.airgap.tezos.core.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.core.internal.module.TezosModule

public class TezosCoreModule private constructor(public val dependencyRegistry: CoreDependencyRegistry) : TezosModule {

    public class Builder : TezosModule.Builder<TezosCoreModule> {
        @InternalTezosSdkApi
        override fun build(dependencyRegistry: DependencyRegistry, moduleRegistry: ModuleRegistry): TezosCoreModule =
            TezosCoreModule(CoreDependencyRegistry(dependencyRegistry))
    }
}

@InternalTezosSdkApi
public val Tezos.coreModule: TezosCoreModule
    get() = moduleRegistry.coreModule(dependencyRegistry)

@InternalTezosSdkApi
public fun ModuleRegistry.coreModule(dependencyRegistry: DependencyRegistry): TezosCoreModule =
    module(dependencyRegistry) { TezosCoreModule.Builder() }