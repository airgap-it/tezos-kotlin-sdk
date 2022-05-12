package it.airgap.tezos.core.coder.zarith

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ZarithNaturalBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.zarith.ZarithNatural

// -- ZarithNatural <-> ByteArray --

public fun ZarithNatural.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().zarithNaturalBytesCoder)

public fun ZarithNatural.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ZarithNatural =
    ZarithNatural.decodeFromBytes(bytes, tezos.dependencyRegistry.core().zarithNaturalBytesCoder)

@InternalTezosSdkApi
public fun ZarithNatural.encodeToBytes(zarithNaturalBytesCoder: ZarithNaturalBytesCoder): ByteArray =
    zarithNaturalBytesCoder.encode(this)

@InternalTezosSdkApi
public fun ZarithNatural.Companion.decodeFromBytes(bytes: ByteArray, zarithNaturalBytesCoder: ZarithNaturalBytesCoder): ZarithNatural =
    zarithNaturalBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun ZarithNatural.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, zarithNaturalBytesCoder: ZarithNaturalBytesCoder): ZarithNatural =
    zarithNaturalBytesCoder.decodeConsuming(bytes)
