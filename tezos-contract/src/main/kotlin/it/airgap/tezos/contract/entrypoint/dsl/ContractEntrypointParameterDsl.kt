package it.airgap.tezos.contract.entrypoint.dsl

import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.contract.entrypoint.dsl.builder.ContractEntrypointObjectParameterBuilder
import it.airgap.tezos.contract.internal.context.withTezosContext
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.Micheline

/**
 * Creates [ContractEntrypointParameter] using the [parameter builder configuration][createObject].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun entrypointParameters(tezos: Tezos = Tezos.Default, createObject: ContractEntrypointObjectParameterBuilder.() -> Unit = {}): ContractEntrypointParameter = withTezosContext {
    entrypointParameters(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, createObject)
}

@InternalTezosSdkApi
public interface ContractEntrypointParameterDslContext {
    public fun entrypointParameters(
        michelsonToMichelineConverter: Converter<Michelson, Micheline>,
        createObject: ContractEntrypointObjectParameterBuilder.() -> Unit = {},
    ): ContractEntrypointParameter = ContractEntrypointObjectParameterBuilder(michelsonToMichelineConverter).apply(createObject).build()
}