package it.airgap.tezos.rpc.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.delegate.default
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.TezosModule
import it.airgap.tezos.core.internal.utils.failWithDependencyNotFound
import it.airgap.tezos.operation.internal.TezosOperationModule
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.internal.di.RpcDependencyRegistry
import java.util.*
import kotlin.reflect.KClass

public class TezosRpcModule private constructor(public val dependencyRegistry: RpcDependencyRegistry) : TezosModule {

    private object Static {
        val defaultHttpClientProvider: HttpClientProvider
            get() = httpClientProviders.firstOrNull() ?: failWithDependencyNotFound("HttpClientProvider", "tezos-http")

        private val httpClientProviders: List<HttpClientProvider> = HttpClientProvider::class.java.let {
            ServiceLoader.load(it, it.classLoader).toList()
        }
    }

    public class Builder : TezosModule.Builder<TezosRpcModule> {
        public var httpClientProvider: HttpClientProvider by default { Static.defaultHttpClientProvider }

        @InternalTezosSdkApi
        override val moduleDependencies: Set<KClass<out TezosModule>> = setOf(TezosOperationModule::class)

        @InternalTezosSdkApi
        override fun build(dependencyRegistry: DependencyRegistry, modules: List<TezosModule>): TezosRpcModule =
            TezosRpcModule(
                RpcDependencyRegistry(
                    httpClientProvider,
                ),
            )
    }
}

public var Tezos.Builder.httpClientProvider: HttpClientProvider
    get() = moduleBuilders.getOrPut { TezosRpcModule.Builder() }.httpClientProvider
    set(value) {
        val builder = moduleBuilders.getOrPut { TezosRpcModule.Builder() }
        builder.httpClientProvider = value
    }

@Suppress("UNCHECKED_CAST")
private inline fun <reified T : TezosModule, B : TezosModule.Builder<T>> MutableMap<KClass<out TezosModule>, TezosModule.Builder<*>>.getOrPut(defaultValue: () -> B): B {
    val key = T::class
    return this[key] as? B ?: defaultValue().also { put(key, it) }
}

@InternalTezosSdkApi
public fun Tezos.rpc(): TezosRpcModule = moduleRegistry.findModule()
    ?: TezosRpcModule.Builder().build(dependencyRegistry, emptyList()).also { moduleRegistry.registerModule(it) }