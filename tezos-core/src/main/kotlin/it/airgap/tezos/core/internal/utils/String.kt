package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public fun String.padStartEven(padChar: Char): String {
    val nextEven = if (length % 2 == 0) length else length + 1
    return padStart(nextEven, padChar)
}
