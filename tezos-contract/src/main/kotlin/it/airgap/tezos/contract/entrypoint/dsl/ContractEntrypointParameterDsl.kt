package it.airgap.tezos.contract.entrypoint.dsl

import it.airgap.tezos.contract.entrypoint.ContractEntrypoint
import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.contract.entrypoint.dsl.builder.ContractEntrypointObjectParameterBuilder
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.type.limits.OperationLimits

public suspend fun ContractEntrypoint.call(
    source: ImplicitAddress,
    branch: BlockHash? = null,
    fee: Mutez? = null,
    counter: String? = null,
    limits: OperationLimits? = null,
    amount: Mutez? = null,
    headers: List<HttpHeader> = emptyList(),
    parametersBuilder: ContractEntrypointObjectParameterBuilder.() -> Unit = {},
): Operation.Unsigned {
    val parameters = ContractEntrypointObjectParameterBuilder().apply(parametersBuilder).build()
    return call(parameters, source, branch, fee, counter, limits, amount, headers)
}

internal fun entrypointParameters(builderAction: ContractEntrypointObjectParameterBuilder.() -> Unit = {}): ContractEntrypointParameter =
    ContractEntrypointObjectParameterBuilder().apply(builderAction).build()