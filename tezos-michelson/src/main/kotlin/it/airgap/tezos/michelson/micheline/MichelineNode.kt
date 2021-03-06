package it.airgap.tezos.michelson.micheline

import it.airgap.tezos.michelson.internal.serializer.MichelineNodeSerializer
import kotlinx.serialization.Serializable

/**
 * Tezos Micheline types as defined in [the documentation](https://tezos.gitlab.io/shell/micheline.html#bnf-grammar).
 */
@Serializable(with = MichelineNodeSerializer::class)
public sealed class MichelineNode {
    public companion object {}
}