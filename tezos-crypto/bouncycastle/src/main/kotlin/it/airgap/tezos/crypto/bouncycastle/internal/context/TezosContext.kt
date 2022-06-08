package it.airgap.tezos.crypto.bouncycastle.internal.context

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

private typealias TezosCoreContext = it.airgap.tezos.core.internal.context.TezosContext

@InternalTezosSdkApi
public interface TezosContext : TezosCoreContext
internal object TezosCryptoBouncyCastleContext : TezosContext

internal inline fun <T> withTezosContext(block: TezosContext.() -> T): T = block(TezosCryptoBouncyCastleContext)