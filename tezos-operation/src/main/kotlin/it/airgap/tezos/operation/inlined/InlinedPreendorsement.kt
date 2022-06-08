package it.airgap.tezos.operation.inlined

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.Signature
import it.airgap.tezos.operation.OperationContent

/**
 * Inlined preendorsement.
 *
 * See [P2P message format](https://tezos.gitlab.io/shell/p2p_api.html#alpha-inlined-preendorsement) for more details.
 */
public data class InlinedPreendorsement(
    public val branch: BlockHash,
    public val operations: OperationContent.Preendorsement,
    public val signature: Signature,
)
