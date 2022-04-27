package it.airgap.tezos.rpc

import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.applyLimits
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.type.FeeLimits
import it.airgap.tezos.rpc.active.ActiveSimplifiedRpc
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cache
import it.airgap.tezos.rpc.internal.utils.withFeeFrom
import it.airgap.tezos.rpc.shell.ShellSimplifiedRpc
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.shell.config.Config
import it.airgap.tezos.rpc.shell.injection.Injection
import it.airgap.tezos.rpc.shell.monitor.Monitor
import it.airgap.tezos.rpc.shell.network.Network

internal class TezosRpcClient(
    shellRpc: ShellSimplifiedRpc,
    activeRpc: ActiveSimplifiedRpc,
    override val chains: Chains,
    override val config: Config,
    override val injection: Injection,
    override val monitor: Monitor,
    override val network: Network,
    private val operationContentBytesCoder: OperationContentBytesCoder,
) : TezosRpc, ShellSimplifiedRpc by shellRpc, ActiveSimplifiedRpc by activeRpc {

    private val chainIdCache: Cache<String, ChainId> = Cache { key, headers -> chains(key).chainId.get(headers).chainId }

    override suspend fun fee(chainId: String, operation: Operation, limits: FeeLimits, headers: List<HttpHeader>): Operation {
        val runnableOperation = operation
            .applyLimits(limits)
            .asRunnable(chainId.asChainId(headers))

        val runOperationResult = chains(chainId).blocks.head.helpers.scripts.runOperation.post(runnableOperation, headers)

        return runnableOperation
            .asOperation()
            .withFeeFrom(runOperationResult.contents, operationContentBytesCoder)
    }

    private suspend fun String.asChainId(headers: List<HttpHeader> = emptyList()): ChainId =
        if (ChainId.isValid(this)) ChainId(this) else chainIdCache.get(this, headers)
}