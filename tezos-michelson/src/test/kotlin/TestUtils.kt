import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

// -- extensions --

fun <T> String.inRange(range: ClosedRange<T>): Boolean where T : Comparable<T> {
    val maxNumericIfNegative = range.start.toString().removePrefix("-")
    val maxNumericIfPositive = range.endInclusive.toString()

    val (abs, max) = if (startsWith("-")) {
        Pair(removePrefix("-"), maxNumericIfNegative)
    } else {
        Pair(this, maxNumericIfPositive)
    }

    return if (abs.length == max.length) abs <= max
    else abs.length < max.length
}

@Suppress("UNCHECKED_CAST")
fun <K, V> Map<K, V?>.filterValuesNotNull(): Map<K, V> = filterValues { it != null } as Map<K, V>

// -- data sets --

val michelsonMichelinePairs: List<Pair<Michelson, MichelineNode>>
    get() = michelsonDataMichelinePairs + michelsonTypeMichelinePairs

val michelsonDataMichelinePairs: List<Pair<MichelsonData, MichelineNode>>
    get() = listOf(
        MichelsonData.IntConstant(1) to MichelineLiteral.Integer(1),
        MichelsonData.StringConstant("string") to MichelineLiteral.String("string"),
        MichelsonData.ByteSequenceConstant("0x00") to MichelineLiteral.Bytes("0x00"),
        MichelsonData.Unit to MichelinePrimitiveApplication("Unit"),
        MichelsonData.True to MichelinePrimitiveApplication("True"),
        MichelsonData.False to MichelinePrimitiveApplication("False"),
        MichelsonData.Pair(
            MichelsonData.True,
            MichelsonData.False,
        ) to MichelinePrimitiveApplication(
            "Pair",
            listOf(MichelinePrimitiveApplication("True"), MichelinePrimitiveApplication("False")),
        ),
        MichelsonData.Left(MichelsonData.Unit) to MichelinePrimitiveApplication("Left", listOf(MichelinePrimitiveApplication("Unit"))),
        MichelsonData.Right(MichelsonData.Unit) to MichelinePrimitiveApplication("Right", listOf(MichelinePrimitiveApplication("Unit"))),
        MichelsonData.Some(MichelsonData.Unit) to MichelinePrimitiveApplication("Some", listOf(MichelinePrimitiveApplication("Unit"))),
        MichelsonData.None to MichelinePrimitiveApplication("None"),
        MichelsonData.Sequence() to MichelineSequence(),
        MichelsonData.Sequence(MichelsonData.Unit) to MichelineSequence(MichelinePrimitiveApplication("Unit")),
        MichelsonData.Sequence(
            MichelsonInstruction.Unit(),
            MichelsonData.Unit,
        ) to MichelineSequence(
            MichelinePrimitiveApplication("UNIT"),
            MichelinePrimitiveApplication("Unit"),
        ),
        MichelsonData.Sequence(
            MichelsonInstruction.Unit(),
            MichelsonData.Unit,
        ) to MichelineSequence(
            MichelinePrimitiveApplication("UNIT"),
            MichelinePrimitiveApplication("Unit"),
        ),
        MichelsonData.EltSequence(
            MichelsonData.Elt(MichelsonData.Unit, MichelsonData.Unit)
        ) to MichelineSequence(
            MichelinePrimitiveApplication(
                "Elt",
                listOf(
                    MichelinePrimitiveApplication("Unit"),
                    MichelinePrimitiveApplication("Unit"),
                ),
            ),
        ),
    ) + michelsonInstructionMichelinePairs

