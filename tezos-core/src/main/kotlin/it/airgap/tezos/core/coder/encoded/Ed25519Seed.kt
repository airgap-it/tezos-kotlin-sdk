package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.Ed25519Seed

// -- Ed25519Seed <-> ByteArray --

public fun Ed25519Seed.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().encodedBytesCoder)

public fun Ed25519Seed.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Ed25519Seed =
    Ed25519Seed.decodeFromBytes(bytes, tezos.dependencyRegistry.core().encodedBytesCoder)

@InternalTezosSdkApi
public fun Ed25519Seed.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Ed25519Seed.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Ed25519Seed =
    encodedBytesCoder.decode(bytes, Ed25519Seed)

@InternalTezosSdkApi
public fun Ed25519Seed.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Ed25519Seed =
    encodedBytesCoder.decodeConsuming(bytes, Ed25519Seed)
