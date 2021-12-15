package it.airgap.tezos.core.internal.utils

public fun failWithIllegalState(message: String? = null): Nothing = throw IllegalStateException(message)
public fun failWithIllegalArgument(message: String? = null): Nothing = throw IllegalArgumentException(message)