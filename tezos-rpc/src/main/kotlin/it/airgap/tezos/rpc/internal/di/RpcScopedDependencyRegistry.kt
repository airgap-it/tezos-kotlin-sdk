package it.airgap.tezos.rpc.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.delegate.lazyWeak
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.di.findScoped
import it.airgap.tezos.core.internal.utils.failWithIllegalState
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.internal.http.HttpClient
import kotlinx.serialization.json.Json

internal class RpcScopedDependencyRegistry(
    httpClientProvider: HttpClientProvider,
    dependencyRegistry: DependencyRegistry,
) : ScopedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- serialization --

    override val json: Json by lazyWeak { Json { /* TODO: configuration */ } }

    // -- network --

    override val httpClient: HttpClient by lazyWeak {
        HttpClient(
            httpClientProvider,
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }
        )
    }
}

@InternalTezosSdkApi
public fun DependencyRegistry.rpc(): ScopedDependencyRegistry =
    if (this is ScopedDependencyRegistry) this
    else findScoped<RpcScopedDependencyRegistry>() ?: failWithIllegalState("Tezos RPC was module not initialized.")