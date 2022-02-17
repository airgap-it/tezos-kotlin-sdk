package it.airgap.tezos.operation.inlined

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.SignatureEncoded
import it.airgap.tezos.operation.OperationContent

public data class InlinedPreendorsement(
    public val branch: BlockHash,
    public val operations: OperationContent.Preendorsement,
    public val signature: SignatureEncoded<*>,
)
