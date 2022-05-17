package it.airgap.tezos.operation.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.TezosCoreModule
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.TezosModule
import it.airgap.tezos.core.internal.utils.failWithModuleNotFound
import it.airgap.tezos.core.internal.utils.firstInstanceOfOrNull
import it.airgap.tezos.michelson.internal.TezosMichelsonModule
import it.airgap.tezos.michelson.internal.michelson
import it.airgap.tezos.operation.internal.di.OperationDependencyRegistry
import kotlin.reflect.KClass

public class TezosOperationModule private constructor(public val dependencyRegistry: OperationDependencyRegistry) : TezosModule {

    public class Builder : TezosModule.Builder<TezosOperationModule> {

        @InternalTezosSdkApi
        override val moduleDependencies: Set<KClass<out TezosModule>> = setOf(TezosCoreModule::class, TezosMichelsonModule::class)

        @InternalTezosSdkApi
        override fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): TezosOperationModule {
            val core = modules.firstInstanceOfOrNull<TezosCoreModule>() ?: failWithModuleNotFound(TezosCoreModule::class)
            val michelson = modules.firstInstanceOfOrNull<TezosMichelsonModule>() ?: failWithModuleNotFound(TezosMichelsonModule::class)

            return TezosOperationModule(
                OperationDependencyRegistry(
                    dependencyRegistry,
                    core.dependencyRegistry,
                    michelson.dependencyRegistry,
                )
            )
        }
    }
}

@InternalTezosSdkApi
public fun Tezos.operation(): TezosOperationModule = moduleRegistry.findModule()
    ?: TezosOperationModule.Builder().build(dependencyRegistry, listOf(core(), michelson())).also { moduleRegistry.registerModule(it) }