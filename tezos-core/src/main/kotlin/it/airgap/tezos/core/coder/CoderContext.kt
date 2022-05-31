package it.airgap.tezos.core.coder

import it.airgap.tezos.core.coder.encoded.EncodedCoderContext
import it.airgap.tezos.core.coder.number.NumberCoderContext
import it.airgap.tezos.core.coder.tez.TezCoderContext
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface CoderContext : EncodedCoderContext, NumberCoderContext, TezCoderContext