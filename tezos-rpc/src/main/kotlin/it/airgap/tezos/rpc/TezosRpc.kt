package it.airgap.tezos.rpc

import it.airgap.tezos.rpc.active.ActiveSimplifiedRpc
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
}