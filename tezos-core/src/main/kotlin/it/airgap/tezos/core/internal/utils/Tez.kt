package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.core.type.tez.Tez

@InternalTezosSdkApi
public fun Tez.toBigInt(): BigInt = BigInt.valueOf(value)

@InternalTezosSdkApi
public fun BigInt.toTez(): Tez = Tez(toString())

@InternalTezosSdkApi
public fun Mutez.toBigInt(): BigInt = BigInt.valueOf(value)

@InternalTezosSdkApi
public fun BigInt.toMutez(): Mutez = Mutez(toString())

@InternalTezosSdkApi
public fun Nanotez.toBigInt(): BigInt = BigInt.valueOf(value)

@InternalTezosSdkApi
public fun BigInt.toNanotez(): Nanotez = Nanotez(toString())