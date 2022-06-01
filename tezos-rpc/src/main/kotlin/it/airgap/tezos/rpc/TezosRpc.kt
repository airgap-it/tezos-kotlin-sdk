package it.airgap.tezos.rpc

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.rpc.active.ActiveSimplifiedRpc
import it.airgap.tezos.rpc.internal.estimator.FeeEstimator
import it.airgap.tezos.rpc.internal.rpcModule
import it.airgap.tezos.rpc.shell.ShellSimplifiedRpc
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.shell.config.Config
import it.airgap.tezos.rpc.shell.injection.Injection
import it.airgap.tezos.rpc.shell.monitor.Monitor
import it.airgap.tezos.rpc.shell.network.Network

public interface TezosRpc : ShellSimplifiedRpc, ActiveSimplifiedRpc, FeeEstimator<Operation> {
    public val chains: Chains
    public val config: Config
    public val injection: Injection
    public val monitor: Monitor
    public val network: Network
}

public fun TezosRpc(nodeUrl: String, tezos: Tezos = Tezos.Default): TezosRpc =
    tezos.rpcModule.dependencyRegistry.tezosRpc(nodeUrl.trimEnd('/'))