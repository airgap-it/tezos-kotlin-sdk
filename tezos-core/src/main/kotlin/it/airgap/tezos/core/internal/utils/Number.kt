package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.zarith.ZarithInteger
import it.airgap.tezos.core.type.zarith.ZarithNatural

@InternalTezosSdkApi
public fun BigInt.toZarithInteger(): ZarithInteger = ZarithInteger(toString(10))

@InternalTezosSdkApi
public fun BigInt.toZarithNatural(): ZarithNatural = ZarithNatural(toString(10))

@InternalTezosSdkApi
internal fun ZarithInteger.toBigInt(): BigInt = BigInt.valueOf(int)

@InternalTezosSdkApi
internal fun ZarithNatural.toBigInt(): BigInt = BigInt.valueOf(int)