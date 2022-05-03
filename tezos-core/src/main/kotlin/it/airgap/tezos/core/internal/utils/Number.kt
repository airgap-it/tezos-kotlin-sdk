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
public fun String.asZarithInteger(): ZarithInteger = asZarithIntegerOrNull() ?: failWithInvalidZarithInteger(this)

@InternalTezosSdkApi
public fun String.asZarithIntegerOrNull(): ZarithInteger? = if (ZarithInteger.isValid(this)) ZarithInteger(this) else null

@InternalTezosSdkApi
public fun String.asZarithNatural(): ZarithNatural = asZarithNaturalOrNull() ?: failWithInvalidZarithNatural(this)

@InternalTezosSdkApi
public fun String.asZarithNaturalOrNull(): ZarithNatural? = if (ZarithNatural.isValid(this)) ZarithNatural(this) else null

@InternalTezosSdkApi
public fun ZarithInteger.toBigInt(): BigInt = BigInt.valueOf(int)

@InternalTezosSdkApi
public fun ZarithNatural.toBigInt(): BigInt = BigInt.valueOf(int)

@InternalTezosSdkApi
public fun Byte.toBigInt(): BigInt = BigInt.valueOf(this)

@InternalTezosSdkApi
public fun UByte.toBigInt(): BigInt = BigInt.valueOf(this)

@InternalTezosSdkApi
public fun Short.toBigInt(): BigInt = BigInt.valueOf(this)

@InternalTezosSdkApi
public fun UShort.toBigInt(): BigInt = BigInt.valueOf(this)

@InternalTezosSdkApi
public fun Int.toBigInt(): BigInt = BigInt.valueOf(this)

@InternalTezosSdkApi
public fun UInt.toBigInt(): BigInt = BigInt.valueOf(this)

@InternalTezosSdkApi
public fun Long.toBigInt(): BigInt = BigInt.valueOf(this)

@InternalTezosSdkApi
public fun ULong.toBigInt(): BigInt = BigInt.valueOf(this)

private fun failWithInvalidZarithInteger(string: String): Nothing =
    failWithIllegalArgument("$string is not a valid Zarith integer")

private fun failWithInvalidZarithNatural(string: String): Nothing =
    failWithIllegalArgument("$string is not a valid Zarith natural number")