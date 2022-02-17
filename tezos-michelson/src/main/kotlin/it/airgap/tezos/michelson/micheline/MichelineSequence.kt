package it.airgap.tezos.michelson.micheline

import it.airgap.tezos.michelson.internal.coder.MichelineJsonCoder
import kotlinx.serialization.Serializable

// https://tezos.gitlab.io/shell/micheline.html#bnf-grammar
@Serializable(with = MichelineJsonCoder.SequenceSerializer::class)
public data class MichelineSequence(public val nodes: List<MichelineNode>) : MichelineNode() {
    public companion object {}
}

public fun MichelineSequence(vararg nodes: MichelineNode): MichelineSequence = MichelineSequence(nodes.toList())