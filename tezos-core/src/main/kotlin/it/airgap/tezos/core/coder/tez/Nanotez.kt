package it.airgap.tezos.core.coder.tez

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.tez.Nanotez

// -- Nanotez <-> ByteArray --

public fun Nanotez.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().nanotezBytesCoder)

public fun Nanotez.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Nanotez =
    Nanotez.decodeFromBytes(bytes, tezos.dependencyRegistry.core().nanotezBytesCoder)

@InternalTezosSdkApi
public fun Nanotez.encodeToBytes(nanotezBytesCoder: ConsumingBytesCoder<Nanotez>): ByteArray =
    nanotezBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Nanotez.Companion.decodeFromBytes(bytes: ByteArray, nanotezBytesCoder: ConsumingBytesCoder<Nanotez>): Nanotez =
    nanotezBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun Nanotez.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, nanotezBytesCoder: ConsumingBytesCoder<Nanotez>): Nanotez =
    nanotezBytesCoder.decodeConsuming(bytes)