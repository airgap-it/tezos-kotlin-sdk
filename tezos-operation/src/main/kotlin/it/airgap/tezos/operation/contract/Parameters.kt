package it.airgap.tezos.operation.contract

import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.operation.OperationContent

/**
 * Transaction operation content parameters.
 *
 * See [P2P message format](https://tezos.gitlab.io/shell/p2p_api.html#x-142) for more details.
 *
 * @see [OperationContent.Transaction]
 */
public data class Parameters(
    public val entrypoint: Entrypoint,
    public val value: MichelineNode,
) {
    public companion object {}
}
