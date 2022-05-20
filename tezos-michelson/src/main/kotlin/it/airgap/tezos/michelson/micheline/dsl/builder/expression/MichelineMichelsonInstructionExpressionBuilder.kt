@file:Suppress("FunctionName")

package it.airgap.tezos.michelson.micheline.dsl.builder.expression

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.utils.replaceOrAdd
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonInstruction
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.dsl.builder.MichelineBuilder
import it.airgap.tezos.michelson.micheline.dsl.builder.node.*

public typealias MichelineMichelsonInstructionExpressionBuilder = MichelineNodeBuilder<MichelsonInstruction, MichelsonInstruction.Prim>

public typealias MichelineMichelsonInstructionNoArgsBuilder = MichelinePrimitiveApplicationNoArgsBuilder<MichelsonInstruction.Prim>
public typealias MichelineMichelsonInstructionSingleArgBuilder = MichelinePrimitiveApplicationSingleArgBuilder<MichelsonInstruction, MichelsonInstruction.Prim>
public typealias MichelineMichelsonInstructionOptionalIntegerArgBuilder = MichelinePrimitiveApplicationOptionalIntegerArgBuilder<MichelsonInstruction.Prim>
public typealias MichelineMichelsonInstructionIntegerArgBuilder = MichelinePrimitiveApplicationIntegerArgBuilder<MichelsonInstruction.Prim>

public class MichelineMichelsonInstructionConditionalBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    prim: MichelsonInstruction.Prim,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonInstruction.Prim>(michelsonToMichelineConverter, prim) {
    public fun ifBranch(builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit): MichelineMichelsonInstructionExpressionBuilder =
        MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it.mapNodeToSequence()) }

    public fun elseBranch(builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit): MichelineMichelsonInstructionExpressionBuilder =
        MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(1, it.mapNodeToSequence()) }
}

public class MichelineMichelsonInstructionPushBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonInstruction.Prim>(michelsonToMichelineConverter, MichelsonInstruction.Push) {
    public fun type(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit): MichelineMichelsonTypeExpressionBuilder =
        MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }

    public fun value(builderAction: MichelineMichelsonDataExpressionBuilder.() -> Unit): MichelineMichelsonDataExpressionBuilder =
        MichelineMichelsonDataExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(1, it) }
}

public class MichelineMichelsonInstructionKeyValueBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    prim: MichelsonInstruction.Prim,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonInstruction.Prim>(michelsonToMichelineConverter, prim) {
    public fun key(builderAction: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit): MichelineMichelsonComparableTypeExpressionBuilder =
        MichelineMichelsonComparableTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }

    public fun value(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit): MichelineMichelsonTypeExpressionBuilder =
        MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(1, it) }
}

public class MichelineMichelsonInstructionSingleExpressionArgBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    prim: MichelsonInstruction.Prim,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonInstruction.Prim>(michelsonToMichelineConverter, prim) {
    public fun expression(builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit): MichelineMichelsonInstructionExpressionBuilder =
        MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it.mapNodeToSequence()) }
}

public class MichelineMichelsonInstructionSingleBodyArgBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    prim: MichelsonInstruction.Prim,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonInstruction.Prim>(michelsonToMichelineConverter, prim) {
    public fun body(builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit): MichelineMichelsonInstructionExpressionBuilder =
        MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it.mapNodeToSequence()) }
}

public class MichelineMichelsonInstructionLambdaBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonInstruction.Prim>(michelsonToMichelineConverter, MichelsonInstruction.Lambda) {
    public fun parameter(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit): MichelineMichelsonTypeExpressionBuilder =
        MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }

    public fun returnType(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit): MichelineMichelsonTypeExpressionBuilder =
        MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(1, it) }

    public fun body(builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit): MichelineMichelsonInstructionExpressionBuilder =
        MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(2, it.mapNodeToSequence()) }
}

public class MichelineMichelsonInstructionDipBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonInstruction.Prim>(michelsonToMichelineConverter, MichelsonInstruction.Dip) {
    public infix fun n(value: String): MichelineBuilder = MichelineBuilder { MichelineLiteral.Integer(value) }.also { args.replaceOrAdd(0, it) }
    public infix fun n(value: UByte): MichelineBuilder = n(value.toString())
    public infix fun n(value: UShort): MichelineBuilder = n(value.toString())
    public infix fun n(value: UInt): MichelineBuilder = n(value.toString())
    public infix fun n(value: ULong): MichelineBuilder = n(value.toString())

