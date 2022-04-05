package it.airgap.tezos.operation

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.core.internal.di.DependencyRegistryFactory
import it.airgap.tezos.operation.internal.di.OperationScopedDependencyRegistry

public class TezosOperationModule : TezosSdk.Module {
    override val dependencyRegistryFactory: DependencyRegistryFactory
        get() = ::OperationScopedDependencyRegistry
}