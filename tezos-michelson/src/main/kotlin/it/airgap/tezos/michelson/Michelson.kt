package it.airgap.tezos.michelson

// https://tezos.gitlab.io/active/michelson.html#full-grammar
public sealed interface Michelson {
    public sealed interface GrammarType {
        public val name: String

        public companion object {}
    }

    public companion object {}
}