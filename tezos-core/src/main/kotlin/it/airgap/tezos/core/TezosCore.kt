package it.airgap.tezos.core

import it.airgap.tezos.core.internal.di.CoreScopedDependencyRegistry
import it.airgap.tezos.core.internal.di.DependencyRegistryFactory

public class TezosCore : TezosSdk.Module {
    override val dependencyRegistryFactory: DependencyRegistryFactory
        get() = ::CoreScopedDependencyRegistry
}