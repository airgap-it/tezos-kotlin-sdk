package it.airgap.tezos.contract

import it.airgap.tezos.contract.internal.TezosContractModule

public val ContractModule: TezosContractModule.Builder
    get() = TezosContractModule.Builder()