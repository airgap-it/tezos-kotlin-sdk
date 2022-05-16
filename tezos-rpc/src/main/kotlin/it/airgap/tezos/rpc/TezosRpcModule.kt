package it.airgap.tezos.rpc

import it.airgap.tezos.core.internal.di.DependencyRegistryFactory
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.internal.di.RpcScopedDependencyRegistry

public class TezosRpcModule(private val httpClientProvider: HttpClientProvider) : TezosSdk.Module {
    override val dependencyRegistryFactory: DependencyRegistryFactory
        get() = { RpcScopedDependencyRegistry(httpClientProvider, it) }
}