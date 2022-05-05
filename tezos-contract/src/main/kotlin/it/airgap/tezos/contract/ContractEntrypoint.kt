package it.airgap.tezos.contract

import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- ContractEntrypoint --

public class ContractEntrypoint internal constructor(
    public val name: String,
    private val type: MetaContractEntrypoint?,
) {
    public operator fun invoke(args: MichelsonData)/*: TODO */ {
        TODO()
    }

    public operator fun invoke(args: MichelineNode)/*: TODO */ {
        TODO()
    }

    public operator fun invoke(args: List<Pair<String, Any>>)/*: TODO */ {
        TODO()
    }
}

// -- MetaContractEntrypoint --

internal class MetaContractEntrypoint()