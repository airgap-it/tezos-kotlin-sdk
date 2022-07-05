package it.airgap.tezos.michelson.micheline.dsl.builder.expression

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.replaceOrAdd
import it.airgap.tezos.michelson.internal.utils.failWithUnexpectedMichelsonPrim
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.dsl.builder.node.*

public typealias MichelineMichelsonTypeExpressionBuilder = MichelineNodeBuilder<MichelsonType, MichelsonType.Prim>

public typealias MichelineMichelsonTypeNoArgsBuilder = MichelinePrimitiveApplicationNoArgsBuilder<MichelsonType.Prim>
public typealias MichelineMichelsonTypeSingleArgBuilder = MichelinePrimitiveApplicationSingleArgBuilder<MichelsonType, MichelsonType.Prim>
public typealias MichelineMichelsonTypeIntegerArgBuilder = MichelinePrimitiveApplicationIntegerArgBuilder<MichelsonType.Prim>

public class MichelineMichelsonTypeCodeBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, Micheline>
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonType.Prim>(michelsonToMichelineConverter, MichelsonType.Code) {
    public fun arg(builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit): MichelineMichelsonInstructionExpressionBuilder =
        MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }
}

public class MichelineMichelsonTypeOptionBuilder<T : MichelsonType, G : MichelsonType.Prim> @PublishedApi internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    prim: G,
) : MichelinePrimitiveApplicationNoArgsBuilder<G>(michelsonToMichelineConverter, prim) {
    public fun arg(builderAction: MichelineNodeBuilder<T, G>.() -> Unit): MichelineNodeBuilder<T, G> = MichelineNodeBuilder<T, G>(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }
}

public class MichelineMichelsonTypeOrBuilder<T : MichelsonType, G : MichelsonType.Prim> @PublishedApi internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    prim: G,
) : MichelinePrimitiveApplicationNoArgsBuilder<G>(michelsonToMichelineConverter, prim) {
    public fun lhs(builderAction: MichelineNodeBuilder<T, G>.() -> Unit): MichelineNodeBuilder<T, G> = MichelineNodeBuilder<T, G>(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }
    public fun rhs(builderAction: MichelineNodeBuilder<T, G>.() -> Unit): MichelineNodeBuilder<T, G> = MichelineNodeBuilder<T, G>(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(1, it) }
}

public class MichelineMichelsonTypeLambdaBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, Micheline>
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonType.Prim>(michelsonToMichelineConverter, MichelsonType.Lambda) {
    public fun parameter(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit): MichelineMichelsonTypeExpressionBuilder =
        MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }

    public fun returnType(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit): MichelineMichelsonTypeExpressionBuilder =
        MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(1, it) }
}

public class MichelineMichelsonTypeMapBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    prim: MichelsonType.Prim,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonType.Prim>(michelsonToMichelineConverter, prim) {
    public fun key(builderAction: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit): MichelineMichelsonComparableTypeExpressionBuilder =
        MichelineMichelsonComparableTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }

    public fun value(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit): MichelineMichelsonTypeExpressionBuilder =
        MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(1, it) }
}

public inline fun <T : MichelsonType, reified G : MichelsonType.Prim> MichelineNodeBuilder<T, G>.option(
    builderAction: MichelineMichelsonTypeOptionBuilder<T, G>.() -> Unit
): MichelineMichelsonTypeOptionBuilder<T, G> {
    val prim: G = when (G::class) {
        MichelsonType.Prim::class -> MichelsonType.Option as G
        MichelsonComparableType.Prim::class -> MichelsonComparableType.Option as G
        else -> null
    } ?: failWithUnexpectedMichelsonPrim(G::class, "pair")

    return MichelineMichelsonTypeOptionBuilder<T, G>(michelsonToMichelineConverter, prim).apply(builderAction).also { builders.add(it) }
}

