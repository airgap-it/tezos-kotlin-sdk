package it.airgap.tezos.michelson

// https://tezos.gitlab.io/active/michelson.html#full-grammar
public sealed interface MichelsonComparableType : MichelsonType {
    public object Unit : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "unit"
    }

    public object Never : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "never"
    }

    public object Bool : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "bool"
    }

    public object Int : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "int"
    }

    public object Nat : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "nat"
    }

    public object String : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "string"
    }

    public object ChainId : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "chain_id"
    }

    public object Bytes : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "bytes"
    }

    public object Mutez : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "mutez"
    }

    public object KeyHash : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "key_hash"
    }

    public object Key : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "key"
    }

    public object Signature : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "signature"
    }

    public object Timestamp : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "timestamp"
    }

    public object Address : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "address"
    }

    public data class Option(public val type: MichelsonComparableType) : MichelsonComparableType {
        public companion object : GrammarType {
            override val name: kotlin.String = "option"
        }
    }

    public data class Or(
        public val lhs: MichelsonComparableType,
        public val rhs: MichelsonComparableType,
    ) : MichelsonComparableType {
        public companion object : GrammarType {
            override val name: kotlin.String = "or"
        }
    }

    public data class Pair(public val types: List<MichelsonComparableType>) : MichelsonComparableType {
        init {
            require(types.size >= 2)
        }

        public companion object : GrammarType {
            override val name: kotlin.String = "pair"
        }
    }

    public sealed interface GrammarType : MichelsonType.GrammarType {
        public companion object {}
    }

    public companion object {
        public fun Pair(vararg types: MichelsonComparableType): Pair = Pair(types.toList())
    }
}