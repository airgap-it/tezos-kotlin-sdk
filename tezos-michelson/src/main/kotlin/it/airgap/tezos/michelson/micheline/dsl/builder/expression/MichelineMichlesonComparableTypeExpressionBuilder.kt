package it.airgap.tezos.michelson.micheline.dsl.builder.expression

import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.micheline.dsl.builder.node.MichelineNodeBuilder
import it.airgap.tezos.michelson.micheline.dsl.builder.node.MichelinePrimitiveApplicationBuilder
import it.airgap.tezos.michelson.micheline.dsl.builder.node.MichelinePrimitiveApplicationNoArgsBuilder
import it.airgap.tezos.michelson.micheline.dsl.builder.node.MichelinePrimitiveApplicationSingleArgBuilder

public typealias MichelineMichelsonComparableTypeExpressionBuilder = MichelineNodeBuilder<MichelsonComparableType, MichelsonComparableType.GrammarType>

public typealias MichelineMichelsonComparableTypeNoArgsBuilder = MichelinePrimitiveApplicationNoArgsBuilder<MichelsonComparableType.GrammarType>
public typealias MichelineMichelsonComparableTypeSingleArgBuilder = MichelinePrimitiveApplicationSingleArgBuilder<MichelsonComparableType, MichelsonComparableType.GrammarType>
public typealias MichelineMichelsonComparableTypeWithArgsBuilder = MichelinePrimitiveApplicationBuilder<MichelsonComparableType, MichelsonComparableType.GrammarType>

public fun MichelineMichelsonComparableTypeExpressionBuilder.unit(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Unit, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.unit: MichelineMichelsonComparableTypeNoArgsBuilder get() = unit {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.never(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Never, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.never: MichelineMichelsonComparableTypeNoArgsBuilder get() = never {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.bool(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Bool, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.bool: MichelineMichelsonComparableTypeNoArgsBuilder get() = bool {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.int(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Int, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.int: MichelineMichelsonComparableTypeNoArgsBuilder get() = int {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.nat(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Nat, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.nat: MichelineMichelsonComparableTypeNoArgsBuilder get() = nat {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.string(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.String, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.string: MichelineMichelsonComparableTypeNoArgsBuilder get() = string {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.chainId(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.ChainId, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.chainId: MichelineMichelsonComparableTypeNoArgsBuilder get() = chainId {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.bytes(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Bytes, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.bytes: MichelineMichelsonComparableTypeNoArgsBuilder get() = bytes {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.mutez(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Mutez, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.mutez: MichelineMichelsonComparableTypeNoArgsBuilder get() = mutez {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.keyHash(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.KeyHash, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.keyHash: MichelineMichelsonComparableTypeNoArgsBuilder get() = keyHash {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.key(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Key, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.key: MichelineMichelsonComparableTypeNoArgsBuilder get() = key {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.signature(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Signature, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.signature: MichelineMichelsonComparableTypeNoArgsBuilder get() = signature {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.timestamp(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Timestamp, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.timestamp: MichelineMichelsonComparableTypeNoArgsBuilder get() = timestamp {}

public fun MichelineMichelsonComparableTypeExpressionBuilder.address(builderAction: MichelineMichelsonComparableTypeNoArgsBuilder.() -> Unit = {}): MichelineMichelsonComparableTypeNoArgsBuilder =
    primitiveApplication(MichelsonComparableType.Address, builderAction)

public val MichelineMichelsonComparableTypeExpressionBuilder.address: MichelineMichelsonComparableTypeNoArgsBuilder get() = address {}
