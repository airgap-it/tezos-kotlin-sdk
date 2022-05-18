package it.airgap.tezos.michelson.converter

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- Micheline -> Michelson --

public fun <T : MichelineNode> T.toMichelson(tezos: Tezos = Tezos.Default): Michelson =
    toMichelson(tezos.michelsonModule.dependencyRegistry.michelineToMichelsonConverter)

@InternalTezosSdkApi
public fun <T : MichelineNode> T.toMichelson(michelineToMichelsonConverter: Converter<MichelineNode, Michelson>): Michelson =
    michelineToMichelsonConverter.convert(this)

// -- Micheline -> String

public fun <T : MichelineNode> T.toExpression(tezos: Tezos = Tezos.Default): String =
    toExpression(tezos.michelsonModule.dependencyRegistry.michelineToStringConverter)

public fun <T : MichelineNode> T.toCompactExpression(tezos: Tezos = Tezos.Default): String =
    toCompactExpression(tezos.michelsonModule.dependencyRegistry.michelineToCompactStringConverter)

@InternalTezosSdkApi
public fun <T : MichelineNode> T.toExpression(michelineToStringConverter: Converter<MichelineNode, String>): String =
    michelineToStringConverter.convert(this)


@InternalTezosSdkApi
public fun <T : MichelineNode> T.toCompactExpression(michelineToCompactStringConverter: Converter<MichelineNode, String>): String =
    michelineToCompactStringConverter.convert(this)
