package it.airgap.tezos.rpc

import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.type.FeeLimits
import it.airgap.tezos.rpc.active.ActiveSimplifiedRpc
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants
import it.airgap.tezos.rpc.shell.ShellSimplifiedRpc
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.shell.config.Config
import it.airgap.tezos.rpc.shell.injection.Injection
import it.airgap.tezos.rpc.shell.monitor.Monitor
import it.airgap.tezos.rpc.shell.network.Network

public interface TezosRpc : ShellSimplifiedRpc, ActiveSimplifiedRpc {
    public val chains: Chains
    public val config: Config
    public val injection: Injection
    public val monitor: Monitor
    public val network: Network

    // -- fee --

    public suspend fun fee(
        chainId: String = Constants.Chain.MAIN,
        operation: Operation,
        limits: FeeLimits = FeeLimits(),
        headers: List<HttpHeader> = emptyList(),
    ): Operation

    public suspend fun fee(
        chainId: ChainId,
        operation: Operation,
        limits: FeeLimits = FeeLimits(),
        headers: List<HttpHeader> = emptyList(),
    ): Operation = fee(chainId.base58, operation, limits, headers)
}