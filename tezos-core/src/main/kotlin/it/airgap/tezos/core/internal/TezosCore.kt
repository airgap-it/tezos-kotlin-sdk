package it.airgap.tezos.core.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry

@InternalTezosSdkApi
public class TezosCore(public val dependencyRegistry: CoreDependencyRegistry) : Tezos.DynamicModule

@InternalTezosSdkApi
public fun Tezos.core(): TezosCore = findModule() ?: TezosCore(CoreDependencyRegistry(dependencyRegistry)).also { registerDynamicModule(it) }