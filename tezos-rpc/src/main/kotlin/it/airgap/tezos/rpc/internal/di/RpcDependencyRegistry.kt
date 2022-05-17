package it.airgap.tezos.rpc.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.delegate.lazyWeak
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.internal.serializer.rpcJson
import kotlinx.serialization.json.Json

@InternalTezosSdkApi
public class RpcDependencyRegistry(httpClientProvider: HttpClientProvider) {

    // -- serialization --

    public val json: Json by lazyWeak {
        Json(from = rpcJson) {
            prettyPrint = false
        }
    }

    // -- network --

    public val httpClient: HttpClient by lazyWeak { HttpClient(httpClientProvider, json) }
}
