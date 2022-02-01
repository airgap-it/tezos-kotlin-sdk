package it.airgap.tezos.operation.contract

import it.airgap.tezos.michelson.micheline.MichelineNode

public data class Script(
    public val code: MichelineNode,
    public val storage: MichelineNode,
)
