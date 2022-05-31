package it.airgap.tezos.michelson.internal.context

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.michelson.coder.CoderContext
import it.airgap.tezos.michelson.converter.ConverterContext
import it.airgap.tezos.michelson.micheline.dsl.MichelineDslContext
import it.airgap.tezos.michelson.normalizer.NormalizerContext
import it.airgap.tezos.michelson.packer.PackerContext

private typealias TezosCoreContext = it.airgap.tezos.core.internal.context.TezosContext

@InternalTezosSdkApi
public interface TezosContext :
    TezosCoreContext,
    CoderContext,
    ConverterContext,
    MichelineDslContext,
    NormalizerContext,
    PackerContext

internal object TezosMichelsonContext : TezosContext

internal inline fun <T> withTezosContext(block: TezosContext.() -> T): T = block(TezosMichelsonContext)