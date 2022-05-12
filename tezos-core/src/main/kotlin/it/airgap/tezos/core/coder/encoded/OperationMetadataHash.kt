package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.OperationMetadataHash

// -- OperationMetadataHash <-> ByteArray --

public fun OperationMetadataHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().encodedBytesCoder)

public fun OperationMetadataHash.Companion.decodeFromBytes(bytes: ByteArray,tezos: Tezos = Tezos.Default): OperationMetadataHash =
    OperationMetadataHash.decodeFromBytes(bytes, tezos.dependencyRegistry.core().encodedBytesCoder)

@InternalTezosSdkApi
public fun OperationMetadataHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun OperationMetadataHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): OperationMetadataHash =
    encodedBytesCoder.decode(bytes, OperationMetadataHash)

@InternalTezosSdkApi
public fun OperationMetadataHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): OperationMetadataHash =
    encodedBytesCoder.decodeConsuming(bytes, OperationMetadataHash)
