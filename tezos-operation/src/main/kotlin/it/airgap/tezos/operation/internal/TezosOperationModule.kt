package it.airgap.tezos.operation.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.core.internal.module.TezosModule
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.operation.internal.di.OperationDependencyRegistry

public class TezosOperationModule private constructor(public val dependencyRegistry: OperationDependencyRegistry) : TezosModule {

    public class Builder : TezosModule.Builder<TezosOperationModule> {

        @InternalTezosSdkApi
        override fun build(dependencyRegistry: DependencyRegistry, moduleRegistry: ModuleRegistry): TezosOperationModule {
            val core = moduleRegistry.coreModule(dependencyRegistry)
            val michelson = moduleRegistry.michelsonModule(dependencyRegistry)

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
public val Tezos.operationModule: TezosOperationModule
    get() = moduleRegistry.operationModule(dependencyRegistry)

@InternalTezosSdkApi
public fun ModuleRegistry.operationModule(dependencyRegistry: DependencyRegistry): TezosOperationModule =
    module(dependencyRegistry) { TezosOperationModule.Builder() }