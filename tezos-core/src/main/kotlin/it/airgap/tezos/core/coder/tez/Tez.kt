package it.airgap.tezos.core.coder.tez

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.TezBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.tez.Tez

// -- Tez <-> ByteArray --

public fun Tez.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().tezBytesCoder)

public fun Tez.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Tez =
    Tez.decodeFromBytes(bytes, tezos.dependencyRegistry.core().tezBytesCoder)

@InternalTezosSdkApi
public fun Tez.encodeToBytes(tezBytesCoder: TezBytesCoder): ByteArray =
    tezBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Tez.Companion.decodeFromBytes(bytes: ByteArray, tezBytesCoder: TezBytesCoder): Tez =
    tezBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun Tez.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, tezBytesCoder: TezBytesCoder): Tez =
    tezBytesCoder.decodeConsuming(bytes)
