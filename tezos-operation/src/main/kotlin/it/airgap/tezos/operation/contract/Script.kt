package it.airgap.tezos.operation.contract

import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.operation.OperationContent

/**
 * Contract script.
 *
 * See [P2P message format](https://tezos.gitlab.io/shell/p2p_api.html#alpha-scripted-contracts) for more details.
 *
 * @see [OperationContent.Origination]
 */
public data class Script(
    public val code: Micheline,
    public val storage: Micheline,
)
