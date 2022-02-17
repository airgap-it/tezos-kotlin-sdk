package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public fun failWithIllegalState(message: String? = null): Nothing = throw IllegalStateException(message)

@InternalTezosSdkApi
public fun failWithIllegalArgument(message: String? = null): Nothing = throw IllegalArgumentException(message)