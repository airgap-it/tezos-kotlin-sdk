package it.airgap.tezos.contract.internal.estimator

import it.airgap.tezos.operation.Operation
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.estimator.FeeEstimator

// -- Operation --

internal suspend fun Operation.Unsigned.withMinFee(
    operationFeeEstimator: FeeEstimator<Operation>,
    headers: List<HttpHeader> = emptyList(),
): Operation.Unsigned = operationFeeEstimator.minFee(operation = this, headers = headers).asUnsigned()

private fun Operation.asUnsigned(): Operation.Unsigned =
    when (this) {
        is Operation.Unsigned -> this
        is Operation.Signed -> Operation.Unsigned(branch, contents)
    }