package it.airgap.tezos.rpc.internal.context

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

private typealias TezosCoreContext = it.airgap.tezos.core.internal.context.TezosContext
private typealias TezosMichelsonContext = it.airgap.tezos.michelson.internal.context.TezosContext
private typealias TezosOperationContext = it.airgap.tezos.operation.internal.context.TezosContext

@InternalTezosSdkApi
public interface TezosContext :
    TezosCoreContext,
    TezosMichelsonContext,
    TezosOperationContext

internal object TezosRpcContext : TezosContext

internal inline fun <T> withTezosContext(block: TezosContext.() -> T): T = block(TezosRpcContext)