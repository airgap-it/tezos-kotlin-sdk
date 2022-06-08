package it.airgap.tezos.operation

import it.airgap.tezos.operation.internal.TezosOperationModule

public val OperationModule: TezosOperationModule.Builder
    get() = TezosOperationModule.Builder()