val michelsonInstructionMichelinePairs: List<Pair<MichelsonInstruction, MichelineNode>>
    get() = listOf(
        MichelsonInstruction.Sequence(MichelsonInstruction.Unit()) to MichelineSequence(MichelinePrimitiveApplication("UNIT")),
        MichelsonInstruction.Drop() to MichelinePrimitiveApplication("DROP"),
        MichelsonInstruction.Drop(1U) to MichelinePrimitiveApplication("DROP", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Dup() to MichelinePrimitiveApplication("DUP"),
        MichelsonInstruction.Dup(1U) to MichelinePrimitiveApplication("DUP", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Dup(
            1U,
            MichelsonInstruction.Dup.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("DUP", listOf(MichelineLiteral.Integer(1)), listOf("@variable")),
        MichelsonInstruction.Swap to MichelinePrimitiveApplication("SWAP"),
        MichelsonInstruction.Dig(1U) to MichelinePrimitiveApplication("DIG", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Dug(1U) to MichelinePrimitiveApplication("DUG", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Push(
            MichelsonComparableType.Unit(),
            MichelsonData.Unit,
        ) to MichelinePrimitiveApplication(
            "PUSH",
            listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("Unit"),
            ),
        ),
        MichelsonInstruction.Push(
            MichelsonComparableType.Unit(),
            MichelsonData.Unit,
            MichelsonInstruction.Push.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication(
            "PUSH",
            listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("Unit"),
            ),
            listOf("@variable"),
        ),
        MichelsonInstruction.Some() to MichelinePrimitiveApplication("SOME"),
        MichelsonInstruction.Some(
            MichelsonInstruction.Some.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SOME", annots = listOf(":type", "@variable")),
        MichelsonInstruction.None(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("NONE", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.None(
            MichelsonComparableType.Unit(),
            MichelsonInstruction.None.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("NONE", listOf(MichelinePrimitiveApplication("unit")), listOf(":type", "@variable")),
        MichelsonInstruction.Unit() to MichelinePrimitiveApplication("UNIT"),
        MichelsonInstruction.Unit(
            MichelsonInstruction.Unit.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("UNIT", annots = listOf(":type", "@variable")),
        MichelsonInstruction.Never to MichelinePrimitiveApplication("NEVER"),
        MichelsonInstruction.IfNone(
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
        ) to MichelinePrimitiveApplication(
            "IF_NONE",
            listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")), MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
        ),
        MichelsonInstruction.Pair() to MichelinePrimitiveApplication("PAIR"),
        MichelsonInstruction.Pair(1U) to MichelinePrimitiveApplication("PAIR", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Pair(
            1U,
            MichelsonInstruction.Pair.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("PAIR", listOf(MichelineLiteral.Integer(1)), listOf(":type", "@variable")),
        MichelsonInstruction.Car() to MichelinePrimitiveApplication("CAR"),
        MichelsonInstruction.Car(
            MichelsonInstruction.Car.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("CAR", annots = listOf("@variable")),
        MichelsonInstruction.Cdr() to MichelinePrimitiveApplication("CDR"),
        MichelsonInstruction.Cdr(
            MichelsonInstruction.Cdr.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("CDR", annots = listOf("@variable")),
        MichelsonInstruction.Unpair() to MichelinePrimitiveApplication("UNPAIR"),
        MichelsonInstruction.Unpair(1U) to MichelinePrimitiveApplication("UNPAIR", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Unpair(
            1U,
            MichelsonInstruction.Unpair.Metadata(
                firstVariableName = Michelson.Annotation.Variable("@variable1"),
                secondVariableName = Michelson.Annotation.Variable("@variable2"),
            ),
        ) to MichelinePrimitiveApplication("UNPAIR", listOf(MichelineLiteral.Integer(1)), listOf("@variable1", "@variable2")),
        MichelsonInstruction.Left(
            MichelsonComparableType.Unit(),
            MichelsonInstruction.Left.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("LEFT", listOf(MichelinePrimitiveApplication("unit")), listOf(":type", "@variable")),
        MichelsonInstruction.Right(
            MichelsonComparableType.Unit(),
            MichelsonInstruction.Right.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("RIGHT", listOf(MichelinePrimitiveApplication("unit")), listOf(":type", "@variable")),
        MichelsonInstruction.IfLeft(
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
        ) to MichelinePrimitiveApplication(
            "IF_LEFT",
            listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")), MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
        ),
        MichelsonInstruction.Nil(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("NIL", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.Nil(
            MichelsonComparableType.Unit(),
            MichelsonInstruction.Nil.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("NIL", listOf(MichelinePrimitiveApplication("unit")), listOf(":type", "@variable")),
        MichelsonInstruction.Cons() to MichelinePrimitiveApplication("CONS"),
        MichelsonInstruction.Cons(
            MichelsonInstruction.Cons.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("CONS", annots = listOf("@variable")),
        MichelsonInstruction.IfCons(
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
        ) to MichelinePrimitiveApplication(
            "IF_CONS",
            listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")), MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
        ),
        MichelsonInstruction.Size() to MichelinePrimitiveApplication("SIZE"),
        MichelsonInstruction.Size(
            MichelsonInstruction.Size.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SIZE", annots = listOf("@variable")),
        MichelsonInstruction.EmptySet(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("EMPTY_SET", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.EmptySet(
            MichelsonComparableType.Unit(),
            MichelsonInstruction.EmptySet.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("EMPTY_SET", listOf(MichelinePrimitiveApplication("unit")), listOf(":type", "@variable")),
        MichelsonInstruction.EmptyMap(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
        ) to MichelinePrimitiveApplication(
            "EMPTY_MAP",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonInstruction.EmptyMap(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
            MichelsonInstruction.EmptyMap.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication(
            "EMPTY_MAP",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
            listOf(":type", "@variable"),
        ),
        MichelsonInstruction.EmptyBigMap(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
        ) to MichelinePrimitiveApplication(
            "EMPTY_BIG_MAP",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonInstruction.EmptyBigMap(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
            MichelsonInstruction.EmptyBigMap.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication(
            "EMPTY_BIG_MAP",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
            listOf(":type", "@variable"),
        ),
        MichelsonInstruction.Map(MichelsonInstruction.Sequence(MichelsonInstruction.Unit())) to MichelinePrimitiveApplication("MAP", listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")))),
        MichelsonInstruction.Map(
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
            MichelsonInstruction.Map.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("MAP", listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT"))), listOf("@variable")),
        MichelsonInstruction.Iter(MichelsonInstruction.Sequence(MichelsonInstruction.Unit())) to MichelinePrimitiveApplication("ITER", listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")))),
        MichelsonInstruction.Mem() to MichelinePrimitiveApplication("MEM"),
        MichelsonInstruction.Mem(
            MichelsonInstruction.Mem.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("MEM", annots = listOf("@variable")),
        MichelsonInstruction.Get() to MichelinePrimitiveApplication("GET"),
        MichelsonInstruction.Get(1U) to MichelinePrimitiveApplication("GET", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Get(
            1U,
            MichelsonInstruction.Get.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("GET", listOf(MichelineLiteral.Integer(1)), listOf("@variable")),
        MichelsonInstruction.Update() to MichelinePrimitiveApplication("UPDATE"),
        MichelsonInstruction.Update(1U) to MichelinePrimitiveApplication("UPDATE", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.Update(
            1U,
            MichelsonInstruction.Update.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("UPDATE", listOf(MichelineLiteral.Integer(1)), listOf("@variable")),
        MichelsonInstruction.GetAndUpdate to MichelinePrimitiveApplication("GET_AND_UPDATE"),
        MichelsonInstruction.If(
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
        ) to MichelinePrimitiveApplication(
            "IF",
            listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")), MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
        ),
        MichelsonInstruction.Loop(MichelsonInstruction.Sequence(MichelsonInstruction.Unit())) to MichelinePrimitiveApplication("LOOP", listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")))),
        MichelsonInstruction.LoopLeft(MichelsonInstruction.Sequence(MichelsonInstruction.Unit())) to MichelinePrimitiveApplication("LOOP_LEFT", listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")))),
        MichelsonInstruction.Lambda(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
        ) to MichelinePrimitiveApplication(
            "LAMBDA",
            listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
                MichelineSequence(MichelinePrimitiveApplication("UNIT")),
            )
        ),
        MichelsonInstruction.Lambda(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
            MichelsonInstruction.Lambda.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication(
            "LAMBDA",
            listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
                MichelineSequence(MichelinePrimitiveApplication("UNIT")),
            ),
            listOf("@variable"),
        ),
        MichelsonInstruction.Exec() to MichelinePrimitiveApplication("EXEC"),
        MichelsonInstruction.Exec(
            MichelsonInstruction.Exec.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("EXEC", annots = listOf("@variable")),
        MichelsonInstruction.Apply to MichelinePrimitiveApplication("APPLY"),
        MichelsonInstruction.Dip(MichelsonInstruction.Sequence(MichelsonInstruction.Unit())) to MichelinePrimitiveApplication("DIP", listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")))),
        MichelsonInstruction.Dip(MichelsonInstruction.Sequence(MichelsonInstruction.Unit()), 1U) to MichelinePrimitiveApplication(
            "DIP",
            listOf(MichelineLiteral.Integer(1), MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
        ),
        MichelsonInstruction.Failwith to MichelinePrimitiveApplication("FAILWITH"),
        MichelsonInstruction.Cast() to MichelinePrimitiveApplication("CAST"),
        MichelsonInstruction.Cast(
            MichelsonInstruction.Cast.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("CAST", annots = listOf("@variable")),
        MichelsonInstruction.Rename() to MichelinePrimitiveApplication("RENAME"),
        MichelsonInstruction.Rename(
            MichelsonInstruction.Rename.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("RENAME", annots = listOf("@variable")),
        MichelsonInstruction.Concat() to MichelinePrimitiveApplication("CONCAT"),
        MichelsonInstruction.Concat(
            MichelsonInstruction.Concat.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("CONCAT", annots = listOf("@variable")),
        MichelsonInstruction.Slice to MichelinePrimitiveApplication("SLICE"),
        MichelsonInstruction.Pack to MichelinePrimitiveApplication("PACK"),
        MichelsonInstruction.Unpack(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("UNPACK", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.Add() to MichelinePrimitiveApplication("ADD"),
        MichelsonInstruction.Add(
            MichelsonInstruction.Add.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("ADD", annots = listOf("@variable")),
        MichelsonInstruction.Sub() to MichelinePrimitiveApplication("SUB"),
        MichelsonInstruction.Sub(
            MichelsonInstruction.Sub.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SUB", annots = listOf("@variable")),
        MichelsonInstruction.Mul() to MichelinePrimitiveApplication("MUL"),
        MichelsonInstruction.Mul(
            MichelsonInstruction.Mul.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("MUL", annots = listOf("@variable")),
        MichelsonInstruction.Ediv() to MichelinePrimitiveApplication("EDIV"),
        MichelsonInstruction.Ediv(
            MichelsonInstruction.Ediv.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("EDIV", annots = listOf("@variable")),
        MichelsonInstruction.Abs() to MichelinePrimitiveApplication("ABS"),
        MichelsonInstruction.Abs(
            MichelsonInstruction.Abs.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("ABS", annots = listOf("@variable")),
        MichelsonInstruction.Isnat() to MichelinePrimitiveApplication("ISNAT"),
        MichelsonInstruction.Isnat(
            MichelsonInstruction.Isnat.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("ISNAT", annots = listOf("@variable")),
        MichelsonInstruction.Int() to MichelinePrimitiveApplication("INT"),
        MichelsonInstruction.Int(
            MichelsonInstruction.Int.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("INT", annots = listOf("@variable")),
        MichelsonInstruction.Neg() to MichelinePrimitiveApplication("NEG"),
        MichelsonInstruction.Neg(
            MichelsonInstruction.Neg.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("NEG", annots = listOf("@variable")),
        MichelsonInstruction.Lsl() to MichelinePrimitiveApplication("LSL"),
        MichelsonInstruction.Lsl(
            MichelsonInstruction.Lsl.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("LSL", annots = listOf("@variable")),
        MichelsonInstruction.Lsr() to MichelinePrimitiveApplication("LSR"),
        MichelsonInstruction.Lsr(
            MichelsonInstruction.Lsr.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("LSR", annots = listOf("@variable")),
        MichelsonInstruction.Or() to MichelinePrimitiveApplication("OR"),
        MichelsonInstruction.Or(
            MichelsonInstruction.Or.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("OR", annots = listOf("@variable")),
        MichelsonInstruction.And() to MichelinePrimitiveApplication("AND"),
        MichelsonInstruction.And(
            MichelsonInstruction.And.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("AND", annots = listOf("@variable")),
        MichelsonInstruction.Xor() to MichelinePrimitiveApplication("XOR"),
        MichelsonInstruction.Xor(
            MichelsonInstruction.Xor.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("XOR", annots = listOf("@variable")),
        MichelsonInstruction.Not() to MichelinePrimitiveApplication("NOT"),
        MichelsonInstruction.Not(
            MichelsonInstruction.Not.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("NOT", annots = listOf("@variable")),
        MichelsonInstruction.Compare() to MichelinePrimitiveApplication("COMPARE"),
        MichelsonInstruction.Compare(
            MichelsonInstruction.Compare.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("COMPARE", annots = listOf("@variable")),
        MichelsonInstruction.Eq() to MichelinePrimitiveApplication("EQ"),
        MichelsonInstruction.Eq(
            MichelsonInstruction.Eq.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("EQ", annots = listOf("@variable")),
        MichelsonInstruction.Neq() to MichelinePrimitiveApplication("NEQ"),
        MichelsonInstruction.Neq(
            MichelsonInstruction.Neq.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("NEQ", annots = listOf("@variable")),
        MichelsonInstruction.Lt() to MichelinePrimitiveApplication("LT"),
        MichelsonInstruction.Lt(
            MichelsonInstruction.Lt.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("LT", annots = listOf("@variable")),
        MichelsonInstruction.Gt() to MichelinePrimitiveApplication("GT"),
        MichelsonInstruction.Gt(
            MichelsonInstruction.Gt.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("GT", annots = listOf("@variable")),
        MichelsonInstruction.Le() to MichelinePrimitiveApplication("LE"),
        MichelsonInstruction.Le(
            MichelsonInstruction.Le.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("LE", annots = listOf("@variable")),
        MichelsonInstruction.Ge() to MichelinePrimitiveApplication("GE"),
        MichelsonInstruction.Ge(
            MichelsonInstruction.Ge.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("GE", annots = listOf("@variable")),
        MichelsonInstruction.Self() to MichelinePrimitiveApplication("SELF"),
        MichelsonInstruction.Self(
            MichelsonInstruction.Self.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SELF", annots = listOf("@variable")),
        MichelsonInstruction.SelfAddress() to MichelinePrimitiveApplication("SELF_ADDRESS"),
        MichelsonInstruction.SelfAddress(
            MichelsonInstruction.SelfAddress.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SELF_ADDRESS", annots = listOf("@variable")),
        MichelsonInstruction.Contract(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("CONTRACT", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonInstruction.Contract(
            MichelsonComparableType.Unit(),
            MichelsonInstruction.Contract.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("CONTRACT", listOf(MichelinePrimitiveApplication("unit")), listOf("@variable")),
        MichelsonInstruction.TransferTokens to MichelinePrimitiveApplication("TRANSFER_TOKENS"),
        MichelsonInstruction.SetDelegate() to MichelinePrimitiveApplication("SET_DELEGATE"),
        MichelsonInstruction.SetDelegate(
            MichelsonInstruction.SetDelegate.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SET_DELEGATE", annots = listOf("@variable")),
        MichelsonInstruction.CreateContract(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
        ) to MichelinePrimitiveApplication(
            "CREATE_CONTRACT",
            listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
                MichelineSequence(MichelinePrimitiveApplication("UNIT")),
            )
        ),
        MichelsonInstruction.CreateContract(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
            MichelsonInstruction.Sequence(MichelsonInstruction.Unit()),
            MichelsonInstruction.CreateContract.Metadata(
                firstVariableName = Michelson.Annotation.Variable("@variable1"),
                secondVariableName = Michelson.Annotation.Variable("@variable2"),
            ),
        ) to MichelinePrimitiveApplication(
            "CREATE_CONTRACT",
            listOf(
                MichelinePrimitiveApplication("unit"),
                MichelinePrimitiveApplication("unit"),
                MichelineSequence(MichelinePrimitiveApplication("UNIT")),
            ),
            listOf("@variable1", "@variable2")
        ),
        MichelsonInstruction.ImplicitAccount() to MichelinePrimitiveApplication("IMPLICIT_ACCOUNT"),
        MichelsonInstruction.ImplicitAccount(
            MichelsonInstruction.ImplicitAccount.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("IMPLICIT_ACCOUNT", annots = listOf("@variable")),
        MichelsonInstruction.VotingPower to MichelinePrimitiveApplication("VOTING_POWER"),
        MichelsonInstruction.Now() to MichelinePrimitiveApplication("NOW"),
        MichelsonInstruction.Now(
            MichelsonInstruction.Now.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("NOW", annots = listOf("@variable")),
        MichelsonInstruction.Level() to MichelinePrimitiveApplication("LEVEL"),
        MichelsonInstruction.Level(
            MichelsonInstruction.Level.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("LEVEL", annots = listOf("@variable")),
        MichelsonInstruction.Amount() to MichelinePrimitiveApplication("AMOUNT"),
        MichelsonInstruction.Amount(
            MichelsonInstruction.Amount.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("AMOUNT", annots = listOf("@variable")),
        MichelsonInstruction.Balance() to MichelinePrimitiveApplication("BALANCE"),
        MichelsonInstruction.Balance(
            MichelsonInstruction.Balance.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("BALANCE", annots = listOf("@variable")),
        MichelsonInstruction.CheckSignature() to MichelinePrimitiveApplication("CHECK_SIGNATURE"),
        MichelsonInstruction.CheckSignature(
            MichelsonInstruction.CheckSignature.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("CHECK_SIGNATURE", annots = listOf("@variable")),
        MichelsonInstruction.Blake2B() to MichelinePrimitiveApplication("BLAKE2B"),
        MichelsonInstruction.Blake2B(
            MichelsonInstruction.Blake2B.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("BLAKE2B", annots = listOf("@variable")),
        MichelsonInstruction.Keccak() to MichelinePrimitiveApplication("KECCAK"),
        MichelsonInstruction.Keccak(
            MichelsonInstruction.Keccak.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("KECCAK", annots = listOf("@variable")),
        MichelsonInstruction.Sha3() to MichelinePrimitiveApplication("SHA3"),
        MichelsonInstruction.Sha3(
            MichelsonInstruction.Sha3.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SHA3", annots = listOf("@variable")),
        MichelsonInstruction.Sha256() to MichelinePrimitiveApplication("SHA256"),
        MichelsonInstruction.Sha256(
            MichelsonInstruction.Sha256.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SHA256", annots = listOf("@variable")),
        MichelsonInstruction.Sha512() to MichelinePrimitiveApplication("SHA512"),
        MichelsonInstruction.Sha512(
            MichelsonInstruction.Sha512.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SHA512", annots = listOf("@variable")),
        MichelsonInstruction.HashKey() to MichelinePrimitiveApplication("HASH_KEY"),
        MichelsonInstruction.HashKey(
            MichelsonInstruction.HashKey.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("HASH_KEY", annots = listOf("@variable")),
        MichelsonInstruction.Source() to MichelinePrimitiveApplication("SOURCE"),
        MichelsonInstruction.Source(
            MichelsonInstruction.Source.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SOURCE", annots = listOf("@variable")),
        MichelsonInstruction.Sender() to MichelinePrimitiveApplication("SENDER"),
        MichelsonInstruction.Sender(
            MichelsonInstruction.Sender.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("SENDER", annots = listOf("@variable")),
        MichelsonInstruction.Address() to MichelinePrimitiveApplication("ADDRESS"),
        MichelsonInstruction.Address(
            MichelsonInstruction.Address.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("ADDRESS", annots = listOf("@variable")),
        MichelsonInstruction.ChainId() to MichelinePrimitiveApplication("CHAIN_ID"),
        MichelsonInstruction.ChainId(
            MichelsonInstruction.ChainId.Metadata(
                variableName = Michelson.Annotation.Variable("@variable"),
            ),
        ) to MichelinePrimitiveApplication("CHAIN_ID", annots = listOf("@variable")),
        MichelsonInstruction.TotalVotingPower to MichelinePrimitiveApplication("TOTAL_VOTING_POWER"),
        MichelsonInstruction.PairingCheck to MichelinePrimitiveApplication("PAIRING_CHECK"),
        MichelsonInstruction.SaplingEmptyState(1U) to MichelinePrimitiveApplication("SAPLING_EMPTY_STATE", listOf(MichelineLiteral.Integer(1))),
        MichelsonInstruction.SaplingVerifyUpdate to MichelinePrimitiveApplication("SAPLING_VERIFY_UPDATE"),
        MichelsonInstruction.Ticket to MichelinePrimitiveApplication("TICKET"),
        MichelsonInstruction.ReadTicket to MichelinePrimitiveApplication("READ_TICKET"),
        MichelsonInstruction.SplitTicket to MichelinePrimitiveApplication("SPLIT_TICKET"),
        MichelsonInstruction.JoinTickets to MichelinePrimitiveApplication("JOIN_TICKETS"),
        MichelsonInstruction.OpenChest to MichelinePrimitiveApplication("OPEN_CHEST"),
    )

val michelsonTypeMichelinePairs: List<Pair<MichelsonType, MichelineNode>>
    get() = listOf(
        MichelsonType.Parameter(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("parameter", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Parameter(
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "parameter", 
            listOf(MichelinePrimitiveApplication("unit")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Storage(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("storage", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Storage(
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "storage", 
            listOf(MichelinePrimitiveApplication("unit")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Code(MichelsonInstruction.Unit()) to MichelinePrimitiveApplication("code", listOf(MichelinePrimitiveApplication("UNIT"))),
        MichelsonType.Code(
            MichelsonInstruction.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "code", 
            listOf(MichelinePrimitiveApplication("UNIT")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Option(MichelsonType.Operation()) to MichelinePrimitiveApplication("option", listOf(MichelinePrimitiveApplication("operation"))),
        MichelsonType.Option(
            MichelsonType.Operation(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "option",
            listOf(MichelinePrimitiveApplication("operation")),
            listOf(":type", "%field"),
        ),
        MichelsonType.List(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("list", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.List(
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication("list", listOf(MichelinePrimitiveApplication("unit")), listOf(":type", "%field"),),
        MichelsonType.Set(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("set", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Set(
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "set", 
            listOf(MichelinePrimitiveApplication("unit")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Operation() to MichelinePrimitiveApplication("operation"),
        MichelsonType.Operation(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication("operation", annots = listOf(":type", "%field")),
        MichelsonType.Contract(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("contract", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Contract(
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "contract", 
            listOf(MichelinePrimitiveApplication("unit")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Ticket(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("ticket", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonType.Ticket(
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "ticket",
            listOf(MichelinePrimitiveApplication("unit")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Pair(
            MichelsonType.Operation(),
            MichelsonType.Operation(),
        ) to MichelinePrimitiveApplication(
            "pair",
            listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("operation")),
        ),
        MichelsonType.Pair(
            MichelsonType.Operation(),
            MichelsonType.Operation(),
            metadata = MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "pair",
            listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("operation")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Pair(
            MichelsonType.Operation(),
            MichelsonComparableType.String(),
        ) to MichelinePrimitiveApplication(
            "pair",
            listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("string")),
        ),
        MichelsonType.Pair(
            MichelsonType.Operation(),
            MichelsonComparableType.String(),
            metadata = MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "pair",
            listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("string")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Or(
            MichelsonType.Operation(),
            MichelsonComparableType.Unit(),
        ) to MichelinePrimitiveApplication(
            "or",
            listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonType.Or(
            MichelsonType.Operation(),
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "or",
            listOf(MichelinePrimitiveApplication("operation"), MichelinePrimitiveApplication("unit")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Lambda(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
        ) to MichelinePrimitiveApplication(
            "lambda",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonType.Lambda(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "lambda",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Map(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
        ) to MichelinePrimitiveApplication(
            "map",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonType.Map(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "map",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
            listOf(":type", "%field"),
        ),
        MichelsonType.BigMap(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
        ) to MichelinePrimitiveApplication(
            "big_map",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonType.BigMap(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "big_map",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
            listOf(":type", "%field"),
        ),
        MichelsonType.Bls12_381G1() to MichelinePrimitiveApplication("bls12_381_g1"),
        MichelsonType.Bls12_381G1(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication("bls12_381_g1", annots = listOf(":type", "%field"),),
        MichelsonType.Bls12_381G2() to MichelinePrimitiveApplication("bls12_381_g2"),
        MichelsonType.Bls12_381G2(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication("bls12_381_g2", annots = listOf(":type", "%field"),),
        MichelsonType.Bls12_381Fr() to MichelinePrimitiveApplication("bls12_381_fr"),
        MichelsonType.Bls12_381Fr(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication("bls12_381_fr", annots = listOf(":type", "%field"),),
        MichelsonType.SaplingTransaction(1U) to MichelinePrimitiveApplication("sapling_transaction", listOf(MichelineLiteral.Integer(1))),
        MichelsonType.SaplingTransaction(
            1U,
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "sapling_transaction",
            listOf(MichelineLiteral.Integer(1)),
            listOf(":type", "%field"),
        ),
        MichelsonType.SaplingState(1U) to MichelinePrimitiveApplication("sapling_state", listOf(MichelineLiteral.Integer(1))),
        MichelsonType.SaplingState(
            1U,
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "sapling_state",
            listOf(MichelineLiteral.Integer(1)),
            listOf(":type", "%field"),
        ),
        MichelsonType.Chest() to MichelinePrimitiveApplication("chest"),
        MichelsonType.Chest(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication("chest", annots = listOf(":type", "%field"),),
        MichelsonType.ChestKey() to MichelinePrimitiveApplication("chest_key"),
        MichelsonType.ChestKey(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication("chest_key", annots = listOf(":type", "%field"),),
    ) + michelsonComparableTypeMichelinePairs

val michelsonComparableTypeMichelinePairs: List<Pair<MichelsonComparableType, MichelineNode>>
    get() = listOf(
        MichelsonComparableType.Unit() to MichelinePrimitiveApplication("unit"),
        MichelsonComparableType.Unit(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("unit", annots = listOf(":type", "%field")),
        MichelsonComparableType.Never() to MichelinePrimitiveApplication("never"),
        MichelsonComparableType.Unit(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("unit", annots = listOf(":type", "%field")),
        MichelsonComparableType.Bool() to MichelinePrimitiveApplication("bool"),
        MichelsonComparableType.Bool(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("bool", annots = listOf(":type", "%field")),
        MichelsonComparableType.Int() to MichelinePrimitiveApplication("int"),
        MichelsonComparableType.Int(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("int", annots = listOf(":type", "%field")),
        MichelsonComparableType.Nat() to MichelinePrimitiveApplication("nat"),
        MichelsonComparableType.Nat(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("nat", annots = listOf(":type", "%field")),
        MichelsonComparableType.String() to MichelinePrimitiveApplication("string"),
        MichelsonComparableType.String(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("string", annots = listOf(":type", "%field")),
        MichelsonComparableType.ChainId() to MichelinePrimitiveApplication("chain_id"),
        MichelsonComparableType.ChainId(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("chain_id", annots = listOf(":type", "%field")),
        MichelsonComparableType.Bytes() to MichelinePrimitiveApplication("bytes"),
        MichelsonComparableType.Bytes(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("bytes", annots = listOf(":type", "%field")),
        MichelsonComparableType.Mutez() to MichelinePrimitiveApplication("mutez"),
        MichelsonComparableType.Mutez(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("mutez", annots = listOf(":type", "%field")),
        MichelsonComparableType.KeyHash() to MichelinePrimitiveApplication("key_hash"),
        MichelsonComparableType.KeyHash(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("key_hash", annots = listOf(":type", "%field")),
        MichelsonComparableType.Key() to MichelinePrimitiveApplication("key"),
        MichelsonComparableType.Key(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("key", annots = listOf(":type", "%field")),
        MichelsonComparableType.Signature() to MichelinePrimitiveApplication("signature"),
        MichelsonComparableType.Signature(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("signature", annots = listOf(":type", "%field")),
        MichelsonComparableType.Timestamp() to MichelinePrimitiveApplication("timestamp"),
        MichelsonComparableType.Timestamp(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("timestamp", annots = listOf(":type", "%field")),
        MichelsonComparableType.Address() to MichelinePrimitiveApplication("address"),
        MichelsonComparableType.Address(
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            )
        ) to MichelinePrimitiveApplication("address", annots = listOf(":type", "%field")),
        MichelsonComparableType.Option(MichelsonComparableType.Unit()) to MichelinePrimitiveApplication("option", listOf(MichelinePrimitiveApplication("unit"))),
        MichelsonComparableType.Option(
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication("option", listOf(MichelinePrimitiveApplication("unit")), listOf(":type", "%field")),
        MichelsonComparableType.Or(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
        ) to MichelinePrimitiveApplication(
            "or",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
        ),
        MichelsonComparableType.Or(
            MichelsonComparableType.Unit(),
            MichelsonComparableType.Unit(),
            MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "or",
            listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("unit")),
            listOf(":type", "%field"),
        ),
        MichelsonComparableType.Pair(
            MichelsonComparableType.Int(),
            MichelsonComparableType.String(),
        ) to MichelinePrimitiveApplication(
            "pair",
            listOf(MichelinePrimitiveApplication("int"), MichelinePrimitiveApplication("string")),
        ),
        MichelsonComparableType.Pair(
            MichelsonComparableType.Int(),
            MichelsonComparableType.String(),
            metadata = MichelsonType.Metadata(
                typeName = Michelson.Annotation.Type(":type"),
                fieldName = Michelson.Annotation.Field("%field"),
            ),
        ) to MichelinePrimitiveApplication(
            "pair",
            listOf(MichelinePrimitiveApplication("int"), MichelinePrimitiveApplication("string")),
            listOf(":type", "%field"),
        ),
    )