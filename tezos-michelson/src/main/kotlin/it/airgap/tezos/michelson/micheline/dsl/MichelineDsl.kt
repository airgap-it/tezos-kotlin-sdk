package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.internal.di.michelson
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*

public fun micheline(builderAction: MichelineMichelsonExpressionBuilder.() -> Unit = {}): MichelineNode = micheline(TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter, builderAction)
internal fun micheline(michelsonToMichelineConverter: MichelsonToMichelineConverter, builderAction: MichelineMichelsonExpressionBuilder.() -> Unit = {}): MichelineNode =
    MichelineMichelsonExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

public fun michelineType(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit = {}): MichelineNode = michelineType(TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter, builderAction)
internal fun michelineType(michelsonToMichelineConverter: MichelsonToMichelineConverter, builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit = {}): MichelineNode =
    MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

public fun michelineComparableType(builderAction: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit = {}): MichelineNode = michelineComparableType(TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter, builderAction)
internal fun michelineComparableType(michelsonToMichelineConverter: MichelsonToMichelineConverter, builderAction: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit = {}): MichelineNode =
    MichelineMichelsonComparableTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

public fun michelineData(builderAction: MichelineMichelsonDataExpressionBuilder.() -> Unit = {}): MichelineNode = michelineData(TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter, builderAction)
internal fun michelineData(michelsonToMichelineConverter: MichelsonToMichelineConverter, builderAction: MichelineMichelsonDataExpressionBuilder.() -> Unit = {}): MichelineNode =
    MichelineMichelsonDataExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

public fun michelineInstruction(builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit = {}): MichelineNode = michelineInstruction(TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter, builderAction)
internal fun michelineInstruction(michelsonToMichelineConverter: MichelsonToMichelineConverter, builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit = {}): MichelineNode =
    MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()
