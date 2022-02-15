package it.airgap.tezos.rpc.internal.di

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.rpc.internal.network.HttpClient

public interface ScopedDependencyRegistry : DependencyRegistry {

    // -- network --

    public val httpClient: HttpClient
}