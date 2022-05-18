package it.airgap.tezos.core.coder.zarith

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.zarith.ZarithNatural

// -- ZarithNatural <-> ByteArray --

public fun ZarithNatural.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.coreModule.dependencyRegistry.zarithNaturalBytesCoder)

public fun ZarithNatural.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ZarithNatural =
    ZarithNatural.decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.zarithNaturalBytesCoder)

@InternalTezosSdkApi
public fun ZarithNatural.encodeToBytes(zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural>): ByteArray =
    zarithNaturalBytesCoder.encode(this)

@InternalTezosSdkApi
public fun ZarithNatural.Companion.decodeFromBytes(bytes: ByteArray, zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural>): ZarithNatural =
    zarithNaturalBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun ZarithNatural.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural>): ZarithNatural =
    zarithNaturalBytesCoder.decodeConsuming(bytes)
