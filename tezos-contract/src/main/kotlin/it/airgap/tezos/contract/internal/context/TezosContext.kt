package it.airgap.tezos.contract.internal.context

import it.airgap.tezos.contract.entrypoint.dsl.ContractEntrypointParameterDslContext
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

private typealias TezosCoreContext = it.airgap.tezos.core.internal.context.TezosContext
private typealias TezosMichelsonContext = it.airgap.tezos.michelson.internal.context.TezosContext
private typealias TezosOperationContext = it.airgap.tezos.operation.internal.context.TezosContext
private typealias TezosRpcContext = it.airgap.tezos.rpc.internal.context.TezosContext

@InternalTezosSdkApi
public interface TezosContext :
    TezosCoreContext,
    TezosMichelsonContext,
    TezosOperationContext,
    TezosRpcContext,
    ContractEntrypointParameterDslContext

@PublishedApi
internal object TezosContractContext : TezosContext

@PublishedApi
internal inline fun <T> withTezosContext(block: TezosContext.() -> T): T = block(TezosContractContext)