package it.airgap.tezos.core.internal.context

import it.airgap.tezos.core.coder.CoderContext
import it.airgap.tezos.core.converter.ConverterContext
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.InternalCoderContext
import it.airgap.tezos.core.internal.delegate.DelegateContext
import it.airgap.tezos.core.internal.utils.UtilsContext

@InternalTezosSdkApi
public interface TezosContext :
    CoderContext,
    ConverterContext,
    InternalCoderContext,
    DelegateContext,
    UtilsContext

internal object TezosCoreContext : TezosContext // TODO: set private and use context receivers (https://github.com/Kotlin/KEEP/blob/master/proposals/context-receivers.md) once they are stable

internal inline fun <T> withTezosContext(block: TezosContext.() -> T): T = block(TezosCoreContext)