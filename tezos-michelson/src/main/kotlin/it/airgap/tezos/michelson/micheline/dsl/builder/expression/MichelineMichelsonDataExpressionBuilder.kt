@file:Suppress("FunctionName")

package it.airgap.tezos.michelson.micheline.dsl.builder.expression

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.replaceOrAdd
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.dsl.builder.node.MichelineNodeBuilder
import it.airgap.tezos.michelson.micheline.dsl.builder.node.MichelinePrimitiveApplicationBuilder
import it.airgap.tezos.michelson.micheline.dsl.builder.node.MichelinePrimitiveApplicationNoArgsBuilder
import it.airgap.tezos.michelson.micheline.dsl.builder.node.MichelinePrimitiveApplicationSingleArgBuilder

public typealias MichelineMichelsonDataExpressionBuilder = MichelineNodeBuilder<MichelsonData, MichelsonData.Prim>

public typealias MichelineMichelsonDataNoArgsBuilder = MichelinePrimitiveApplicationNoArgsBuilder<MichelsonData.Prim>
public typealias MichelineMichelsonDataSingleArgBuilder = MichelinePrimitiveApplicationSingleArgBuilder<MichelsonData, MichelsonData.Prim>
public typealias MichelineMichelsonDataWithArgsBuilder = MichelinePrimitiveApplicationBuilder<MichelsonData, MichelsonData.Prim>

public class MichelineMichelsonDataKeyValueBuilder internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    prim: MichelsonData.Prim,
) : MichelinePrimitiveApplicationNoArgsBuilder<MichelsonData.Prim>(michelsonToMichelineConverter, prim) {
    public fun key(builderAction: MichelineMichelsonDataExpressionBuilder.() -> Unit): MichelineMichelsonDataExpressionBuilder =
        MichelineMichelsonDataExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }
    public fun value(builderAction: MichelineMichelsonDataExpressionBuilder.() -> Unit): MichelineMichelsonDataExpressionBuilder =
        MichelineMichelsonDataExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(1, it) }
}

public fun MichelineMichelsonDataExpressionBuilder.Unit(builderAction: MichelineMichelsonDataNoArgsBuilder.() -> Unit = {}): MichelineMichelsonDataNoArgsBuilder =
    primitiveApplication(MichelsonData.Unit, builderAction)

public val MichelineMichelsonDataExpressionBuilder.Unit: MichelineMichelsonDataNoArgsBuilder get() = Unit {}

public fun MichelineMichelsonDataExpressionBuilder.True(builderAction: MichelineMichelsonDataNoArgsBuilder.() -> Unit = {}): MichelineMichelsonDataNoArgsBuilder =
    primitiveApplication(MichelsonData.True, builderAction)

public val MichelineMichelsonDataExpressionBuilder.True: MichelineMichelsonDataNoArgsBuilder get() = True {}

public fun MichelineMichelsonDataExpressionBuilder.False(builderAction: MichelineMichelsonDataNoArgsBuilder.() -> Unit = {}): MichelineMichelsonDataNoArgsBuilder =
    primitiveApplication(MichelsonData.False, builderAction)

public val MichelineMichelsonDataExpressionBuilder.False: MichelineMichelsonDataNoArgsBuilder get() = False {}

public fun MichelineMichelsonDataExpressionBuilder.Pair(builderAction: MichelineMichelsonDataWithArgsBuilder.() -> Unit): MichelineMichelsonDataWithArgsBuilder =
    primitiveApplication(MichelsonData.Pair, builderAction)

public fun MichelineMichelsonDataExpressionBuilder.Left(builderAction: MichelineMichelsonDataSingleArgBuilder.() -> Unit): MichelineMichelsonDataSingleArgBuilder =
    primitiveApplicationSingleArg(MichelsonData.Left, builderAction)

public fun MichelineMichelsonDataExpressionBuilder.Right(builderAction: MichelineMichelsonDataSingleArgBuilder.() -> Unit): MichelineMichelsonDataSingleArgBuilder =
    primitiveApplicationSingleArg(MichelsonData.Right, builderAction)

public fun MichelineMichelsonDataExpressionBuilder.Some(builderAction: MichelineMichelsonDataSingleArgBuilder.() -> Unit): MichelineMichelsonDataSingleArgBuilder =
    primitiveApplicationSingleArg(MichelsonData.Some, builderAction)

public fun MichelineMichelsonDataExpressionBuilder.None(builderAction: MichelineMichelsonDataNoArgsBuilder.() -> Unit = {}): MichelineMichelsonDataNoArgsBuilder =
    primitiveApplication(MichelsonData.None, builderAction)

public val MichelineMichelsonDataExpressionBuilder.None: MichelineMichelsonDataNoArgsBuilder get() = None {}

public fun MichelineMichelsonDataExpressionBuilder.Elt(builderAction: MichelineMichelsonDataKeyValueBuilder.() -> Unit): MichelineMichelsonDataKeyValueBuilder =
    MichelineMichelsonDataKeyValueBuilder(michelsonToMichelineConverter, MichelsonData.Elt).apply(builderAction).also { builders.add(it) }