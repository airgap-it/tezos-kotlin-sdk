package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.Ed25519PublicKey

// -- Ed25519PublicKey <-> ByteArray --

public fun Ed25519PublicKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().encodedBytesCoder)

public fun Ed25519PublicKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Ed25519PublicKey =
    Ed25519PublicKey.decodeFromBytes(bytes, tezos.dependencyRegistry.core().encodedBytesCoder)

@InternalTezosSdkApi
public fun Ed25519PublicKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Ed25519PublicKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Ed25519PublicKey =
    encodedBytesCoder.decode(bytes, Ed25519PublicKey)

@InternalTezosSdkApi
public fun Ed25519PublicKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Ed25519PublicKey =
    encodedBytesCoder.decodeConsuming(bytes, Ed25519PublicKey)
