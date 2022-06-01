package it.airgap.tezos.michelson

/**
 * Tezos Michelson comparable types as defined in [the documentation](https://tezos.gitlab.io/active/michelson.html#full-grammar).
 *
 * See also: [Michelson Reference](https://tezos.gitlab.io/michelson-reference/).
 */
public sealed interface MichelsonComparableType : MichelsonType {
    public data class Unit(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "unit"
            override val tag: ByteArray = byteArrayOf(108)
        }
    }

    public data class Never(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "never"
            override val tag: ByteArray = byteArrayOf(120)
        }
    }

    public data class Bool(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "bool"
            override val tag: ByteArray = byteArrayOf(89)
        }
    }

    public data class Int(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "int"
            override val tag: ByteArray = byteArrayOf(91)
        }
    }

    public data class Nat(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "nat"
            override val tag: ByteArray = byteArrayOf(98)
        }
    }

    public data class String(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "string"
            override val tag: ByteArray = byteArrayOf(104)
        }
    }

    public data class ChainId(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "chain_id"
            override val tag: ByteArray = byteArrayOf(116)
        }
    }

    public data class Bytes(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "bytes"
            override val tag: ByteArray = byteArrayOf(105)
        }
    }

    public data class Mutez(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "mutez"
            override val tag: ByteArray = byteArrayOf(106)
        }
    }

    public data class KeyHash(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "key_hash"
            override val tag: ByteArray = byteArrayOf(93)
        }
    }

    public data class Key(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "key"
            override val tag: ByteArray = byteArrayOf(92)
        }
    }

    public data class Signature(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "signature"
            override val tag: ByteArray = byteArrayOf(103)
        }
    }

    public data class Timestamp(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "timestamp"
            override val tag: ByteArray = byteArrayOf(107)
        }
    }

    public data class Address(override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public companion object : Prim {
            override val name: kotlin.String = "address"
            override val tag: ByteArray = byteArrayOf(110)
        }
    }

    public data class Option(public val type: MichelsonComparableType, override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        public operator fun get(field: kotlin.String): MichelsonType? = type.takeIf { it.metadata.typeName?.matches(field) == true }

        public companion object : Prim {
            override val name: kotlin.String = "option"
            override val tag: ByteArray = byteArrayOf(99)
        }
    }

    public data class Or(
        public val lhs: MichelsonComparableType,
        public val rhs: MichelsonComparableType,
        override val metadata: MichelsonType.Metadata = MichelsonType.Metadata(),
    ) : MichelsonComparableType {
        public operator fun get(field: kotlin.String): MichelsonType? = listOf(lhs, rhs).find { it.metadata.typeName?.matches(field) == true }

        public companion object : Prim {
            override val name: kotlin.String = "or"
            override val tag: ByteArray = byteArrayOf(100)
        }
    }

    public data class Pair(public val types: List<MichelsonComparableType>, override val metadata: MichelsonType.Metadata = MichelsonType.Metadata()) : MichelsonComparableType {
        init {
            require(types.size >= 2) { "Pair requires at least 2 arguments." }
        }

        public operator fun get(field: kotlin.String): MichelsonType? = types.find { it.metadata.typeName?.matches(field) == true }

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
        public fun Pair(vararg types: MichelsonComparableType, metadata: MichelsonType.Metadata = MichelsonType.Metadata()): Pair =
            Pair(types.toList(), metadata)
    }
}