package it.airgap.tezos.operation.internal.context

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.operation.coder.CoderContext
import it.airgap.tezos.operation.converter.ConverterContext
import it.airgap.tezos.operation.signer.SignerContext

private typealias TezosCoreContext = it.airgap.tezos.core.internal.context.TezosContext
private typealias TezosMichelsonContext = it.airgap.tezos.michelson.internal.context.TezosContext

@InternalTezosSdkApi
public interface TezosContext :
    TezosCoreContext,
    TezosMichelsonContext,
    CoderContext,
    ConverterContext,
    SignerContext

internal object TezosOperationContext : TezosContext

internal inline fun <T> withTezosContext(block: TezosContext.() -> T): T = block(TezosOperationContext)