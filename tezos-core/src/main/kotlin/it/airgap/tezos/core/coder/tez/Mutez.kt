package it.airgap.tezos.core.coder.tez

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.tez.Mutez

// -- Mutez <-> ByteArray --

public fun Mutez.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().mutezBytesCoder)

public fun Mutez.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Mutez =
    Mutez.decodeFromBytes(bytes, tezos.dependencyRegistry.core().mutezBytesCoder)

@InternalTezosSdkApi
public fun Mutez.encodeToBytes(mutezBytesCoder: ConsumingBytesCoder<Mutez>): ByteArray =
    mutezBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Mutez.Companion.decodeFromBytes(bytes: ByteArray, mutezBytesCoder: ConsumingBytesCoder<Mutez>): Mutez =
    mutezBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun Mutez.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, mutezBytesCoder: ConsumingBytesCoder<Mutez>): Mutez =
    mutezBytesCoder.decodeConsuming(bytes)
