package it.airgap.tezos.rpc

import it.airgap.tezos.operation.Operation
import it.airgap.tezos.rpc.active.ActiveSimplifiedRpc
import it.airgap.tezos.rpc.internal.estimator.FeeEstimator
import it.airgap.tezos.rpc.shell.ShellSimplifiedRpc
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.shell.config.Config
import it.airgap.tezos.rpc.shell.injection.Injection
import it.airgap.tezos.rpc.shell.monitor.Monitor
import it.airgap.tezos.rpc.shell.network.Network

internal class TezosRpcClient(
    shellRpc: ShellSimplifiedRpc,
    activeRpc: ActiveSimplifiedRpc,
    operationFeeEstimator: FeeEstimator<Operation>,
    override val chains: Chains,
    override val config: Config,
    override val injection: Injection,
    override val monitor: Monitor,
    override val network: Network,
) : TezosRpc, ShellSimplifiedRpc by shellRpc, ActiveSimplifiedRpc by activeRpc, FeeEstimator<Operation> by operationFeeEstimator