package it.airgap.tezos.rpc

import it.airgap.tezos.rpc.active.ActiveRpc
import it.airgap.tezos.rpc.shell.ShellRpc

public interface TezosRpc : ShellRpc, ActiveRpc