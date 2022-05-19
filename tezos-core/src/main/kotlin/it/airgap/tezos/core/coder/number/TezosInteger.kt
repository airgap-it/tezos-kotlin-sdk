package it.airgap.tezos.core.coder.number

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.number.TezosInteger

// -- TezosInteger <-> ByteArray --

public fun TezosInteger.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.coreModule.dependencyRegistry.tezosIntegerBytesCoder)

public fun TezosInteger.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): TezosInteger =
    TezosInteger.decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.tezosIntegerBytesCoder)

@InternalTezosSdkApi
public fun TezosInteger.encodeToBytes(tezosIntegerBytesCoder: ConsumingBytesCoder<TezosInteger>): ByteArray =
    tezosIntegerBytesCoder.encode(this)

@InternalTezosSdkApi
public fun TezosInteger.Companion.decodeFromBytes(bytes: ByteArray, tezosIntegerBytesCoder: ConsumingBytesCoder<TezosInteger>): TezosInteger =
    tezosIntegerBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun TezosInteger.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, tezosIntegerBytesCoder: ConsumingBytesCoder<TezosInteger>): TezosInteger =
    tezosIntegerBytesCoder.decodeConsuming(bytes)
