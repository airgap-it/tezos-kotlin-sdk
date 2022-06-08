package it.airgap.tezos.core.internal.delegate

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface DelegateContext : DefaultDelegateContext, LazyWeakDelegateContext