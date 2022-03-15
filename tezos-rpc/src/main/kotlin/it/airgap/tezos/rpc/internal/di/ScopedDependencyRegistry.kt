package it.airgap.tezos.rpc.internal.di

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.rpc.internal.http.HttpClient
import kotlinx.serialization.json.Json

public interface ScopedDependencyRegistry : DependencyRegistry {

    // -- serialization --

    public val json: Json

    // -- network --

    public val httpClient: HttpClient
}