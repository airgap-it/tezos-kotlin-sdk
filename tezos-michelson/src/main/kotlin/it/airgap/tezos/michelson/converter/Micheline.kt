package it.airgap.tezos.michelson.converter

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- Micheline -> Michelson --

public fun <T : MichelineNode> T.toMichelson(tezos: Tezos = Tezos.Default): Michelson = withTezosContext {
    toMichelson(tezos.michelsonModule.dependencyRegistry.michelineToMichelsonConverter)
}

// -- Micheline -> String

public fun <T : MichelineNode> T.toExpression(tezos: Tezos = Tezos.Default): String = withTezosContext {
    toExpression(tezos.michelsonModule.dependencyRegistry.michelineToStringConverter)
}

public fun <T : MichelineNode> T.toCompactExpression(tezos: Tezos = Tezos.Default): String = withTezosContext {
    toCompactExpression(tezos.michelsonModule.dependencyRegistry.michelineToCompactStringConverter)
}

@InternalTezosSdkApi
public interface MichelineConverterContext {
    public fun <T : MichelineNode> T.toMichelson(michelineToMichelsonConverter: Converter<MichelineNode, Michelson>): Michelson =
        michelineToMichelsonConverter.convert(this)

    public fun <T : MichelineNode> T.toExpression(michelineToStringConverter: Converter<MichelineNode, String>): String =
        michelineToStringConverter.convert(this)

    public fun <T : MichelineNode> T.toCompactExpression(michelineToCompactStringConverter: Converter<MichelineNode, String>): String =
        michelineToCompactStringConverter.convert(this)
}