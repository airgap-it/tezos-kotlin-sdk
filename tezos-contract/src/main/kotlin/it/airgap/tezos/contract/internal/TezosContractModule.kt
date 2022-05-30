package it.airgap.tezos.contract.internal

import it.airgap.tezos.contract.internal.di.ContractDependencyRegistry
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.core.internal.module.TezosModule
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.rpc.internal.rpcModule

public val ContractModule: TezosContractModule.Builder
    get() = TezosContractModule.Builder()

public class TezosContractModule private constructor(public val dependencyRegistry: ContractDependencyRegistry) : TezosModule {

    public class Builder internal constructor() : TezosModule.Builder<TezosContractModule> {

        @InternalTezosSdkApi
        override fun build(dependencyRegistry: DependencyRegistry, moduleRegistry: ModuleRegistry): TezosContractModule {
            val core = moduleRegistry.coreModule(dependencyRegistry)
            val michelson = moduleRegistry.michelsonModule(dependencyRegistry)
            val rpc = moduleRegistry.rpcModule(dependencyRegistry)

            return TezosContractModule(
                ContractDependencyRegistry(
                    core.dependencyRegistry,
                    michelson.dependencyRegistry,
                    rpc.dependencyRegistry,
                ),
            )
        }
    }
}

@InternalTezosSdkApi
public val Tezos.contractModule: TezosContractModule
    get() = moduleRegistry.contractModule(dependencyRegistry)

public fun ModuleRegistry.contractModule(dependencyRegistry: DependencyRegistry): TezosContractModule =
    module(dependencyRegistry) { TezosContractModule.Builder() }