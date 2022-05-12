package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.BlockMetadataHash

// -- BlockMetadataHash <-> ByteArray --

public fun BlockMetadataHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().encodedBytesCoder)

public fun BlockMetadataHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): BlockMetadataHash =
    BlockMetadataHash.decodeFromBytes(bytes, tezos.dependencyRegistry.core().encodedBytesCoder)

@InternalTezosSdkApi
public fun BlockMetadataHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray = encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun BlockMetadataHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): BlockMetadataHash =
    encodedBytesCoder.decode(bytes, BlockMetadataHash)

@InternalTezosSdkApi
public fun BlockMetadataHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): BlockMetadataHash =
    encodedBytesCoder.decodeConsuming(bytes, BlockMetadataHash)
