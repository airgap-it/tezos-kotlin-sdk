package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.internal.di.michelson
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*

public fun micheline(
    michelsonToMichelineConverter: MichelsonToMichelineConverter = TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter,
    builderAction: MichelineMichelsonExpressionBuilder.() -> Unit = {}
): MichelineNode = MichelineMichelsonExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

public fun michelineType(
    michelsonToMichelineConverter: MichelsonToMichelineConverter = TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter,
    builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit = {}
): MichelineNode = MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

public fun michelineComparableType(
    michelsonToMichelineConverter: MichelsonToMichelineConverter = TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter,
    builderAction: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit = {}
): MichelineNode = MichelineMichelsonComparableTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

public fun michelineData(
    michelsonToMichelineConverter: MichelsonToMichelineConverter = TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter,
    builderAction: MichelineMichelsonDataExpressionBuilder.() -> Unit = {}
): MichelineNode = MichelineMichelsonDataExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

public fun michelineInstruction(
    michelsonToMichelineConverter: MichelsonToMichelineConverter = TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter,
    builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit = {}
): MichelineNode = MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()
