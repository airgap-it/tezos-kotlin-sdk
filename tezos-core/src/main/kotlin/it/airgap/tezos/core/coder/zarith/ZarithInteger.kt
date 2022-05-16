package it.airgap.tezos.core.coder.zarith

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.zarith.ZarithInteger

// -- ZarithInteger <-> ByteArray --

public fun ZarithInteger.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().zarithIntegerBytesCoder)

public fun ZarithInteger.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ZarithInteger =
    ZarithInteger.decodeFromBytes(bytes, tezos.dependencyRegistry.core().zarithIntegerBytesCoder)

@InternalTezosSdkApi
public fun ZarithInteger.encodeToBytes(zarithIntegerBytesCoder: ConsumingBytesCoder<ZarithInteger>): ByteArray =
    zarithIntegerBytesCoder.encode(this)

@InternalTezosSdkApi
public fun ZarithInteger.Companion.decodeFromBytes(bytes: ByteArray, zarithIntegerBytesCoder: ConsumingBytesCoder<ZarithInteger>): ZarithInteger =
    zarithIntegerBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun ZarithInteger.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, zarithIntegerBytesCoder: ConsumingBytesCoder<ZarithInteger>): ZarithInteger =
    zarithIntegerBytesCoder.decodeConsuming(bytes)
