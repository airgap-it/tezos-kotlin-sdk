package it.airgap.tezos.contract

import it.airgap.tezos.michelson.MichelsonType

public data class ContractCode(
    public val parameter: MichelsonType.Parameter,
    public val storage: MichelsonType.Storage,
    public val code: MichelsonType.Code,
)