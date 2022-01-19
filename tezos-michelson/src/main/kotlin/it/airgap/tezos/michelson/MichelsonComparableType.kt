package it.airgap.tezos.michelson

// https://tezos.gitlab.io/active/michelson.html#full-grammar
public sealed interface MichelsonComparableType : MichelsonType {
    public object Unit : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "unit"
        override val tag: kotlin.Int = 108
    }

    public object Never : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "never"
        override val tag: kotlin.Int = 120
    }

    public object Bool : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "bool"
        override val tag: kotlin.Int = 89
    }

    public object Int : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "int"
        override val tag: kotlin.Int = 91
    }

    public object Nat : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "nat"
        override val tag: kotlin.Int = 98
    }

    public object String : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "string"
        override val tag: kotlin.Int = 104
    }

    public object ChainId : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "chain_id"
        override val tag: kotlin.Int = 116
    }

    public object Bytes : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "bytes"
        override val tag: kotlin.Int = 105
    }

    public object Mutez : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "mutez"
        override val tag: kotlin.Int = 106
    }

    public object KeyHash : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "key_hash"
        override val tag: kotlin.Int = 93
    }

    public object Key : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "key"
        override val tag: kotlin.Int = 92
    }

    public object Signature : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "signature"
        override val tag: kotlin.Int = 103
    }

    public object Timestamp : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "timestamp"
        override val tag: kotlin.Int = 107
    }

    public object Address : MichelsonComparableType, GrammarType {
        override val name: kotlin.String = "address"
        override val tag: kotlin.Int = 110
    }

    public data class Option(public val type: MichelsonComparableType) : MichelsonComparableType {
        public companion object : GrammarType {
            override val name: kotlin.String = "option"
            override val tag: kotlin.Int = 99
        }
    }

    public data class Or(
        public val lhs: MichelsonComparableType,
        public val rhs: MichelsonComparableType,
    ) : MichelsonComparableType {
        public companion object : GrammarType {
            override val name: kotlin.String = "or"
            override val tag: kotlin.Int = 100
        }
    }

    public data class Pair(public val types: List<MichelsonComparableType>) : MichelsonComparableType {
        init {
            require(types.size >= 2) { "Pair requires at least 2 arguments." }
        }

        public companion object : GrammarType {
            override val name: kotlin.String = "pair"
            override val tag: kotlin.Int = 101
        }
    }

    public sealed interface GrammarType : MichelsonType.GrammarType {
        public companion object {}
    }

    public companion object {
        public fun Pair(vararg types: MichelsonComparableType): Pair = Pair(types.toList())
    }
}