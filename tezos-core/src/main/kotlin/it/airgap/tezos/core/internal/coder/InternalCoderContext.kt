package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.number.NumberInternalCoderContext
import it.airgap.tezos.core.internal.coder.tez.TezInternalCoderContext

@InternalTezosSdkApi
public interface InternalCoderContext : NumberInternalCoderContext, TezInternalCoderContext