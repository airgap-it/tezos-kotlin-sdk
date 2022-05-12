package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.GenericSignature

// -- GenericSignature <-> ByteArray --

public fun GenericSignature.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().encodedBytesCoder)

public fun GenericSignature.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): GenericSignature =
    GenericSignature.decodeFromBytes(bytes, tezos.dependencyRegistry.core().encodedBytesCoder)

@InternalTezosSdkApi
public fun GenericSignature.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun GenericSignature.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): GenericSignature =
    encodedBytesCoder.decode(bytes, GenericSignature)

@InternalTezosSdkApi
public fun GenericSignature.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): GenericSignature =
    encodedBytesCoder.decodeConsuming(bytes, GenericSignature)
