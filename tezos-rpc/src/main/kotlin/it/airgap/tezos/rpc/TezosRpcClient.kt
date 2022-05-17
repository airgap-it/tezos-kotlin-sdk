package it.airgap.tezos.rpc

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.applyLimits
import it.airgap.tezos.operation.type.Limits
import it.airgap.tezos.rpc.active.ActiveSimplifiedRpc
import it.airgap.tezos.rpc.converter.asOperation
import it.airgap.tezos.rpc.converter.asRunnable
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.CachedMap
import it.airgap.tezos.rpc.internal.utils.updateWith
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
    private val operationContentBytesCoder: ConsumingBytesCoder<OperationContent>,
) : TezosRpc, ShellSimplifiedRpc by shellRpc, ActiveSimplifiedRpc by activeRpc {

    private val chainIdCached: CachedMap<String, ChainId> = CachedMap { key, headers -> chains(key).chainId.get(headers).chainId }

    override suspend fun minFee(chainId: String, operation: Operation, limits: Limits, headers: List<HttpHeader>): Operation {
        val runnableOperation = operation
            .applyLimits(limits)
            .asRunnable(chainId.asChainId(headers))

        val runOperationResult = chains(chainId).blocks.head.helpers.scripts.runOperation.post(runnableOperation, headers)

        return runnableOperation
            .asOperation()
            .updateWith(runOperationResult.contents, operationContentBytesCoder)
    }

    private suspend fun String.asChainId(headers: List<HttpHeader> = emptyList()): ChainId =
        if (ChainId.isValid(this)) ChainId(this) else chainIdCached.get(this, headers)
}