package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.number.TezosInteger
import it.airgap.tezos.core.type.number.TezosNatural

@InternalTezosSdkApi
public fun BigInt.toTezosInteger(): TezosInteger = TezosInteger(toString(10))

@InternalTezosSdkApi
public fun BigInt.toTezosNatural(): TezosNatural = TezosNatural(toString(10))

@InternalTezosSdkApi
public fun String.asTezosInteger(): TezosInteger = asTezosIntegerOrNull() ?: failWithInvalidTezosInteger(this)

@InternalTezosSdkApi
public fun String.asTezosIntegerOrNull(): TezosInteger? = if (TezosInteger.isValid(this)) TezosInteger(this) else null

@InternalTezosSdkApi
public fun String.asTezosNatural(): TezosNatural = asTezosNaturalOrNull() ?: failWithInvalidTezosNatural(this)

@InternalTezosSdkApi
public fun String.asTezosNaturalOrNull(): TezosNatural? = if (TezosNatural.isValid(this)) TezosNatural(this) else null

@InternalTezosSdkApi
public fun TezosInteger.toBigInt(): BigInt = BigInt.valueOf(int)

@InternalTezosSdkApi
public fun TezosNatural.toBigInt(): BigInt = BigInt.valueOf(nat)

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

private fun failWithInvalidTezosInteger(string: String): Nothing =
    failWithIllegalArgument("$string is not a valid Tezos integer")

private fun failWithInvalidTezosNatural(string: String): Nothing =
    failWithIllegalArgument("$string is not a valid Tezos natural number")