package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.BlockPayloadHash

// -- BlockPayloadHash <-> ByteArray --

public fun BlockPayloadHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().encodedBytesCoder)

public fun BlockPayloadHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): BlockPayloadHash =
    BlockPayloadHash.decodeFromBytes(bytes, tezos.dependencyRegistry.core().encodedBytesCoder)

@InternalTezosSdkApi
public fun BlockPayloadHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun BlockPayloadHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): BlockPayloadHash =
    encodedBytesCoder.decode(bytes, BlockPayloadHash)

@InternalTezosSdkApi
public fun BlockPayloadHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): BlockPayloadHash =
    encodedBytesCoder.decodeConsuming(bytes, BlockPayloadHash)
