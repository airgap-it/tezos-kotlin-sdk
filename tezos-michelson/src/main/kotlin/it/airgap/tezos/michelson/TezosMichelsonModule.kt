package it.airgap.tezos.michelson

import it.airgap.tezos.core.internal.di.DependencyRegistryFactory
import it.airgap.tezos.michelson.internal.di.MichelsonScopedDependencyRegistry

public class TezosMichelsonModule : TezosSdk.Module {
    override val dependencyRegistryFactory: DependencyRegistryFactory
        get() = ::MichelsonScopedDependencyRegistry
}