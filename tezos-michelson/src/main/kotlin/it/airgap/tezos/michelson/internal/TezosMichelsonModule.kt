package it.airgap.tezos.michelson.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.michelson.internal.di.MichelsonDependencyRegistry

@InternalTezosSdkApi
public class TezosMichelsonModule(public val dependencyRegistry: MichelsonDependencyRegistry) : Tezos.DynamicModule

@InternalTezosSdkApi
public fun Tezos.michelson(): TezosMichelsonModule = findModule() ?: TezosMichelsonModule(
    MichelsonDependencyRegistry(
        core().dependencyRegistry,
    )
).also { registerDynamicModule(it) }