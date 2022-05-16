package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.michelson
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*

public fun micheline(tezos: Tezos = Tezos.Default, builderAction: MichelineMichelsonExpressionBuilder.() -> Unit = {}): MichelineNode =
    micheline(tezos.michelson().dependencyRegistry.michelsonToMichelineConverter, builderAction)

public fun michelineType(tezos: Tezos = Tezos.Default, builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit = {}): MichelineNode =
    michelineType(tezos.michelson().dependencyRegistry.michelsonToMichelineConverter, builderAction)

public fun michelineComparableType(tezos: Tezos = Tezos.Default, builderAction: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit = {}): MichelineNode =
    michelineComparableType(tezos.michelson().dependencyRegistry.michelsonToMichelineConverter, builderAction)

public fun michelineData(tezos: Tezos = Tezos.Default, builderAction: MichelineMichelsonDataExpressionBuilder.() -> Unit = {}): MichelineNode =
    michelineData(tezos.michelson().dependencyRegistry.michelsonToMichelineConverter, builderAction)

public fun michelineInstruction(tezos: Tezos = Tezos.Default, builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit = {}): MichelineNode =
    michelineInstruction(tezos.michelson().dependencyRegistry.michelsonToMichelineConverter, builderAction)

@InternalTezosSdkApi
public fun micheline(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    builderAction: MichelineMichelsonExpressionBuilder.() -> Unit = {},
): MichelineNode = MichelineMichelsonExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

@InternalTezosSdkApi
public fun michelineType(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit = {},
): MichelineNode = MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

@InternalTezosSdkApi
public fun michelineComparableType(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    builderAction: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit = {},
): MichelineNode = MichelineMichelsonComparableTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

@InternalTezosSdkApi
public fun michelineData(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    builderAction: MichelineMichelsonDataExpressionBuilder.() -> Unit = {},
): MichelineNode = MichelineMichelsonDataExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

@InternalTezosSdkApi
public fun michelineInstruction(
    michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit = {},
): MichelineNode = MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()
