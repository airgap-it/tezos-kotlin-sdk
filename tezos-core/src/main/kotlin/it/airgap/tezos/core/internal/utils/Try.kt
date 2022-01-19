package it.airgap.tezos.core.internal.utils

public inline fun <T> runCatchingFlat(block: () -> Result<T>): Result<T> =
    try {
        block()
    } catch (e: Exception) {
        Result.failure(e)
    }