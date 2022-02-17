package it.airgap.tezos.michelson

// https://tezos.gitlab.io/active/michelson.html#full-grammar
public sealed interface MichelsonType : Michelson {
    public data class Parameter(public val type: MichelsonType) : MichelsonType {
        public companion object : Prim {
            override val name: String = "parameter"
            override val tag: ByteArray = byteArrayOf(0)
        }
    }

    public data class Storage(public val type: MichelsonType) : MichelsonType {
        public companion object : Prim {
            override val name: String = "storage"
            override val tag: ByteArray = byteArrayOf(1)
        }
    }

    public data class Code(public val code: MichelsonInstruction) : MichelsonType {
        public companion object : Prim {
            override val name: String = "code"
            override val tag: ByteArray = byteArrayOf(2)
        }
    }

    public data class Option(public val type: MichelsonType) : MichelsonType {
        public companion object : Prim {
            override val name: String = "option"
            override val tag: ByteArray = byteArrayOf(99)
        }
    }

    public data class List(public val type: MichelsonType) : MichelsonType {
        public companion object : Prim {
            override val name: String = "list"
            override val tag: ByteArray = byteArrayOf(95)
        }
    }

    public data class Set(public val type: MichelsonComparableType) : MichelsonType {
        public companion object : Prim {
            override val name: String = "set"
            override val tag: ByteArray = byteArrayOf(102)
        }
    }

    public object Operation : MichelsonType, Prim {
        override val name: String = "operation"
        override val tag: ByteArray = byteArrayOf(109)
    }

    public data class Contract(public val type: MichelsonType) : MichelsonType {
        public companion object : Prim {
            override val name: String = "contract"
            override val tag: ByteArray = byteArrayOf(90)
        }
    }

    public data class Ticket(public val type: MichelsonComparableType) : MichelsonType {
        public companion object : Prim {
            override val name: String = "ticket"
            override val tag: ByteArray = byteArrayOf((135).toByte())
        }
    }

    public data class Pair(public val types: kotlin.collections.List<MichelsonType>) : MichelsonType {
        init {
            require(types.size >= 2)
        }

        public companion object : Prim {
            override val name: String = "pair"
            override val tag: ByteArray = byteArrayOf(101)
        }
    }

    public data class Or(public val lhs: MichelsonType, public val rhs: MichelsonType) : MichelsonType {
        public companion object : Prim {
            override val name: String = "or"
            override val tag: ByteArray = byteArrayOf(100)
        }
    }

    public data class Lambda(
        public val parameterType: MichelsonType,
        public val returnType: MichelsonType,
    ) : MichelsonType {
        public companion object : Prim {
            override val name: String = "lambda"
            override val tag: ByteArray = byteArrayOf(94)
        }
    }

    public data class Map(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
    ) : MichelsonType {
        public companion object : Prim {
            override val name: String = "map"
            override val tag: ByteArray = byteArrayOf(96)
        }
    }

    public data class BigMap(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
    ) : MichelsonType {
        public companion object : Prim {
            override val name: String = "big_map"
            override val tag: ByteArray = byteArrayOf(97)
        }
    }

    public object Bls12_381G1 : MichelsonType, Prim {
        override val name: String = "bls12_381_g1"
        override val tag: ByteArray = byteArrayOf((128).toByte())
    }

    public object Bls12_381G2 : MichelsonType, Prim {
        override val name: String = "bls12_381_g2"
        override val tag: ByteArray = byteArrayOf((129).toByte())
    }

    public object Bls12_381Fr : MichelsonType, Prim {
        override val name: String = "bls12_381_fr"
        override val tag: ByteArray = byteArrayOf((130).toByte())
    }

    public data class SaplingTransaction(public val memoSize: MichelsonData.NaturalNumberConstant) : MichelsonType {

        public constructor(memoSize: UByte) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UShort) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UInt) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: ULong) : this(MichelsonData.NaturalNumberConstant(memoSize))

        public companion object : Prim {
            override val name: String = "sapling_transaction"
            override val tag: ByteArray = byteArrayOf((132).toByte())
        }
    }

    public data class SaplingState(public val memoSize: MichelsonData.NaturalNumberConstant) : MichelsonType {

        public constructor(memoSize: UByte) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UShort) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UInt) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: ULong) : this(MichelsonData.NaturalNumberConstant(memoSize))

        public companion object : Prim {
            override val name: String = "sapling_state"
            override val tag: ByteArray = byteArrayOf((131).toByte())
        }
    }

    public object Chest : MichelsonType, Prim {
        override val name: String = "chest"
        override val tag: ByteArray = byteArrayOf((141).toByte())
    }

    public object ChestKey : MichelsonType, Prim {
        override val name: String = "chest_key"
        override val tag: ByteArray = byteArrayOf((142).toByte())
    }
    
    public sealed interface Prim : Michelson.Prim {
        public companion object {
            public val values: kotlin.collections.List<Prim>
                get() = listOf(
                    Parameter,
                    Storage,
                    Code,
                    Option,
                    List,
                    Set,
                    Operation,
                    Contract,
                    Ticket,
                    Pair,
                    Or,
                    Lambda,
                    Map,
                    BigMap,
                    Bls12_381G1,
                    Bls12_381G2,
                    Bls12_381Fr,
                    SaplingTransaction,
                    SaplingState,
                    Chest,
                    ChestKey,
                ) + MichelsonComparableType.Prim.values
        }
    }

    public companion object {
        public fun Pair(vararg types: MichelsonType): Pair = Pair(types.toList())
    }
}