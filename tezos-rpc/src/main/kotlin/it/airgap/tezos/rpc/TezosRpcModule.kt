package it.airgap.tezos.rpc

import it.airgap.tezos.rpc.internal.TezosRpcModule

public val RpcModule: TezosRpcModule.Builder
    get() = TezosRpcModule.Builder()