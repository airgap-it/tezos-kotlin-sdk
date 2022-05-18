package it.airgap.tezos.michelson.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.core.internal.module.TezosModule
import it.airgap.tezos.michelson.internal.di.MichelsonDependencyRegistry

public class TezosMichelsonModule private constructor(public val dependencyRegistry: MichelsonDependencyRegistry) : TezosModule {

    public class Builder : TezosModule.Builder<TezosMichelsonModule> {
        @InternalTezosSdkApi
        override fun build(dependencyRegistry: DependencyRegistry, moduleRegistry: ModuleRegistry): TezosMichelsonModule {
            val core = moduleRegistry.coreModule(dependencyRegistry)

            return TezosMichelsonModule(MichelsonDependencyRegistry(core.dependencyRegistry))
        }
    }
}

@InternalTezosSdkApi
public val Tezos.michelsonModule: TezosMichelsonModule
    get() = moduleRegistry.michelsonModule(dependencyRegistry)

@InternalTezosSdkApi
public fun ModuleRegistry.michelsonModule(dependencyRegistry: DependencyRegistry): TezosMichelsonModule =
    module(dependencyRegistry) { TezosMichelsonModule.Builder() }