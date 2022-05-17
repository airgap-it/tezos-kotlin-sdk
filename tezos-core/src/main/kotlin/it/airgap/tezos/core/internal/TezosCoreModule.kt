package it.airgap.tezos.core.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry

@InternalTezosSdkApi
public class TezosCoreModule(public val dependencyRegistry: CoreDependencyRegistry) : Tezos.DynamicModule

@InternalTezosSdkApi
public fun Tezos.core(): TezosCoreModule = findModule() ?: TezosCoreModule(
    CoreDependencyRegistry(
        dependencyRegistry,
    )
).also { registerDynamicModule(it) }