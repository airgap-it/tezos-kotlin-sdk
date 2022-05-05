package it.airgap.tezos.contract

import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- ContractCode --

public data class ContractCode internal constructor(
    public val parameter: MichelsonType.Parameter,
    public val storage: MichelsonType.Storage,
    public val code: MichelsonType.Code,
)

// -- RpcContractCode --

internal data class RpcContractCode(
    val parameter: MichelineNode,
    val storage: MichelineNode,
    val code: MichelineNode,
)