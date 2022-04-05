package it.airgap.tezos.rpc

import it.airgap.tezos.rpc.active.ActiveRpc
import it.airgap.tezos.rpc.shell.ShellRpc

internal class TezosRpcClient(
    shellRpc: ShellRpc,
    activeRpc: ActiveRpc,
) : TezosRpc, ShellRpc by shellRpc, ActiveRpc by activeRpc