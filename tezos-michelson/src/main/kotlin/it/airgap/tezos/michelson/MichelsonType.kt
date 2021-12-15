package it.airgap.tezos.michelson

// https://tezos.gitlab.io/active/michelson.html#full-grammar
public sealed interface MichelsonType : Michelson {
    public data class Option(public val type: MichelsonType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "option"
        }
    }

    public data class List(public val type: MichelsonType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "list"
        }
    }

    public data class Set(public val type: MichelsonComparableType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "set"
        }
    }

    public object Operation : MichelsonType, GrammarType {
        override val name: String = "operation"
    }

    public data class Contract(public val type: MichelsonType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "contract"
        }
    }

    public data class Ticket(public val type: MichelsonComparableType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "ticket"
        }
    }

    public data class Pair(public val types: kotlin.collections.List<MichelsonType>) : MichelsonType {
        init {
            require(types.size >= 2)
        }

        public companion object : GrammarType {
            override val name: String = "pair"
        }
    }

    public data class Or(public val lhs: MichelsonType, public val rhs: MichelsonType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "or"
        }
    }

    public data class Lambda(
        public val parameterType: MichelsonType,
        public val returnType: MichelsonType,
    ) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "lambda"
        }
    }

    public data class Map(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
    ) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "map"
        }
    }

    public data class BigMap(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
    ) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "big_map"
        }
    }

    public object Bls12_381G1 : MichelsonType, GrammarType {
        override val name: String = "bls12_381_g1"
    }

    public object Bls12_381G2 : MichelsonType, GrammarType {
        override val name: String = "bls12_381_g2"
    }

    public object Bls12_381Fr : MichelsonType, GrammarType {
        override val name: String = "bls12_381_fr"
    }

    public data class SaplingTransaction(public val memoSize: MichelsonData.NaturalNumberConstant) : MichelsonType {

        public constructor(memoSize: UByte) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UShort) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UInt) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: ULong) : this(MichelsonData.NaturalNumberConstant(memoSize))

        public companion object : GrammarType {
            override val name: String = "sapling_transaction"
        }
    }

    public data class SaplingState(public val memoSize: MichelsonData.NaturalNumberConstant) : MichelsonType {

        public constructor(memoSize: UByte) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UShort) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UInt) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: ULong) : this(MichelsonData.NaturalNumberConstant(memoSize))

        public companion object : GrammarType {
            override val name: String = "sapling_state"
        }
    }

    public object Chest : MichelsonType, GrammarType {
        override val name: String = "chest"
    }

    public object ChestKey : MichelsonType, GrammarType {
        override val name: String = "chest_key"
    }
    
    public sealed interface GrammarType : Michelson.GrammarType {
        public companion object {}
    }

    public companion object {
        public fun Pair(vararg types: MichelsonType): Pair = Pair(types.toList())
    }
}