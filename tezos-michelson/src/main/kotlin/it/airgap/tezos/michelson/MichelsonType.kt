package it.airgap.tezos.michelson

/**
 * Tezos Michelson types as defined in [the documentation](https://tezos.gitlab.io/active/michelson.html#full-grammar).
 *
 * See also: [Michelson Reference](https://tezos.gitlab.io/michelson-reference/).
 */
public sealed interface MichelsonType : Michelson {
    public val metadata: Metadata

    override val annotations: kotlin.collections.List<Michelson.Annotation>
        get() = with(metadata) { listOfNotNull(typeName, fieldName) }

    public data class Parameter(public val type: MichelsonType, override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "parameter"
            override val tag: ByteArray = byteArrayOf(0)
        }
    }

    public data class Storage(public val type: MichelsonType, override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "storage"
            override val tag: ByteArray = byteArrayOf(1)
        }
    }

    public data class Code(public val code: MichelsonInstruction, override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "code"
            override val tag: ByteArray = byteArrayOf(2)
        }
    }

    public data class Option(public val type: MichelsonType, override val metadata: Metadata = Metadata()) : MichelsonType {

        public operator fun get(field: String): MichelsonType? = type.takeIf { it.metadata.fieldName?.matches(field) == true }

        public companion object : Prim {
            override val name: String = "option"
            override val tag: ByteArray = byteArrayOf(99)
        }
    }

    public data class List(public val type: MichelsonType, override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "list"
            override val tag: ByteArray = byteArrayOf(95)
        }
    }

    public data class Set(public val type: MichelsonComparableType, override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "set"
            override val tag: ByteArray = byteArrayOf(102)
        }
    }

    public data class Operation(override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "operation"
            override val tag: ByteArray = byteArrayOf(109)
        }
    }

    public data class Contract(public val type: MichelsonType, override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "contract"
            override val tag: ByteArray = byteArrayOf(90)
        }
    }

    public data class Ticket(public val type: MichelsonComparableType, override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "ticket"
            override val tag: ByteArray = byteArrayOf((135).toByte())
        }
    }

    public data class Pair(public val types: kotlin.collections.List<MichelsonType>, override val metadata: Metadata = Metadata()) : MichelsonType {
        init {
            require(types.size >= 2)
        }

        public operator fun get(field: String): MichelsonType? = types.find { it.metadata.typeName?.matches(field) == true }

        public companion object : Prim {
            override val name: String = "pair"
            override val tag: ByteArray = byteArrayOf(101)
        }
    }

    public data class Or(public val lhs: MichelsonType, public val rhs: MichelsonType, override val metadata: Metadata = Metadata()) : MichelsonType {

        public operator fun get(field: String): MichelsonType? = listOf(lhs, rhs).find { it.metadata.fieldName?.matches(field) == true }

        public companion object : Prim {
            override val name: String = "or"
            override val tag: ByteArray = byteArrayOf(100)
        }
    }

    public data class Lambda(
        public val parameterType: MichelsonType,
        public val returnType: MichelsonType,
        override val metadata: Metadata = Metadata(),
    ) : MichelsonType {
        public companion object : Prim {
            override val name: String = "lambda"
            override val tag: ByteArray = byteArrayOf(94)
        }
    }

    public data class Map(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
        override val metadata: Metadata = Metadata(),
    ) : MichelsonType {
        public companion object : Prim {
            override val name: String = "map"
            override val tag: ByteArray = byteArrayOf(96)
        }
    }

    public data class BigMap(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
        override val metadata: Metadata = Metadata(),
    ) : MichelsonType {
        public companion object : Prim {
            override val name: String = "big_map"
            override val tag: ByteArray = byteArrayOf(97)
        }
    }

    public data class Bls12_381G1(override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "bls12_381_g1"
            override val tag: ByteArray = byteArrayOf((128).toByte())
        }
    }

    public data class Bls12_381G2(override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "bls12_381_g2"
            override val tag: ByteArray = byteArrayOf((129).toByte())
        }
    }

    public data class Bls12_381Fr(override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "bls12_381_fr"
            override val tag: ByteArray = byteArrayOf((130).toByte())
        }
    }

    public data class SaplingTransaction(public val memoSize: MichelsonData.NaturalNumberConstant, override val metadata: Metadata = Metadata()) : MichelsonType {

        public constructor(memoSize: UByte, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(memoSize), metadata)
        public constructor(memoSize: UShort, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(memoSize), metadata)
        public constructor(memoSize: UInt, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(memoSize), metadata)
        public constructor(memoSize: ULong, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(memoSize), metadata)

        public companion object : Prim {
            override val name: String = "sapling_transaction"
            override val tag: ByteArray = byteArrayOf((132).toByte())
        }
    }

    public data class SaplingState(public val memoSize: MichelsonData.NaturalNumberConstant, override val metadata: Metadata = Metadata()) : MichelsonType {

        public constructor(memoSize: UByte, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(memoSize), metadata)
        public constructor(memoSize: UShort, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(memoSize), metadata)
        public constructor(memoSize: UInt, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(memoSize), metadata)
        public constructor(memoSize: ULong, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(memoSize), metadata)

        public companion object : Prim {
            override val name: String = "sapling_state"
            override val tag: ByteArray = byteArrayOf((131).toByte())
        }
    }

    public data class Chest(override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "chest"
            override val tag: ByteArray = byteArrayOf((141).toByte())
        }
    }

    public data class ChestKey(override val metadata: Metadata = Metadata()) : MichelsonType {
        public companion object : Prim {
            override val name: String = "chest_key"
            override val tag: ByteArray = byteArrayOf((142).toByte())
        }
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
    
    public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val fieldName: Michelson.Annotation.Field? = null)

    public companion object {
        public fun Pair(vararg types: MichelsonType, metadata: Metadata = Metadata()): Pair = Pair(types.toList(), metadata)
    }
}