package it.airgap.tezos.operation.inlined

import it.airgap.tezos.operation.OperationContent

public data class InlinedPreendorsement(
    public val branch: String,
    public val operations: OperationContent.Preendorsement,
    public val signature: String,
)
