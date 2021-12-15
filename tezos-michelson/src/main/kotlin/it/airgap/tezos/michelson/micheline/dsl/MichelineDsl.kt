package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*

public fun micheline(builderAction: MichelineMichelsonExpressionBuilder.() -> Unit = {}): MichelineNode = MichelineMichelsonExpressionBuilder().apply(builderAction).build()

public fun michelineType(builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit = {}): MichelineNode = MichelineMichelsonTypeExpressionBuilder().apply(builderAction).build()
public fun michelineComparableType(builderAction: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit = {}): MichelineNode = MichelineMichelsonComparableTypeExpressionBuilder().apply(builderAction).build()

public fun michelineData(builderAction: MichelineMichelsonDataExpressionBuilder.() -> Unit = {}): MichelineNode = MichelineMichelsonDataExpressionBuilder().apply(builderAction).build()
public fun michelineInstruction(builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit = {}): MichelineNode = MichelineMichelsonInstructionExpressionBuilder().apply(builderAction).build()
