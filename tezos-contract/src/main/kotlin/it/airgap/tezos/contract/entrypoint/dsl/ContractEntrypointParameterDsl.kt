package it.airgap.tezos.contract.entrypoint.dsl

import it.airgap.tezos.contract.entrypoint.ContractEntrypoint
import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.contract.entrypoint.dsl.builder.ContractEntrypointObjectParameterBuilder
import it.airgap.tezos.contract.internal.context.withTezosContext
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.type.limits.OperationLimits

/**
 * Creates [ContractEntrypointParameter] using the [parameter builder configuration][createObject].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun entrypointParameters(tezos: Tezos = Tezos.Default, createObject: ContractEntrypointObjectParameterBuilder.() -> Unit = {}): ContractEntrypointParameter = withTezosContext {
    entrypointParameters(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, createObject)
}

/**
 * Creates an unsigned contract entrypoint call from the [source] address.
 * Configures the resulting [Operation.Unsigned] with optional [branch], [fee], [counter], [operation limits][limits],
 * [amount], [HTTP headers][headers] and [parameters][setParameters].
 */
public suspend fun ContractEntrypoint.call(
    source: ImplicitAddress,
    branch: BlockHash? = null,
    fee: Mutez? = null,
    counter: String? = null,
    limits: OperationLimits? = null,
    amount: Mutez? = null,
    headers: List<HttpHeader> = emptyList(),
    setParameters: ContractEntrypointObjectParameterBuilder.() -> Unit = {},
): Operation.Unsigned = withTezosContext {
    val parameters = entrypointParameters(michelsonToMichelineConverter, setParameters)
    return call(parameters, source, branch, fee, counter, limits, amount, headers)
}

@InternalTezosSdkApi
public interface ContractEntrypointParameterDslContext {
    public fun entrypointParameters(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        createObject: ContractEntrypointObjectParameterBuilder.() -> Unit = {},
    ): ContractEntrypointParameter = ContractEntrypointObjectParameterBuilder(michelsonToMichelineConverter).apply(createObject).build()
}