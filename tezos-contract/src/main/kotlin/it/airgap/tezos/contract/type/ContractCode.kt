package it.airgap.tezos.contract.type

import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.rpc.internal.cache.Cached

/**
 * Contract code representation.
 *
 * It reflects the structure returned as `code`
 * while fetching a contract code from the node:
 *
 * [GET  ../<block_id>/context/contracts/<contract_id>/script](https://tezos.gitlab.io/active/rpc.html#get-block-id-context-contracts-contract-id-script)
 * ```json
 * {
 *      "code": [
 *          {
 *              "prim": "parameters",
 *              "args": [...]
 *          },
 *          {
 *              "prim": "storage",
 *              "args": [...]
 *          },
 *          {
 *              "prim": "code",
 *              "args": [...]
 *          }
 *      ],
 *      "storage": ...
 * }
 * ```
 */
public data class ContractCode internal constructor(
    public val parameter: Micheline,
    public val storage: Micheline,
    public val code: Micheline,
)

internal typealias LazyContractCode = Cached<ContractCode>
