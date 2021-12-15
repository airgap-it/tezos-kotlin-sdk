package it.airgap.tezos.michelson.micheline

import it.airgap.tezos.michelson.internal.coder.MichelineJsonCoder
import kotlinx.serialization.Serializable

// https://tezos.gitlab.io/shell/micheline.html#bnf-grammar
@Serializable(with = MichelineJsonCoder.NodeSerializer::class)
public sealed class MichelineNode {
    public companion object {}
}