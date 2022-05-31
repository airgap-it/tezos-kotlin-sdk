package it.airgap.tezos.operation.signer

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface SignerContext : KeySignerContext, OperationSignerContext