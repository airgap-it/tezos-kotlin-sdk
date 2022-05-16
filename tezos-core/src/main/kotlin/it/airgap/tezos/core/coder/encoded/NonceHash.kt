package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.NonceHash

// -- NonceHash <-> ByteArray --

public fun NonceHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().encodedBytesCoder)

public fun NonceHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): NonceHash =
    NonceHash.decodeFromBytes(bytes, tezos.dependencyRegistry.core().encodedBytesCoder)

@InternalTezosSdkApi
public fun NonceHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun NonceHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): NonceHash =
    encodedBytesCoder.decode(bytes, NonceHash)

@InternalTezosSdkApi
public fun NonceHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): NonceHash =
    encodedBytesCoder.decodeConsuming(bytes, NonceHash)
