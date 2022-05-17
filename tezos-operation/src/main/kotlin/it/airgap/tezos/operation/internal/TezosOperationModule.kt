package it.airgap.tezos.operation.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.michelson.internal.michelson
import it.airgap.tezos.operation.internal.di.OperationDependencyRegistry

@InternalTezosSdkApi
public class TezosOperationModule(public val dependencyRegistry: OperationDependencyRegistry) : Tezos.DynamicModule

@InternalTezosSdkApi
public fun Tezos.operation(): TezosOperationModule = findModule() ?: TezosOperationModule(
    OperationDependencyRegistry(
        dependencyRegistry,
        core().dependencyRegistry,
        michelson().dependencyRegistry,
    )
).also { registerDynamicModule(it) }