    public fun instruction(builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit): MichelineMichelsonInstructionExpressionBuilder =
        MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(1, it.mapNodeToSequence()) }
}

public class MichelineMichelsonInstructionTypeArgBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    prim: MichelsonInstruction.Prim,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonInstruction.Prim>(michelsonToMichelineConverter, prim) {
    public fun arg(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit): MichelineMichelsonTypeExpressionBuilder =
        MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }
}

public class MichelineMichelsonInstructionCreateContractBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonInstruction.Prim>(michelsonToMichelineConverter, MichelsonInstruction.CreateContract) {
    public fun parameter(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit): MichelineMichelsonTypeExpressionBuilder =
        MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }

    public fun storage(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit): MichelineMichelsonTypeExpressionBuilder =
        MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(1, it) }

    public fun code(builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit): MichelineMichelsonInstructionExpressionBuilder =
        MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(2, it.mapNodeToSequence()) }
}

public fun MichelineMichelsonInstructionExpressionBuilder.DROP(builderAction: MichelineMichelsonInstructionOptionalIntegerArgBuilder.() -> Unit = {}): MichelineMichelsonInstructionOptionalIntegerArgBuilder =
    MichelineMichelsonInstructionOptionalIntegerArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Drop).apply(builderAction).also { builders.add(it) }

