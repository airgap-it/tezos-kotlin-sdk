package it.airgap.tezos.rpc.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.core.internal.module.TezosModule
import it.airgap.tezos.operation.internal.operationModule
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.internal.context.TezosRpcContext.default
import it.airgap.tezos.rpc.internal.context.TezosRpcContext.failWithDependencyNotFound
import it.airgap.tezos.rpc.internal.di.RpcDependencyRegistry
import java.util.*

public val RpcModule: TezosRpcModule.Builder
    get() = TezosRpcModule.Builder()

public class TezosRpcModule private constructor(public val dependencyRegistry: RpcDependencyRegistry) : TezosModule {

    private object Static {
        val defaultHttpClientProvider: HttpClientProvider
            get() = httpClientProviders.firstOrNull() ?: failWithDependencyNotFound("HttpClientProvider", "tezos-http")

        private val httpClientProviders: List<HttpClientProvider> = HttpClientProvider::class.java.let {
            ServiceLoader.load(it, it.classLoader).toList()
        }
    }

    public class Builder internal constructor() : TezosModule.Builder<TezosRpcModule> {
        public var httpClientProvider: HttpClientProvider by default { Static.defaultHttpClientProvider }

        @InternalTezosSdkApi
        override fun build(dependencyRegistry: DependencyRegistry, moduleRegistry: ModuleRegistry): TezosRpcModule {
            val core = moduleRegistry.coreModule(dependencyRegistry)
            val operation = moduleRegistry.operationModule(dependencyRegistry)

            return TezosRpcModule(
                RpcDependencyRegistry(
                    httpClientProvider,
                    core.dependencyRegistry,
                    operation.dependencyRegistry,
                ),
            )
        }
    }
}

@InternalTezosSdkApi
public val Tezos.rpcModule: TezosRpcModule
    get() = moduleRegistry.rpcModule(dependencyRegistry)

@InternalTezosSdkApi
public fun ModuleRegistry.rpcModule(dependencyRegistry: DependencyRegistry): TezosRpcModule =
    module(dependencyRegistry) { TezosRpcModule.Builder() }