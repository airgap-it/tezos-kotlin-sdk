package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.type.encoded.OperationMetadataListHash

// -- OperationMetadataListHash <-> ByteArray --

public fun OperationMetadataListHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.core().dependencyRegistry.encodedBytesCoder)

public fun OperationMetadataListHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): OperationMetadataListHash =
    decodeFromBytes(bytes, tezos.core().dependencyRegistry.encodedBytesCoder)

@InternalTezosSdkApi
public fun OperationMetadataListHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun OperationMetadataListHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): OperationMetadataListHash =
    encodedBytesCoder.decode(bytes, OperationMetadataListHash)

@InternalTezosSdkApi
public fun OperationMetadataListHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): OperationMetadataListHash =
    encodedBytesCoder.decodeConsuming(bytes, OperationMetadataListHash)
