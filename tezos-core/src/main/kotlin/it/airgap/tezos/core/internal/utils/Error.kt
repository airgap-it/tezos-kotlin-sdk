package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public fun failWithIllegalArgument(message: String? = null): Nothing = throw IllegalArgumentException(message)