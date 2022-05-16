package it.airgap.tezos.core.coder.tez

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.type.tez.Tez

// -- Tez <-> ByteArray --

public fun Tez.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.core().dependencyRegistry.tezBytesCoder)

public fun Tez.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Tez =
    Tez.decodeFromBytes(bytes, tezos.core().dependencyRegistry.tezBytesCoder)

@InternalTezosSdkApi
public fun Tez.encodeToBytes(tezBytesCoder: ConsumingBytesCoder<Tez>): ByteArray =
    tezBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Tez.Companion.decodeFromBytes(bytes: ByteArray, tezBytesCoder: ConsumingBytesCoder<Tez>): Tez =
    tezBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun Tez.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, tezBytesCoder: ConsumingBytesCoder<Tez>): Tez =
    tezBytesCoder.decodeConsuming(bytes)
