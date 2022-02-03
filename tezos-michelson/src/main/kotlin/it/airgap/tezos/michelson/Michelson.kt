package it.airgap.tezos.michelson

// https://tezos.gitlab.io/active/michelson.html#full-grammar
public sealed interface Michelson {
    public sealed interface Prim {
        public val name: String
        public val tag: ByteArray

        public companion object {
            public val values: List<Prim>
                get() = MichelsonData.Prim.values + MichelsonType.Prim.values
        }
    }

    public companion object {}
}