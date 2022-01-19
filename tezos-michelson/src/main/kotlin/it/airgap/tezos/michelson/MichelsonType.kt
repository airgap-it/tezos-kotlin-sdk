package it.airgap.tezos.michelson

// https://tezos.gitlab.io/active/michelson.html#full-grammar
public sealed interface MichelsonType : Michelson {
    public data class Parameter(public val type: MichelsonType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "parameter"
            override val tag: Int = 0
        }
    }

    public data class Storage(public val type: MichelsonType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "storage"
            override val tag: Int = 1
        }
    }

    public data class Code(public val code: MichelsonInstruction) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "code"
            override val tag: Int = 2
        }
    }

    public data class Option(public val type: MichelsonType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "option"
            override val tag: Int = 99
        }
    }

    public data class List(public val type: MichelsonType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "list"
            override val tag: Int = 95
        }
    }

    public data class Set(public val type: MichelsonComparableType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "set"
            override val tag: Int = 102
        }
    }

    public object Operation : MichelsonType, GrammarType {
        override val name: String = "operation"
        override val tag: Int = 109
    }

    public data class Contract(public val type: MichelsonType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "contract"
            override val tag: Int = 90
        }
    }

    public data class Ticket(public val type: MichelsonComparableType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "ticket"
            override val tag: Int = 135
        }
    }

    public data class Pair(public val types: kotlin.collections.List<MichelsonType>) : MichelsonType {
        init {
            require(types.size >= 2)
        }

        public companion object : GrammarType {
            override val name: String = "pair"
            override val tag: Int = 101
        }
    }

    public data class Or(public val lhs: MichelsonType, public val rhs: MichelsonType) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "or"
            override val tag: Int = 100
        }
    }

    public data class Lambda(
        public val parameterType: MichelsonType,
        public val returnType: MichelsonType,
    ) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "lambda"
            override val tag: Int = 94
        }
    }

    public data class Map(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
    ) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "map"
            override val tag: Int
                get() = 96
        }
    }

    public data class BigMap(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
    ) : MichelsonType {
        public companion object : GrammarType {
            override val name: String = "big_map"
            override val tag: Int = 97
        }
    }

    public object Bls12_381G1 : MichelsonType, GrammarType {
        override val name: String = "bls12_381_g1"
        override val tag: Int = 128
    }

    public object Bls12_381G2 : MichelsonType, GrammarType {
        override val name: String = "bls12_381_g2"
        override val tag: Int = 129
    }

    public object Bls12_381Fr : MichelsonType, GrammarType {
        override val name: String = "bls12_381_fr"
        override val tag: Int = 130
    }

    public data class SaplingTransaction(public val memoSize: MichelsonData.NaturalNumberConstant) : MichelsonType {

        public constructor(memoSize: UByte) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UShort) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UInt) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: ULong) : this(MichelsonData.NaturalNumberConstant(memoSize))

        public companion object : GrammarType {
            override val name: String = "sapling_transaction"
            override val tag: Int = 132
        }
    }

    public data class SaplingState(public val memoSize: MichelsonData.NaturalNumberConstant) : MichelsonType {

        public constructor(memoSize: UByte) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UShort) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UInt) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: ULong) : this(MichelsonData.NaturalNumberConstant(memoSize))

        public companion object : GrammarType {
            override val name: String = "sapling_state"
            override val tag: Int = 131
        }
    }

    public object Chest : MichelsonType, GrammarType {
        override val name: String = "chest"
        override val tag: Int = 141
    }

    public object ChestKey : MichelsonType, GrammarType {
        override val name: String = "chest_key"
        override val tag: Int = 142
    }
    
    public sealed interface GrammarType : Michelson.GrammarType {
        public companion object {}
    }

    public companion object {
        public fun Pair(vararg types: MichelsonType): Pair = Pair(types.toList())
    }
}