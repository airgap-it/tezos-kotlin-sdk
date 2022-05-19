package it.airgap.tezos.rpc.internal.estimator

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants
import it.airgap.tezos.rpc.type.limits.Limits

@InternalTezosSdkApi
public interface FeeEstimator<T> {
    public suspend fun minFee(
        chainId: String = Constants.Chain.MAIN,
        operation: Operation,
        limits: Limits = Limits(),
        headers: List<HttpHeader> = emptyList(),
    ): Operation

    public suspend fun minFee(
        chainId: ChainId,
        operation: Operation,
        limits: Limits = Limits(),
        headers: List<HttpHeader> = emptyList(),
    ): Operation = minFee(chainId.base58, operation, limits, headers)
}