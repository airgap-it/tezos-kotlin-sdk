package it.airgap.tezos.operation.contract

import it.airgap.tezos.michelson.micheline.MichelineNode

public data class Parameters(
    public val entrypoint: Entrypoint,
    public val value: MichelineNode,
) {
    public companion object {}
}
