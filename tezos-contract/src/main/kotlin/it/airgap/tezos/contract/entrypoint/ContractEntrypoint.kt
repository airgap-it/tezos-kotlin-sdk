package it.airgap.tezos.contract.entrypoint

import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.rpc.internal.cache.Cached

// -- ContractEntrypoint --

public class ContractEntrypoint internal constructor(
    public val name: String,
    private val metaCached: Cached<MetaContractEntrypoint?>,
) {
    public operator fun invoke(args: MichelineNode)/*: TODO */ {
        TODO()
    }

    public operator fun invoke(args: List<Pair<String, MichelineNode>>)/*: TODO */ {
        TODO()
    }

    public companion object {
        internal const val DEFAULT = "default"
    }
}

// -- MetaContractEntrypoint --

internal class MetaContractEntrypoint(type: MichelineNode)