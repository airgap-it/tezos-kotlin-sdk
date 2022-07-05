package it.airgap.tezos.michelson.micheline

import it.airgap.tezos.michelson.internal.serializer.MichelineSequenceSerializer
import kotlinx.serialization.Serializable

/**
 * Micheline sequence types as defined in [the documentation](https://tezos.gitlab.io/shell/micheline.html#bnf-grammar).
 */
@Serializable(with = MichelineSequenceSerializer::class)
public data class MichelineSequence(public val nodes: List<Micheline>) : Micheline() {
    public companion object {}
}

public fun MichelineSequence(vararg nodes: Micheline): MichelineSequence = MichelineSequence(nodes.toList())