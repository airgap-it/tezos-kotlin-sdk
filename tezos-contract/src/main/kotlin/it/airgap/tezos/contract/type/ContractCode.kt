package it.airgap.tezos.contract.type

import it.airgap.tezos.michelson.micheline.MichelineNode

// -- ContractCode --

public data class ContractCode internal constructor(
    public val parameter: MichelineNode,
    public val storage: MichelineNode,
    public val code: MichelineNode,
)
