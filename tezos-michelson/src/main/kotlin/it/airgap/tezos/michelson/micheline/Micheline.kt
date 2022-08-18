package it.airgap.tezos.michelson.micheline

import it.airgap.tezos.michelson.internal.serializer.MichelineSerializer
import kotlinx.serialization.Serializable

/**
 * Tezos Micheline types as defined in [the documentation](https://tezos.gitlab.io/shell/micheline.html#bnf-grammar).
 */
@Serializable(with = MichelineSerializer::class)
public sealed class Micheline {
    public companion object {}
}