package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.type.encoded.BlockMetadataHash

// -- BlockMetadataHash <-> ByteArray --

public fun BlockMetadataHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.core().dependencyRegistry.encodedBytesCoder)

public fun BlockMetadataHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): BlockMetadataHash =
    decodeFromBytes(bytes, tezos.core().dependencyRegistry.encodedBytesCoder)

@InternalTezosSdkApi
public fun BlockMetadataHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray = encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun BlockMetadataHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): BlockMetadataHash =
    encodedBytesCoder.decode(bytes, BlockMetadataHash)

@InternalTezosSdkApi
public fun BlockMetadataHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): BlockMetadataHash =
    encodedBytesCoder.decodeConsuming(bytes, BlockMetadataHash)