public infix fun MichelineMichelsonInstructionExpressionBuilder.DROP(n: String): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DROP { arg(n) }
public infix fun MichelineMichelsonInstructionExpressionBuilder.DROP(n: UByte): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DROP(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DROP(n: UShort): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DROP(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DROP(n: UInt): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DROP(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DROP(n: ULong): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DROP(n.toString())

public val MichelineMichelsonInstructionExpressionBuilder.DROP: MichelineMichelsonInstructionOptionalIntegerArgBuilder get() = DROP {}

public fun MichelineMichelsonInstructionExpressionBuilder.DUP(builderAction: MichelineMichelsonInstructionOptionalIntegerArgBuilder.() -> Unit = {}): MichelineMichelsonInstructionOptionalIntegerArgBuilder =
    MichelineMichelsonInstructionOptionalIntegerArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Dup).apply(builderAction).also { builders.add(it) }

public infix fun MichelineMichelsonInstructionExpressionBuilder.DUP(n: String): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DUP { arg(n) }
public infix fun MichelineMichelsonInstructionExpressionBuilder.DUP(n: UByte): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DUP(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DUP(n: UShort): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DUP(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DUP(n: UInt): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DUP(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DUP(n: ULong): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DUP(n.toString())

public val MichelineMichelsonInstructionExpressionBuilder.DUP: MichelineMichelsonInstructionOptionalIntegerArgBuilder get() = DUP {}

public fun MichelineMichelsonInstructionExpressionBuilder.SWAP(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Swap, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SWAP: MichelineMichelsonInstructionNoArgsBuilder get() = SWAP {}

public fun MichelineMichelsonInstructionExpressionBuilder.DIG(builderAction: MichelineMichelsonInstructionOptionalIntegerArgBuilder.() -> Unit = {}): MichelineMichelsonInstructionOptionalIntegerArgBuilder =
    MichelineMichelsonInstructionOptionalIntegerArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Dig).apply(builderAction).also { builders.add(it) }

public infix fun MichelineMichelsonInstructionExpressionBuilder.DIG(n: String): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DIG { arg(n) }
public infix fun MichelineMichelsonInstructionExpressionBuilder.DIG(n: UByte): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DIG(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DIG(n: UShort): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DIG(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DIG(n: UInt): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DIG(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DIG(n: ULong): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DIG(n.toString())

public val MichelineMichelsonInstructionExpressionBuilder.DIG: MichelineMichelsonInstructionOptionalIntegerArgBuilder get() = DIG {}

public fun MichelineMichelsonInstructionExpressionBuilder.DUG(builderAction: MichelineMichelsonInstructionOptionalIntegerArgBuilder.() -> Unit = {}): MichelineMichelsonInstructionOptionalIntegerArgBuilder =
    MichelineMichelsonInstructionOptionalIntegerArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Dug).apply(builderAction).also { builders.add(it) }

public infix fun MichelineMichelsonInstructionExpressionBuilder.DUG(n: String): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DUG { arg(n) }
public infix fun MichelineMichelsonInstructionExpressionBuilder.DUG(n: UByte): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DUG(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DUG(n: UShort): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DUG(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DUG(n: UInt): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DUG(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.DUG(n: ULong): MichelineMichelsonInstructionOptionalIntegerArgBuilder = DUG(n.toString())

public val MichelineMichelsonInstructionExpressionBuilder.DUG: MichelineMichelsonInstructionOptionalIntegerArgBuilder get() = DUG {}

public fun MichelineMichelsonInstructionExpressionBuilder.PUSH(builderAction: MichelineMichelsonInstructionPushBuilder.() -> Unit): MichelineMichelsonInstructionPushBuilder =
    MichelineMichelsonInstructionPushBuilder(michelsonToMichelineConverter).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.SOME(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Some, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SOME: MichelineMichelsonInstructionNoArgsBuilder get() = SOME {}

public fun MichelineMichelsonInstructionExpressionBuilder.NONE(builderAction: MichelineMichelsonInstructionSingleArgBuilder.() -> Unit): MichelineMichelsonInstructionSingleArgBuilder =
    primitiveApplicationSingleArg(MichelsonInstruction.None, builderAction)

public fun MichelineMichelsonInstructionExpressionBuilder.UNIT(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Unit, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.UNIT: MichelineMichelsonInstructionNoArgsBuilder get() = UNIT {}

public fun MichelineMichelsonInstructionExpressionBuilder.NEVER(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Never, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.NEVER: MichelineMichelsonInstructionNoArgsBuilder get() = NEVER {}

public fun MichelineMichelsonInstructionExpressionBuilder.IF_NONE(builderAction: MichelineMichelsonInstructionConditionalBuilder.() -> Unit): MichelineMichelsonInstructionConditionalBuilder =
    MichelineMichelsonInstructionConditionalBuilder(michelsonToMichelineConverter, MichelsonInstruction.IfNone).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.PAIR(builderAction: MichelineMichelsonInstructionOptionalIntegerArgBuilder.() -> Unit = {}): MichelineMichelsonInstructionOptionalIntegerArgBuilder =
    MichelineMichelsonInstructionOptionalIntegerArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Pair).apply(builderAction).also { builders.add(it) }

public infix fun MichelineMichelsonInstructionExpressionBuilder.PAIR(n: String): MichelineMichelsonInstructionOptionalIntegerArgBuilder = PAIR { arg(n) }
public infix fun MichelineMichelsonInstructionExpressionBuilder.PAIR(n: UByte): MichelineMichelsonInstructionOptionalIntegerArgBuilder = PAIR(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.PAIR(n: UShort): MichelineMichelsonInstructionOptionalIntegerArgBuilder = PAIR(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.PAIR(n: UInt): MichelineMichelsonInstructionOptionalIntegerArgBuilder = PAIR(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.PAIR(n: ULong): MichelineMichelsonInstructionOptionalIntegerArgBuilder = PAIR(n.toString())

public val MichelineMichelsonInstructionExpressionBuilder.PAIR: MichelineMichelsonInstructionOptionalIntegerArgBuilder get() = PAIR {}

public fun MichelineMichelsonInstructionExpressionBuilder.CAR(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Car, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.CAR: MichelineMichelsonInstructionNoArgsBuilder get() = CAR {}

public fun MichelineMichelsonInstructionExpressionBuilder.CDR(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Cdr, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.CDR: MichelineMichelsonInstructionNoArgsBuilder get() = CDR {}

public fun MichelineMichelsonInstructionExpressionBuilder.UNPAIR(builderAction: MichelineMichelsonInstructionOptionalIntegerArgBuilder.() -> Unit = {}): MichelineMichelsonInstructionOptionalIntegerArgBuilder =
    MichelineMichelsonInstructionOptionalIntegerArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Unpair).apply(builderAction).also { builders.add(it) }

public infix fun MichelineMichelsonInstructionExpressionBuilder.UNPAIR(n: String): MichelineMichelsonInstructionOptionalIntegerArgBuilder = UNPAIR { arg(n) }
public infix fun MichelineMichelsonInstructionExpressionBuilder.UNPAIR(n: UByte): MichelineMichelsonInstructionOptionalIntegerArgBuilder = UNPAIR(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.UNPAIR(n: UShort): MichelineMichelsonInstructionOptionalIntegerArgBuilder = UNPAIR(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.UNPAIR(n: UInt): MichelineMichelsonInstructionOptionalIntegerArgBuilder = UNPAIR(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.UNPAIR(n: ULong): MichelineMichelsonInstructionOptionalIntegerArgBuilder = UNPAIR(n.toString())

public val MichelineMichelsonInstructionExpressionBuilder.UNPAIR: MichelineMichelsonInstructionOptionalIntegerArgBuilder get() = UNPAIR {}

public fun MichelineMichelsonInstructionExpressionBuilder.LEFT(builderAction: MichelineMichelsonInstructionSingleArgBuilder.() -> Unit): MichelineMichelsonInstructionSingleArgBuilder =
    primitiveApplicationSingleArg(MichelsonInstruction.Left, builderAction)

public fun MichelineMichelsonInstructionExpressionBuilder.RIGHT(builderAction: MichelineMichelsonInstructionSingleArgBuilder.() -> Unit): MichelineMichelsonInstructionSingleArgBuilder =
    primitiveApplicationSingleArg(MichelsonInstruction.Right, builderAction)

public fun MichelineMichelsonInstructionExpressionBuilder.IF_LEFT(builderAction: MichelineMichelsonInstructionConditionalBuilder.() -> Unit): MichelineMichelsonInstructionConditionalBuilder =
    MichelineMichelsonInstructionConditionalBuilder(michelsonToMichelineConverter, MichelsonInstruction.IfLeft).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.NIL(builderAction: MichelineMichelsonInstructionSingleArgBuilder.() -> Unit): MichelineMichelsonInstructionSingleArgBuilder =
    primitiveApplicationSingleArg(MichelsonInstruction.Nil, builderAction)

public fun MichelineMichelsonInstructionExpressionBuilder.CONS(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Cons, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.CONS: MichelineMichelsonInstructionNoArgsBuilder get() = CONS {}

public fun MichelineMichelsonInstructionExpressionBuilder.IF_CONS(builderAction: MichelineMichelsonInstructionConditionalBuilder.() -> Unit): MichelineMichelsonInstructionConditionalBuilder =
    MichelineMichelsonInstructionConditionalBuilder(michelsonToMichelineConverter, MichelsonInstruction.IfCons).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.SIZE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Size, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SIZE: MichelineMichelsonInstructionNoArgsBuilder get() = SIZE {}

public fun MichelineMichelsonInstructionExpressionBuilder.EMPTY_SET(builderAction: MichelineMichelsonInstructionSingleArgBuilder.() -> Unit): MichelineMichelsonInstructionSingleArgBuilder =
    primitiveApplicationSingleArg(MichelsonInstruction.EmptySet, builderAction)

public fun MichelineMichelsonInstructionExpressionBuilder.EMPTY_MAP(builderAction: MichelineMichelsonInstructionKeyValueBuilder.() -> Unit): MichelineMichelsonInstructionKeyValueBuilder =
    MichelineMichelsonInstructionKeyValueBuilder(michelsonToMichelineConverter, MichelsonInstruction.EmptyMap).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.EMPTY_BIG_MAP(builderAction: MichelineMichelsonInstructionKeyValueBuilder.() -> Unit): MichelineMichelsonInstructionKeyValueBuilder =
    MichelineMichelsonInstructionKeyValueBuilder(michelsonToMichelineConverter, MichelsonInstruction.EmptyBigMap).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.MAP(builderAction: MichelineMichelsonInstructionSingleExpressionArgBuilder.() -> Unit): MichelineMichelsonInstructionSingleExpressionArgBuilder =
    MichelineMichelsonInstructionSingleExpressionArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Map).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.ITER(builderAction: MichelineMichelsonInstructionSingleExpressionArgBuilder.() -> Unit): MichelineMichelsonInstructionSingleExpressionArgBuilder =
    MichelineMichelsonInstructionSingleExpressionArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Iter).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.MEM(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Mem, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.MEM: MichelineMichelsonInstructionNoArgsBuilder get() = MEM {}

public fun MichelineMichelsonInstructionExpressionBuilder.GET(builderAction: MichelineMichelsonInstructionOptionalIntegerArgBuilder.() -> Unit = {}): MichelineMichelsonInstructionOptionalIntegerArgBuilder =
    MichelineMichelsonInstructionOptionalIntegerArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Get).apply(builderAction).also { builders.add(it) }

public infix fun MichelineMichelsonInstructionExpressionBuilder.GET(n: String): MichelineMichelsonInstructionOptionalIntegerArgBuilder = GET { arg(n) }
public infix fun MichelineMichelsonInstructionExpressionBuilder.GET(n: UByte): MichelineMichelsonInstructionOptionalIntegerArgBuilder = GET(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.GET(n: UShort): MichelineMichelsonInstructionOptionalIntegerArgBuilder = GET(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.GET(n: UInt): MichelineMichelsonInstructionOptionalIntegerArgBuilder = GET(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.GET(n: ULong): MichelineMichelsonInstructionOptionalIntegerArgBuilder = GET(n.toString())

public val MichelineMichelsonInstructionExpressionBuilder.GET: MichelineMichelsonInstructionOptionalIntegerArgBuilder get() = GET {}

public fun MichelineMichelsonInstructionExpressionBuilder.UPDATE(builderAction: MichelineMichelsonInstructionOptionalIntegerArgBuilder.() -> Unit = {}): MichelineMichelsonInstructionOptionalIntegerArgBuilder =
    MichelineMichelsonInstructionOptionalIntegerArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Update).apply(builderAction).also { builders.add(it) }

public infix fun MichelineMichelsonInstructionExpressionBuilder.UPDATE(n: String): MichelineMichelsonInstructionOptionalIntegerArgBuilder = UPDATE { arg(n) }
public infix fun MichelineMichelsonInstructionExpressionBuilder.UPDATE(n: UByte): MichelineMichelsonInstructionOptionalIntegerArgBuilder = UPDATE(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.UPDATE(n: UShort): MichelineMichelsonInstructionOptionalIntegerArgBuilder = UPDATE(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.UPDATE(n: UInt): MichelineMichelsonInstructionOptionalIntegerArgBuilder = UPDATE(n.toString())
public infix fun MichelineMichelsonInstructionExpressionBuilder.UPDATE(n: ULong): MichelineMichelsonInstructionOptionalIntegerArgBuilder = UPDATE(n.toString())

public val MichelineMichelsonInstructionExpressionBuilder.UPDATE: MichelineMichelsonInstructionOptionalIntegerArgBuilder get() = UPDATE {}

public fun MichelineMichelsonInstructionExpressionBuilder.GET_AND_UPDATE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.GetAndUpdate, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.GET_AND_UPDATE: MichelineMichelsonInstructionNoArgsBuilder get() = GET_AND_UPDATE {}

public fun MichelineMichelsonInstructionExpressionBuilder.IF(builderAction: MichelineMichelsonInstructionConditionalBuilder.() -> Unit): MichelineMichelsonInstructionConditionalBuilder =
    MichelineMichelsonInstructionConditionalBuilder(michelsonToMichelineConverter, MichelsonInstruction.If).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.LOOP(builderAction: MichelineMichelsonInstructionSingleBodyArgBuilder.() -> Unit): MichelineMichelsonInstructionSingleBodyArgBuilder =
    MichelineMichelsonInstructionSingleBodyArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Loop).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.LOOP_LEFT(builderAction: MichelineMichelsonInstructionSingleBodyArgBuilder.() -> Unit): MichelineMichelsonInstructionSingleBodyArgBuilder =
    MichelineMichelsonInstructionSingleBodyArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.LoopLeft).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.LAMBDA(builderAction: MichelineMichelsonInstructionLambdaBuilder.() -> Unit): MichelineMichelsonInstructionLambdaBuilder =
    MichelineMichelsonInstructionLambdaBuilder(michelsonToMichelineConverter).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.EXEC(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Exec, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.EXEC: MichelineMichelsonInstructionNoArgsBuilder get() = EXEC {}

public fun MichelineMichelsonInstructionExpressionBuilder.APPLY(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Apply, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.APPLY: MichelineMichelsonInstructionNoArgsBuilder get() = APPLY {}

public fun MichelineMichelsonInstructionExpressionBuilder.DIP(builderAction: MichelineMichelsonInstructionDipBuilder.() -> Unit): MichelineMichelsonInstructionDipBuilder =
    MichelineMichelsonInstructionDipBuilder(michelsonToMichelineConverter).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.FAILWITH(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Failwith, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.FAILWITH: MichelineMichelsonInstructionNoArgsBuilder get() = FAILWITH {}

public fun MichelineMichelsonInstructionExpressionBuilder.CAST(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Cast, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.CAST: MichelineMichelsonInstructionNoArgsBuilder get() = CAST {}

public fun MichelineMichelsonInstructionExpressionBuilder.RENAME(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Rename, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.RENAME: MichelineMichelsonInstructionNoArgsBuilder get() = RENAME {}

public fun MichelineMichelsonInstructionExpressionBuilder.CONCAT(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Concat, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.CONCAT: MichelineMichelsonInstructionNoArgsBuilder get() = CONCAT {}

public fun MichelineMichelsonInstructionExpressionBuilder.SLICE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Slice, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SLICE: MichelineMichelsonInstructionNoArgsBuilder get() = SLICE {}

public fun MichelineMichelsonInstructionExpressionBuilder.PACK(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Pack, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.PACK: MichelineMichelsonInstructionNoArgsBuilder get() = PACK {}

public fun MichelineMichelsonInstructionExpressionBuilder.UNPACK(builderAction: MichelineMichelsonInstructionTypeArgBuilder.() -> Unit): MichelineMichelsonInstructionTypeArgBuilder =
    MichelineMichelsonInstructionTypeArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Unpack).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.ADD(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Add, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.ADD: MichelineMichelsonInstructionNoArgsBuilder get() = ADD {}

public fun MichelineMichelsonInstructionExpressionBuilder.SUB(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Sub, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SUB: MichelineMichelsonInstructionNoArgsBuilder get() = SUB {}

public fun MichelineMichelsonInstructionExpressionBuilder.MUL(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Mul, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.MUL: MichelineMichelsonInstructionNoArgsBuilder get() = MUL {}

public fun MichelineMichelsonInstructionExpressionBuilder.EDIV(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Ediv, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.EDIV: MichelineMichelsonInstructionNoArgsBuilder get() = EDIV {}

public fun MichelineMichelsonInstructionExpressionBuilder.ABS(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Abs, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.ABS: MichelineMichelsonInstructionNoArgsBuilder get() = ABS {}

public fun MichelineMichelsonInstructionExpressionBuilder.ISNAT(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Isnat, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.ISNAT: MichelineMichelsonInstructionNoArgsBuilder get() = ISNAT {}

public fun MichelineMichelsonInstructionExpressionBuilder.INT(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Int, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.INT: MichelineMichelsonInstructionNoArgsBuilder get() = INT {}

public fun MichelineMichelsonInstructionExpressionBuilder.NEG(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Neg, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.NEG: MichelineMichelsonInstructionNoArgsBuilder get() = NEG {}

public fun MichelineMichelsonInstructionExpressionBuilder.LSL(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Lsl, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.LSL: MichelineMichelsonInstructionNoArgsBuilder get() = LSL {}

public fun MichelineMichelsonInstructionExpressionBuilder.LSR(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Lsr, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.LSR: MichelineMichelsonInstructionNoArgsBuilder get() = LSR {}

public fun MichelineMichelsonInstructionExpressionBuilder.OR(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Or, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.OR: MichelineMichelsonInstructionNoArgsBuilder get() = OR {}

public fun MichelineMichelsonInstructionExpressionBuilder.AND(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.And, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.AND: MichelineMichelsonInstructionNoArgsBuilder get() = AND {}

public fun MichelineMichelsonInstructionExpressionBuilder.XOR(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Xor, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.XOR: MichelineMichelsonInstructionNoArgsBuilder get() = XOR {}

public fun MichelineMichelsonInstructionExpressionBuilder.NOT(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Not, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.NOT: MichelineMichelsonInstructionNoArgsBuilder get() = NOT {}

public fun MichelineMichelsonInstructionExpressionBuilder.COMPARE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Compare, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.COMPARE: MichelineMichelsonInstructionNoArgsBuilder get() = COMPARE {}

public fun MichelineMichelsonInstructionExpressionBuilder.EQ(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Eq, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.EQ: MichelineMichelsonInstructionNoArgsBuilder get() = EQ {}

public fun MichelineMichelsonInstructionExpressionBuilder.NEQ(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Neq, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.NEQ: MichelineMichelsonInstructionNoArgsBuilder get() = NEQ {}

public fun MichelineMichelsonInstructionExpressionBuilder.LT(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Lt, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.LT: MichelineMichelsonInstructionNoArgsBuilder get() = LT {}

public fun MichelineMichelsonInstructionExpressionBuilder.GT(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Gt, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.GT: MichelineMichelsonInstructionNoArgsBuilder get() = GT {}

public fun MichelineMichelsonInstructionExpressionBuilder.LE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Le, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.LE: MichelineMichelsonInstructionNoArgsBuilder get() = LE {}

public fun MichelineMichelsonInstructionExpressionBuilder.GE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Ge, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.GE: MichelineMichelsonInstructionNoArgsBuilder get() = GE {}

public fun MichelineMichelsonInstructionExpressionBuilder.SELF(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Self, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SELF: MichelineMichelsonInstructionNoArgsBuilder get() = SELF {}

public fun MichelineMichelsonInstructionExpressionBuilder.SELF_ADDRESS(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.SelfAddress, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SELF_ADDRESS: MichelineMichelsonInstructionNoArgsBuilder get() = SELF_ADDRESS {}

public fun MichelineMichelsonInstructionExpressionBuilder.CONTRACT(builderAction: MichelineMichelsonInstructionTypeArgBuilder.() -> Unit): MichelineMichelsonInstructionTypeArgBuilder =
    MichelineMichelsonInstructionTypeArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.Contract).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.TRANSFER_TOKENS(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.TransferTokens, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.TRANSFER_TOKENS: MichelineMichelsonInstructionNoArgsBuilder get() = TRANSFER_TOKENS {}

public fun MichelineMichelsonInstructionExpressionBuilder.SET_DELEGATE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.SetDelegate, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SET_DELEGATE: MichelineMichelsonInstructionNoArgsBuilder get() = SET_DELEGATE {}

public fun MichelineMichelsonInstructionExpressionBuilder.CREATE_CONTRACT(builderAction: MichelineMichelsonInstructionCreateContractBuilder.() -> Unit): MichelineMichelsonInstructionCreateContractBuilder =
    MichelineMichelsonInstructionCreateContractBuilder(michelsonToMichelineConverter).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.IMPLICIT_ACCOUNT(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.ImplicitAccount, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.IMPLICIT_ACCOUNT: MichelineMichelsonInstructionNoArgsBuilder get() = IMPLICIT_ACCOUNT {}

public fun MichelineMichelsonInstructionExpressionBuilder.VOTING_POWER(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.VotingPower, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.VOTING_POWER: MichelineMichelsonInstructionNoArgsBuilder get() = VOTING_POWER {}

public fun MichelineMichelsonInstructionExpressionBuilder.NOW(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Now, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.NOW: MichelineMichelsonInstructionNoArgsBuilder get() = NOW {}

public fun MichelineMichelsonInstructionExpressionBuilder.LEVEL(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Level, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.LEVEL: MichelineMichelsonInstructionNoArgsBuilder get() = LEVEL {}

public fun MichelineMichelsonInstructionExpressionBuilder.AMOUNT(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Amount, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.AMOUNT: MichelineMichelsonInstructionNoArgsBuilder get() = AMOUNT {}

public fun MichelineMichelsonInstructionExpressionBuilder.BALANCE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Balance, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.BALANCE: MichelineMichelsonInstructionNoArgsBuilder get() = BALANCE {}

public fun MichelineMichelsonInstructionExpressionBuilder.CHECK_SIGNATURE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.CheckSignature, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.CHECK_SIGNATURE: MichelineMichelsonInstructionNoArgsBuilder get() = CHECK_SIGNATURE {}

public fun MichelineMichelsonInstructionExpressionBuilder.BLAKE2B(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Blake2B, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.BLAKE2B: MichelineMichelsonInstructionNoArgsBuilder get() = BLAKE2B {}

public fun MichelineMichelsonInstructionExpressionBuilder.KECCAK(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Keccak, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.KECCAK: MichelineMichelsonInstructionNoArgsBuilder get() = KECCAK {}

public fun MichelineMichelsonInstructionExpressionBuilder.SHA3(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Sha3, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SHA3: MichelineMichelsonInstructionNoArgsBuilder get() = SHA3 {}

public fun MichelineMichelsonInstructionExpressionBuilder.SHA256(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Sha256, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SHA256: MichelineMichelsonInstructionNoArgsBuilder get() = SHA256 {}

public fun MichelineMichelsonInstructionExpressionBuilder.SHA512(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Sha512, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SHA512: MichelineMichelsonInstructionNoArgsBuilder get() = SHA512 {}

public fun MichelineMichelsonInstructionExpressionBuilder.HASH_KEY(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.HashKey, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.HASH_KEY: MichelineMichelsonInstructionNoArgsBuilder get() = HASH_KEY {}

public fun MichelineMichelsonInstructionExpressionBuilder.SOURCE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Source, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SOURCE: MichelineMichelsonInstructionNoArgsBuilder get() = SOURCE {}

public fun MichelineMichelsonInstructionExpressionBuilder.SENDER(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Sender, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SENDER: MichelineMichelsonInstructionNoArgsBuilder get() = SENDER {}

public fun MichelineMichelsonInstructionExpressionBuilder.ADDRESS(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Address, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.ADDRESS: MichelineMichelsonInstructionNoArgsBuilder get() = ADDRESS {}

public fun MichelineMichelsonInstructionExpressionBuilder.CHAIN_ID(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.ChainId, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.CHAIN_ID: MichelineMichelsonInstructionNoArgsBuilder get() = CHAIN_ID {}

public fun MichelineMichelsonInstructionExpressionBuilder.TOTAL_VOTING_POWER(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.TotalVotingPower, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.TOTAL_VOTING_POWER: MichelineMichelsonInstructionNoArgsBuilder get() = TOTAL_VOTING_POWER {}

public fun MichelineMichelsonInstructionExpressionBuilder.PAIRING_CHECK(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.PairingCheck, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.PAIRING_CHECK: MichelineMichelsonInstructionNoArgsBuilder get() = PAIRING_CHECK {}

public fun MichelineMichelsonInstructionExpressionBuilder.SAPLING_EMPTY_STATE(
    memoSize: String, builderAction: MichelineMichelsonInstructionIntegerArgBuilder.() -> Unit = {}
): MichelineMichelsonInstructionIntegerArgBuilder =
    MichelineMichelsonInstructionIntegerArgBuilder(michelsonToMichelineConverter, MichelsonInstruction.SaplingEmptyState, MichelineLiteral.Integer(memoSize)).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonInstructionExpressionBuilder.SAPLING_EMPTY_STATE(
    memoSize: UByte, builderAction: MichelineMichelsonInstructionIntegerArgBuilder.() -> Unit = {}
): MichelineMichelsonInstructionIntegerArgBuilder = SAPLING_EMPTY_STATE(memoSize.toString(), builderAction)

public fun MichelineMichelsonInstructionExpressionBuilder.SAPLING_EMPTY_STATE(
    memoSize: UShort, builderAction: MichelineMichelsonInstructionIntegerArgBuilder.() -> Unit = {}
): MichelineMichelsonInstructionIntegerArgBuilder = SAPLING_EMPTY_STATE(memoSize.toString(), builderAction)

public fun MichelineMichelsonInstructionExpressionBuilder.SAPLING_EMPTY_STATE(
    memoSize: UInt, builderAction: MichelineMichelsonInstructionIntegerArgBuilder.() -> Unit = {}
): MichelineMichelsonInstructionIntegerArgBuilder = SAPLING_EMPTY_STATE(memoSize.toString(), builderAction)

public fun MichelineMichelsonInstructionExpressionBuilder.SAPLING_EMPTY_STATE(
    memoSize: ULong, builderAction: MichelineMichelsonInstructionIntegerArgBuilder.() -> Unit = {}
): MichelineMichelsonInstructionIntegerArgBuilder = SAPLING_EMPTY_STATE(memoSize.toString(), builderAction)

public fun MichelineMichelsonInstructionExpressionBuilder.SAPLING_VERIFY_UPDATE(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.SaplingVerifyUpdate, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SAPLING_VERIFY_UPDATE: MichelineMichelsonInstructionNoArgsBuilder get() = SAPLING_VERIFY_UPDATE {}

public fun MichelineMichelsonInstructionExpressionBuilder.TICKET(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.Ticket, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.TICKET: MichelineMichelsonInstructionNoArgsBuilder get() = TICKET {}

public fun MichelineMichelsonInstructionExpressionBuilder.READ_TICKET(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.ReadTicket, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.READ_TICKET: MichelineMichelsonInstructionNoArgsBuilder get() = READ_TICKET {}

public fun MichelineMichelsonInstructionExpressionBuilder.SPLIT_TICKET(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.SplitTicket, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.SPLIT_TICKET: MichelineMichelsonInstructionNoArgsBuilder get() = SPLIT_TICKET {}

public fun MichelineMichelsonInstructionExpressionBuilder.JOIN_TICKETS(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.JoinTickets, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.JOIN_TICKETS: MichelineMichelsonInstructionNoArgsBuilder get() = JOIN_TICKETS {}

public fun MichelineMichelsonInstructionExpressionBuilder.OPEN_CHEST(builderAction: MichelineMichelsonInstructionNoArgsBuilder.() -> Unit = {}): MichelineMichelsonInstructionNoArgsBuilder =
    primitiveApplication(MichelsonInstruction.OpenChest, builderAction)

public val MichelineMichelsonInstructionExpressionBuilder.OPEN_CHEST: MichelineMichelsonInstructionNoArgsBuilder get() = OPEN_CHEST {}