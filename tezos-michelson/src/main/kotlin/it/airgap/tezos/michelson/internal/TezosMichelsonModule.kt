package it.airgap.tezos.michelson.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.TezosCoreModule
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.TezosModule
import it.airgap.tezos.core.internal.utils.failWithModuleNotFound
import it.airgap.tezos.core.internal.utils.firstInstanceOfOrNull
import it.airgap.tezos.michelson.internal.di.MichelsonDependencyRegistry
import kotlin.reflect.KClass

public class TezosMichelsonModule private constructor(public val dependencyRegistry: MichelsonDependencyRegistry) : TezosModule {

    public class Builder : TezosModule.Builder<TezosMichelsonModule> {

        @InternalTezosSdkApi
        override val moduleDependencies: Set<KClass<out TezosModule>> = setOf(TezosCoreModule::class)

        @InternalTezosSdkApi
        override fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): TezosMichelsonModule {
            val core = modules.firstInstanceOfOrNull<TezosCoreModule>() ?: failWithModuleNotFound(TezosCoreModule::class)

            return TezosMichelsonModule(MichelsonDependencyRegistry(core.dependencyRegistry))
        }
    }
}

@InternalTezosSdkApi
public fun Tezos.michelson(): TezosMichelsonModule = moduleRegistry.findModule()
    ?: TezosMichelsonModule.Builder().build(dependencyRegistry, listOf(core())).also { moduleRegistry.registerModule(it) }