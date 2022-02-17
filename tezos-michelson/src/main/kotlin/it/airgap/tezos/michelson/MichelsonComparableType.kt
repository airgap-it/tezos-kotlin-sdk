package it.airgap.tezos.michelson

// https://tezos.gitlab.io/active/michelson.html#full-grammar
public sealed interface MichelsonComparableType : MichelsonType {
    public object Unit : MichelsonComparableType, Prim {
        override val name: kotlin.String = "unit"
        override val tag: ByteArray = byteArrayOf(108)
    }

    public object Never : MichelsonComparableType, Prim {
        override val name: kotlin.String = "never"
        override val tag: ByteArray = byteArrayOf(120)
    }

    public object Bool : MichelsonComparableType, Prim {
        override val name: kotlin.String = "bool"
        override val tag: ByteArray = byteArrayOf(89)
    }

    public object Int : MichelsonComparableType, Prim {
        override val name: kotlin.String = "int"
        override val tag: ByteArray = byteArrayOf(91)
    }

    public object Nat : MichelsonComparableType, Prim {
        override val name: kotlin.String = "nat"
        override val tag: ByteArray = byteArrayOf(98)
    }

    public object String : MichelsonComparableType, Prim {
        override val name: kotlin.String = "string"
        override val tag: ByteArray = byteArrayOf(104)
    }

    public object ChainId : MichelsonComparableType, Prim {
        override val name: kotlin.String = "chain_id"
        override val tag: ByteArray = byteArrayOf(116)
    }

    public object Bytes : MichelsonComparableType, Prim {
        override val name: kotlin.String = "bytes"
        override val tag: ByteArray = byteArrayOf(105)
    }

    public object Mutez : MichelsonComparableType, Prim {
        override val name: kotlin.String = "mutez"
        override val tag: ByteArray = byteArrayOf(106)
    }

    public object KeyHash : MichelsonComparableType, Prim {
        override val name: kotlin.String = "key_hash"
        override val tag: ByteArray = byteArrayOf(93)
    }

    public object Key : MichelsonComparableType, Prim {
        override val name: kotlin.String = "key"
        override val tag: ByteArray = byteArrayOf(92)
    }

    public object Signature : MichelsonComparableType, Prim {
        override val name: kotlin.String = "signature"
        override val tag: ByteArray = byteArrayOf(103)
    }

    public object Timestamp : MichelsonComparableType, Prim {
        override val name: kotlin.String = "timestamp"
        override val tag: ByteArray = byteArrayOf(107)
    }

    public object Address : MichelsonComparableType, Prim {
        override val name: kotlin.String = "address"
        override val tag: ByteArray = byteArrayOf(110)
    }

    public data class Option(public val type: MichelsonComparableType) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "option"
            override val tag: ByteArray = byteArrayOf(99)
        }
    }

    public data class Or(
        public val lhs: MichelsonComparableType,
        public val rhs: MichelsonComparableType,
    ) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "or"
            override val tag: ByteArray = byteArrayOf(100)
        }
    }

    public data class Pair(public val types: List<MichelsonComparableType>) : MichelsonComparableType {
        init {
            require(types.size >= 2) { "Pair requires at least 2 arguments." }
        }

        public companion object : Prim {
            override val name: kotlin.String = "pair"
            override val tag: ByteArray = byteArrayOf(101)
        }
    }

    public sealed interface Prim : MichelsonType.Prim {
        public companion object {
            public val values: List<Prim>
                get() = listOf(
                    Unit,
                    Never,
                    Bool,
                    Int,
                    Nat,
                    String,
                    ChainId,
                    Bytes,
                    Mutez,
                    KeyHash,
                    Key,
                    Signature,
                    Timestamp,
                    Address,
                    Option,
                    Or,
                    Pair,
                )
        }
    }

    public companion object {
        public fun Pair(vararg types: MichelsonComparableType): Pair = Pair(types.toList())
    }
}