package it.airgap.tezos.michelson

/**
 * Tezos Michelson instruction types as defined in [the documentation](https://tezos.gitlab.io/active/michelson.html#full-grammar).
 *
 * See also: [Michelson Reference](https://tezos.gitlab.io/michelson-reference/).
 */
public sealed interface MichelsonInstruction : MichelsonData {
    public data class Sequence(public val instructions: List<MichelsonInstruction>) : MichelsonInstruction {
        public companion object {}
    }

    public data class Drop(public val n: MichelsonData.NaturalNumberConstant? = null) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : Prim {
            override val name: String = "DROP"
            override val tag: ByteArray = byteArrayOf(32)
        }
    }

    public data class Dup(public val n: MichelsonData.NaturalNumberConstant? = null, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public constructor(n: UByte, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: UShort, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: UInt, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: ULong, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)

        public companion object : Prim {
            override val name: String = "DUP"
            override val tag: ByteArray = byteArrayOf(33)
        }
    }

    public object Swap : MichelsonInstruction, Prim {
        override val name: String = "SWAP"
        override val tag: ByteArray = byteArrayOf(76)
    }

    public data class Dig(public val n: MichelsonData.NaturalNumberConstant) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : Prim {
            override val name: String = "DIG"
            override val tag: ByteArray = byteArrayOf(112)
        }
    }

    public data class Dug(public val n: MichelsonData.NaturalNumberConstant) : MichelsonInstruction {

        public constructor(n: UByte) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UShort) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: UInt) : this(MichelsonData.NaturalNumberConstant(n))
        public constructor(n: ULong) : this(MichelsonData.NaturalNumberConstant(n))

        public companion object : Prim {
            override val name: String = "DUG"
            override val tag: ByteArray = byteArrayOf(113)
        }
    }

    public data class Push(public val type: MichelsonType, public val value: MichelsonData, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "PUSH"
            override val tag: ByteArray = byteArrayOf(67)
        }
    }

    public data class Some(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(typeName, variableName) }

        public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SOME"
            override val tag: ByteArray = byteArrayOf(70)
        }
    }

    public data class None(public val type: MichelsonType, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(typeName, variableName) }

        public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "NONE"
            override val tag: ByteArray = byteArrayOf(62)
        }
    }

    public data class Unit(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(typeName, variableName) }

        public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "UNIT"
            override val tag: ByteArray = byteArrayOf(79)
        }
    }

    public object Never : MichelsonInstruction, Prim {
        override val name: String = "NEVER"
        override val tag: ByteArray = byteArrayOf(121)
    }

    public data class IfNone(
        public val ifBranch: Sequence,
        public val elseBranch: Sequence,
    ) : MichelsonInstruction {
        public companion object : Prim {
            override val name: String = "IF_NONE"
            override val tag: ByteArray = byteArrayOf(47)
        }
    }

    public data class Pair(public val n: MichelsonData.NaturalNumberConstant? = null, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(typeName, variableName) }

        public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val variableName: Michelson.Annotation.Variable? = null)

        public constructor(n: UByte, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: UShort, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: UInt, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: ULong, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)

        public companion object : Prim {
            override val name: String = "PAIR"
            override val tag: ByteArray = byteArrayOf(66)
        }
    }

    public data class Car(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "CAR"
            override val tag: ByteArray = byteArrayOf(22)
        }
    }
    public data class Cdr(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "CDR"
            override val tag: ByteArray = byteArrayOf(23)
        }
    }

    public data class Unpair(public val n: MichelsonData.NaturalNumberConstant? = null, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(firstVariableName, secondVariableName) }

        public data class Metadata(public val firstVariableName: Michelson.Annotation.Variable? = null, public val secondVariableName: Michelson.Annotation.Variable? = null)

        public constructor(n: UByte, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: UShort, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: UInt, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: ULong, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)

        public companion object : Prim {
            override val name: String = "UNPAIR"
            override val tag: ByteArray = byteArrayOf(122)
        }
    }

    public data class Left(public val type: MichelsonType, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(typeName, variableName) }

        public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "LEFT"
            override val tag: ByteArray = byteArrayOf(51)
        }
    }

    public data class Right(public val type: MichelsonType, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(typeName, variableName) }

        public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "RIGHT"
            override val tag: ByteArray = byteArrayOf(68)
        }
    }

    public data class IfLeft(
        public val ifBranch: Sequence,
        public val elseBranch: Sequence,
    ) : MichelsonInstruction {
        public companion object : Prim {
            override val name: String = "IF_LEFT"
            override val tag: ByteArray = byteArrayOf(46)
        }
    }

    public data class Nil(public val type: MichelsonType, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(typeName, variableName) }

        public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "NIL"
            override val tag: ByteArray = byteArrayOf(61)
        }
    }

    public data class Cons(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "CONS"
            override val tag: ByteArray = byteArrayOf(27)
        }
    }

    public data class IfCons(
        public val ifBranch: Sequence,
        public val elseBranch: Sequence,
    ) : MichelsonInstruction {
        public companion object : Prim {
            override val name: String = "IF_CONS"
            override val tag: ByteArray = byteArrayOf(45)
        }
    }

    public data class Size(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SIZE"
            override val tag: ByteArray = byteArrayOf(69)
        }
    }

    public data class EmptySet(public val type: MichelsonComparableType, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(typeName, variableName) }

        public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "EMPTY_SET"
            override val tag: ByteArray = byteArrayOf(36)
        }
    }

    public data class EmptyMap(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
        public val metadata: Metadata = Metadata(),
    ) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(typeName, variableName) }

        public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "EMPTY_MAP"
            override val tag: ByteArray = byteArrayOf(35)
        }
    }

    public data class EmptyBigMap(
        public val keyType: MichelsonComparableType,
        public val valueType: MichelsonType,
        public val metadata: Metadata = Metadata(),
    ) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(typeName, variableName) }

        public data class Metadata(public val typeName: Michelson.Annotation.Type? = null, public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "EMPTY_BIG_MAP"
            override val tag: ByteArray = byteArrayOf(114)
        }
    }

    public data class Map(public val expression: Sequence, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "MAP"
            override val tag: ByteArray = byteArrayOf(56)
        }
    }

    public data class Iter(public val expression: Sequence) : MichelsonInstruction {
        public companion object : Prim {
            override val name: String = "ITER"
            override val tag: ByteArray = byteArrayOf(82)
        }
    }

    public data class Mem(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "MEM"
            override val tag: ByteArray = byteArrayOf(57)
        }
    }

    public data class Get(public val n: MichelsonData.NaturalNumberConstant? = null, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public constructor(n: UByte, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: UShort, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: UInt, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: ULong, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)

        public companion object : Prim {
            override val name: String = "GET"
            override val tag: ByteArray = byteArrayOf(41)
        }
    }

    public data class Update(public val n: MichelsonData.NaturalNumberConstant? = null, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public constructor(n: UByte, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: UShort, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: UInt, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)
        public constructor(n: ULong, metadata: Metadata = Metadata()) : this(MichelsonData.NaturalNumberConstant(n), metadata)

        public companion object : Prim {
            override val name: String = "UPDATE"
            override val tag: ByteArray = byteArrayOf(80)
        }
    }

    public object GetAndUpdate : MichelsonInstruction, Prim {
        override val name: String = "GET_AND_UPDATE"
        override val tag: ByteArray = byteArrayOf((140).toByte())
    }

    public data class If(
        public val ifBranch: Sequence,
        public val elseBranch: Sequence,
    ) : MichelsonInstruction {
        public companion object : Prim {
            override val name: String = "IF"
            override val tag: ByteArray = byteArrayOf(44)
        }
    }

    public data class Loop(public val body: Sequence) : MichelsonInstruction {
        public companion object : Prim {
            override val name: String = "LOOP"
            override val tag: ByteArray = byteArrayOf(52)
        }
    }
    public data class LoopLeft(public val body: Sequence) : MichelsonInstruction {
        public companion object : Prim {
            override val name: String = "LOOP_LEFT"
            override val tag: ByteArray = byteArrayOf(83)
        }
    }

    public data class Lambda(
        public val parameterType: MichelsonType,
        public val returnType: MichelsonType,
        public val body: Sequence,
        public val metadata: Metadata = Metadata()
    ) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "LAMBDA"
            override val tag: ByteArray = byteArrayOf(49)
        }
    }

    public data class Exec(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "EXEC"
            override val tag: ByteArray = byteArrayOf(38)
        }
    }

    public object Apply : MichelsonInstruction, Prim {
        override val name: String = "APPLY"
        override val tag: ByteArray = byteArrayOf(115)
    }

    public data class Dip(
        public val instruction: Sequence,
        public val n: MichelsonData.NaturalNumberConstant? = null,
    ) : MichelsonInstruction {

        public constructor(instruction: Sequence, n: UByte) : this(instruction, MichelsonData.NaturalNumberConstant(n))
        public constructor(instruction: Sequence, n: UShort) : this(instruction, MichelsonData.NaturalNumberConstant(n))
        public constructor(instruction: Sequence, n: UInt) : this(instruction, MichelsonData.NaturalNumberConstant(n))
        public constructor(instruction: Sequence, n: ULong) : this(instruction, MichelsonData.NaturalNumberConstant(n))

        public companion object : Prim {
            override val name: String = "DIP"
            override val tag: ByteArray = byteArrayOf(31)
        }
    }

    public object Failwith : MichelsonInstruction, Prim {
        override val name: String = "FAILWITH"
        override val tag: ByteArray = byteArrayOf(39)
    }

    public data class Cast(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "CAST"
            override val tag: ByteArray = byteArrayOf(87)
        }
    }

    public data class Rename(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "RENAME"
            override val tag: ByteArray = byteArrayOf(88)
        }
    }

    public data class Concat(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "CONCAT"
            override val tag: ByteArray = byteArrayOf(26)
        }
    }

    public object Slice : MichelsonInstruction, Prim {
        override val name: String = "SLICE"
        override val tag: ByteArray = byteArrayOf(111)
    }

    public object Pack : MichelsonInstruction, Prim {
        override val name: String = "PACK"
        override val tag: ByteArray = byteArrayOf(12)
    }

    public data class Unpack(public val type: MichelsonType) : MichelsonInstruction {
        public companion object : Prim {
            override val name: String = "UNPACK"
            override val tag: ByteArray = byteArrayOf(13)
        }
    }

    public data class Add(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "ADD"
            override val tag: ByteArray = byteArrayOf(18)
        }
    }

    public data class Sub(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SUB"
            override val tag: ByteArray = byteArrayOf(75)
        }
    }

    public data class Mul(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "MUL"
            override val tag: ByteArray = byteArrayOf(58)
        }
    }

    public data class Ediv(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "EDIV"
            override val tag: ByteArray = byteArrayOf(34)
        }
    }

    public data class Abs(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "ABS"
            override val tag: ByteArray = byteArrayOf(17)
        }
    }

    public data class Isnat(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "ISNAT"
            override val tag: ByteArray = byteArrayOf(86)
        }
    }

    public data class Int(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "INT"
            override val tag: ByteArray = byteArrayOf(48)
        }
    }

    public data class Neg(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "NEG"
            override val tag: ByteArray = byteArrayOf(59)
        }
    }

    public data class Lsl(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "LSL"
            override val tag: ByteArray = byteArrayOf(53)
        }
    }

    public data class Lsr(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "LSR"
            override val tag: ByteArray = byteArrayOf(54)
        }
    }

    public data class Or(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "OR"
            override val tag: ByteArray = byteArrayOf(65)
        }
    }

    public data class And(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "AND"
            override val tag: ByteArray = byteArrayOf(20)
        }
    }

    public data class Xor(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "XOR"
            override val tag: ByteArray = byteArrayOf(81)
        }
    }

    public data class Not(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "NOT"
            override val tag: ByteArray = byteArrayOf(63)
        }
    }

    public data class Compare(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "COMPARE"
            override val tag: ByteArray = byteArrayOf(25)
        }
    }

    public data class Eq(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "EQ"
            override val tag: ByteArray = byteArrayOf(37)
        }
    }

    public data class Neq(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "NEQ"
            override val tag: ByteArray = byteArrayOf(60)
        }
    }

    public data class Lt(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "LT"
            override val tag: ByteArray = byteArrayOf(55)
        }
    }

    public data class Gt(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "GT"
            override val tag: ByteArray = byteArrayOf(42)
        }
    }

    public data class Le(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "LE"
            override val tag: ByteArray = byteArrayOf(50)
        }
    }

    public data class Ge(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "GE"
            override val tag: ByteArray = byteArrayOf(40)
        }
    }

    public data class Self(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SELF"
            override val tag: ByteArray = byteArrayOf(73)
        }
    }

    public data class SelfAddress(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SELF_ADDRESS"
            override val tag: ByteArray = byteArrayOf(119)
        }
    }

    public data class Contract(public val type: MichelsonType, public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "CONTRACT"
            override val tag: ByteArray = byteArrayOf(85)
        }
    }

    public object TransferTokens : MichelsonInstruction, Prim {
        override val name: String = "TRANSFER_TOKENS"
        override val tag: ByteArray = byteArrayOf(77)
    }

    public data class SetDelegate(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SET_DELEGATE"
            override val tag: ByteArray = byteArrayOf(78)
        }
    }

    public data class CreateContract(
        public val parameterType: MichelsonType,
        public val storageType: MichelsonType,
        public val code: Sequence,
        public val metadata: Metadata = Metadata()
    ) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(firstVariableName, secondVariableName) }

        public data class Metadata internal constructor(
            public val firstVariableName: Michelson.Annotation.Variable? = null,
            public val secondVariableName: Michelson.Annotation.Variable? = null,
        ) {
            public constructor() : this(null, null)
        }

        public companion object : Prim {
            override val name: String = "CREATE_CONTRACT"
            override val tag: ByteArray = byteArrayOf(29)
        }
    }

    public data class ImplicitAccount(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "IMPLICIT_ACCOUNT"
            override val tag: ByteArray = byteArrayOf(30)
        }
    }

    public object VotingPower : MichelsonInstruction, Prim {
        override val name: String = "VOTING_POWER"
        override val tag: ByteArray = byteArrayOf(123)
    }

    public data class Now(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "NOW"
            override val tag: ByteArray = byteArrayOf(64)
        }
    }

    public data class Level(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "LEVEL"
            override val tag: ByteArray = byteArrayOf(118)
        }
    }

    public data class Amount(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "AMOUNT"
            override val tag: ByteArray = byteArrayOf(19)
        }
    }

    public data class Balance(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "BALANCE"
            override val tag: ByteArray = byteArrayOf(21)
        }
    }

    public data class CheckSignature(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "CHECK_SIGNATURE"
            override val tag: ByteArray = byteArrayOf(24)
        }
    }

    public data class Blake2B(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "BLAKE2B"
            override val tag: ByteArray = byteArrayOf(14)
        }
    }

    public data class Keccak(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "KECCAK"
            override val tag: ByteArray = byteArrayOf(125)
        }
    }

    public data class Sha3(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SHA3"
            override val tag: ByteArray = byteArrayOf(126)
        }
    }

    public data class Sha256(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SHA256"
            override val tag: ByteArray = byteArrayOf(15)
        }
    }

    public data class Sha512(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SHA512"
            override val tag: ByteArray = byteArrayOf(16)
        }
    }

    public data class HashKey(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "HASH_KEY"
            override val tag: ByteArray = byteArrayOf(43)
        }
    }

    public data class Source(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SOURCE"
            override val tag: ByteArray = byteArrayOf(71)
        }
    }

    public data class Sender(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "SENDER"
            override val tag: ByteArray = byteArrayOf(72)
        }
    }

    public data class Address(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "ADDRESS"
            override val tag: ByteArray = byteArrayOf(84)
        }
    }

    public data class ChainId(public val metadata: Metadata = Metadata()) : MichelsonInstruction {

        override val annotations: List<Michelson.Annotation>
            get() = with(metadata) { listOfNotNull(variableName) }

        public data class Metadata(public val variableName: Michelson.Annotation.Variable? = null)

        public companion object : Prim {
            override val name: String = "CHAIN_ID"
            override val tag: ByteArray = byteArrayOf(117)
        }
    }

    public object TotalVotingPower : MichelsonInstruction, Prim {
        override val name: String = "TOTAL_VOTING_POWER"
        override val tag: ByteArray = byteArrayOf(124)
    }

    public object PairingCheck : MichelsonInstruction, Prim {
        override val name: String = "PAIRING_CHECK"
        override val tag: ByteArray = byteArrayOf(127)
    }

    public data class SaplingEmptyState(public val memoSize: MichelsonData.NaturalNumberConstant) : MichelsonInstruction {

        public constructor(memoSize: UByte) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UShort) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: UInt) : this(MichelsonData.NaturalNumberConstant(memoSize))
        public constructor(memoSize: ULong) : this(MichelsonData.NaturalNumberConstant(memoSize))

        public companion object : Prim {
            override val name: String = "SAPLING_EMPTY_STATE"
            override val tag: ByteArray = byteArrayOf((133).toByte())
        }
    }

    public object SaplingVerifyUpdate : MichelsonInstruction, Prim {
        override val name: String = "SAPLING_VERIFY_UPDATE"
        override val tag: ByteArray = byteArrayOf((134).toByte())
    }

    public object Ticket : MichelsonInstruction, Prim {
        override val name: String = "TICKET"
        override val tag: ByteArray = byteArrayOf((136).toByte())
    }

    public object ReadTicket : MichelsonInstruction, Prim {
        override val name: String = "READ_TICKET"
        override val tag: ByteArray = byteArrayOf((137).toByte())
    }

    public object SplitTicket : MichelsonInstruction, Prim {
        override val name: String = "SPLIT_TICKET"
        override val tag: ByteArray = byteArrayOf((138).toByte())
    }

    public object JoinTickets : MichelsonInstruction, Prim {
        override val name: String = "JOIN_TICKETS"
        override val tag: ByteArray = byteArrayOf((139).toByte())
    }

    public object OpenChest : MichelsonInstruction, Prim {
        override val name: String = "OPEN_CHEST"
        override val tag: ByteArray = byteArrayOf((143).toByte())
    }

    public sealed interface Prim : MichelsonData.Prim {
        public companion object {
            public val values: List<Prim>
                get() = listOf(
                    Drop,
                    Dup,
                    Swap,
                    Dig,
                    Dug,
                    Push,
                    Some,
                    None,
                    Unit,
                    Never,
                    IfNone,
                    Pair,
                    Car,
                    Cdr,
                    Unpair,
                    Left,
                    Right,
                    IfLeft,
                    Nil,
                    Cons,
                    IfCons,
                    Size,
                    EmptySet,
                    EmptyMap,
                    EmptyBigMap,
                    Map,
                    Iter,
                    Mem,
                    Get,
                    Update,
                    GetAndUpdate,
                    If,
                    Loop,
                    LoopLeft,
                    Lambda,
                    Exec,
                    Apply,
                    Dip,
                    Failwith,
                    Cast,
                    Rename,
                    Concat,
                    Slice,
                    Pack,
                    Unpack,
                    Add,
                    Sub,
                    Mul,
                    Ediv,
                    Abs,
                    Isnat,
                    Int,
                    Neg,
                    Lsl,
                    Lsr,
                    Or,
                    And,
                    Xor,
                    Not,
                    Compare,
                    Eq,
                    Neq,
                    Lt,
                    Gt,
                    Le,
                    Ge,
                    Self,
                    SelfAddress,
                    Contract,
                    TransferTokens,
                    SetDelegate,
                    CreateContract,
                    ImplicitAccount,
                    VotingPower,
                    Now,
                    Level,
                    Amount,
                    Balance,
                    CheckSignature,
                    Blake2B,
                    Keccak,
                    Sha3,
                    Sha256,
                    Sha512,
                    HashKey,
                    Source,
                    Sender,
                    Address,
                    ChainId,
                    TotalVotingPower,
                    PairingCheck,
                    SaplingEmptyState,
                    SaplingVerifyUpdate,
                    Ticket,
                    ReadTicket,
                    SplitTicket,
                    JoinTickets,
                    OpenChest,
                )
        }
    }

    public companion object {
        public fun Sequence(vararg instructions: MichelsonInstruction): Sequence = Sequence(instructions.toList())
    }
}