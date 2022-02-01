package it.airgap.tezos.operation.inlined

import it.airgap.tezos.operation.OperationContent


public data class InlinedEndorsement(
    public val branch: String,
    public val operations: OperationContent.Endorsement,
    public val signature: String,
)