public fun MichelineMichelsonTypeExpressionBuilder.parameter(builderAction: MichelineMichelsonTypeSingleArgBuilder.() -> Unit): MichelineMichelsonTypeSingleArgBuilder =
    primitiveApplication(MichelsonType.Parameter, builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.storage(builderAction: MichelineMichelsonTypeSingleArgBuilder.() -> Unit): MichelineMichelsonTypeSingleArgBuilder =
    primitiveApplication(MichelsonType.Storage, builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.code(builderAction: MichelineMichelsonTypeCodeBuilder.() -> Unit): MichelineMichelsonTypeCodeBuilder =
    MichelineMichelsonTypeCodeBuilder(michelsonToMichelineConverter).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonTypeExpressionBuilder.option(builderAction: MichelineMichelsonTypeSingleArgBuilder.() -> Unit): MichelineMichelsonTypeSingleArgBuilder =
    primitiveApplication(MichelsonType.Option, builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.list(builderAction: MichelineMichelsonTypeSingleArgBuilder.() -> Unit): MichelineMichelsonTypeSingleArgBuilder =
    primitiveApplicationSingleArg(MichelsonType.List, builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.set(builderAction: MichelineMichelsonComparableTypeSingleArgBuilder.() -> Unit): MichelineMichelsonComparableTypeSingleArgBuilder =
    primitiveApplicationSingleArg(MichelsonType.Set, builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.operation(builderAction: MichelineMichelsonTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonTypeNoArgsBuilder =
    primitiveApplication(MichelsonType.Operation, builderAction)

public val MichelineMichelsonTypeExpressionBuilder.operation: MichelineMichelsonTypeNoArgsBuilder get() = operation {}

public fun MichelineMichelsonTypeExpressionBuilder.contract(builderAction: MichelineMichelsonTypeSingleArgBuilder.() -> Unit): MichelineMichelsonTypeSingleArgBuilder =
    primitiveApplication(MichelsonType.Contract, builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.ticket(builderAction: MichelineMichelsonTypeSingleArgBuilder.() -> Unit): MichelineMichelsonTypeSingleArgBuilder =
    primitiveApplication(MichelsonType.Ticket, builderAction)

public inline fun <T : MichelsonType, reified G : MichelsonType.Prim> MichelineNodeBuilder<T, G>.pair(
    builderAction: MichelinePrimitiveApplicationBuilder<T, G>.() -> Unit
): MichelinePrimitiveApplicationBuilder<T, G> {
    val prim: G = when (G::class) {
        MichelsonType.Prim::class -> MichelsonType.Pair as G
        MichelsonComparableType.Prim::class -> MichelsonComparableType.Pair as G
        else -> null
    } ?: failWithUnexpectedMichelsonPrim(G::class, "pair")

    return MichelinePrimitiveApplicationBuilder<T, G>(michelsonToMichelineConverter, prim).apply(builderAction).also { builders.add(it) }
}

public inline fun <T : MichelsonType, reified G : MichelsonType.Prim> MichelineNodeBuilder<T, G>.or(
    builderAction: MichelineMichelsonTypeOrBuilder<T, G>.() -> Unit
): MichelineMichelsonTypeOrBuilder<T, G> {
    val prim: G = when (G::class) {
        MichelsonType.Prim::class -> MichelsonType.Or as G
        MichelsonComparableType.Prim::class -> MichelsonComparableType.Or as G
        else -> null
    } ?: failWithUnexpectedMichelsonPrim(G::class, "or")

    return MichelineMichelsonTypeOrBuilder<T, G>(michelsonToMichelineConverter, prim).apply(builderAction).also { builders.add(it) }
}

public fun MichelineMichelsonTypeExpressionBuilder.lambda(builderAction: MichelineMichelsonTypeLambdaBuilder.() -> Unit): MichelineMichelsonTypeLambdaBuilder =
    MichelineMichelsonTypeLambdaBuilder(michelsonToMichelineConverter).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonTypeExpressionBuilder.map(builderAction: MichelineMichelsonTypeMapBuilder.() -> Unit): MichelineMichelsonTypeMapBuilder =
    MichelineMichelsonTypeMapBuilder(michelsonToMichelineConverter, MichelsonType.Map).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonTypeExpressionBuilder.bigMap(builderAction: MichelineMichelsonTypeMapBuilder.() -> Unit): MichelineMichelsonTypeMapBuilder =
    MichelineMichelsonTypeMapBuilder(michelsonToMichelineConverter, MichelsonType.BigMap).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonTypeExpressionBuilder.bls12_381G1(builderAction: MichelineMichelsonTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonTypeNoArgsBuilder =
    primitiveApplication(MichelsonType.Bls12_381G1, builderAction)

public val MichelineMichelsonTypeExpressionBuilder.bls12_381G1: MichelineMichelsonTypeNoArgsBuilder get() = bls12_381G1 {}

public fun MichelineMichelsonTypeExpressionBuilder.bls12_381G2(builderAction: MichelineMichelsonTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonTypeNoArgsBuilder =
    primitiveApplication(MichelsonType.Bls12_381G2, builderAction)

public val MichelineMichelsonTypeExpressionBuilder.bls12_381G2: MichelineMichelsonTypeNoArgsBuilder get() = bls12_381G2 {}

public fun MichelineMichelsonTypeExpressionBuilder.bls12_381Fr(builderAction: MichelineMichelsonTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonTypeNoArgsBuilder =
    primitiveApplication(MichelsonType.Bls12_381Fr, builderAction)

public val MichelineMichelsonTypeExpressionBuilder.bls12_381Fr: MichelineMichelsonTypeNoArgsBuilder get() = bls12_381Fr {}

public fun MichelineMichelsonTypeExpressionBuilder.saplingTransaction(
    memoSize: String,
    builderAction: MichelineMichelsonTypeIntegerArgBuilder.() -> Unit = {},
): MichelineMichelsonTypeIntegerArgBuilder =
    MichelineMichelsonTypeIntegerArgBuilder(michelsonToMichelineConverter, MichelsonType.SaplingTransaction, MichelineLiteral.Integer(memoSize)).apply(builderAction).also { builders.add(it) }

public fun MichelineMichelsonTypeExpressionBuilder.saplingTransaction(
    memoSize: UByte,
    builderAction: MichelineMichelsonTypeIntegerArgBuilder.() -> Unit = {},
): MichelineMichelsonTypeIntegerArgBuilder = saplingTransaction(memoSize.toString(), builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.saplingTransaction(
    memoSize: UShort,
    builderAction: MichelineMichelsonTypeIntegerArgBuilder.() -> Unit = {},
): MichelineMichelsonTypeIntegerArgBuilder = saplingTransaction(memoSize.toString(), builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.saplingTransaction(
    memoSize: UInt,
    builderAction: MichelineMichelsonTypeIntegerArgBuilder.() -> Unit = {},
): MichelineMichelsonTypeIntegerArgBuilder = saplingTransaction(memoSize.toString(), builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.saplingTransaction(
    memoSize: ULong,
    builderAction: MichelineMichelsonTypeIntegerArgBuilder.() -> Unit = {},
): MichelineMichelsonTypeIntegerArgBuilder = saplingTransaction(memoSize.toString(), builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.saplingState(
    memoSize: String,
    builderAction: MichelineMichelsonTypeIntegerArgBuilder.() -> Unit = {},
): MichelineMichelsonTypeIntegerArgBuilder =
    MichelineMichelsonTypeIntegerArgBuilder(michelsonToMichelineConverter, MichelsonType.SaplingState, MichelineLiteral.Integer(memoSize)).apply(builderAction).also { builders.add(it) }
public fun MichelineMichelsonTypeExpressionBuilder.saplingState(
    memoSize: UByte,
    builderAction: MichelineMichelsonTypeIntegerArgBuilder.() -> Unit = {},
): MichelineMichelsonTypeIntegerArgBuilder = saplingState(memoSize.toString(), builderAction)
public fun MichelineMichelsonTypeExpressionBuilder.saplingState(
    memoSize: UShort,
    builderAction: MichelineMichelsonTypeIntegerArgBuilder.() -> Unit = {},
): MichelineMichelsonTypeIntegerArgBuilder = saplingState(memoSize.toString(), builderAction)
public fun MichelineMichelsonTypeExpressionBuilder.saplingState(
    memoSize: UInt,
    builderAction: MichelineMichelsonTypeIntegerArgBuilder.() -> Unit = {},
): MichelineMichelsonTypeIntegerArgBuilder = saplingState(memoSize.toString(), builderAction)
public fun MichelineMichelsonTypeExpressionBuilder.saplingState(
    memoSize: ULong,
    builderAction: MichelineMichelsonTypeIntegerArgBuilder.() -> Unit = {},
): MichelineMichelsonTypeIntegerArgBuilder = saplingState(memoSize.toString(), builderAction)

public fun MichelineMichelsonTypeExpressionBuilder.chest(builderAction: MichelineMichelsonTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonTypeNoArgsBuilder =
    primitiveApplication(MichelsonType.Chest, builderAction)

public val MichelineMichelsonTypeExpressionBuilder.chest: MichelineMichelsonTypeNoArgsBuilder get() = chest {}

public fun MichelineMichelsonTypeExpressionBuilder.chestKey(builderAction: MichelineMichelsonTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonTypeNoArgsBuilder =
    primitiveApplication(MichelsonType.ChestKey, builderAction)

public val MichelineMichelsonTypeExpressionBuilder.chestKey: MichelineMichelsonTypeNoArgsBuilder get() = chestKey {}
