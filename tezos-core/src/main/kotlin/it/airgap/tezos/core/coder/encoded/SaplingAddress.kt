package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.SaplingAddress

// -- SaplingAddress <-> ByteArray --

public fun SaplingAddress.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)

public fun SaplingAddress.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): SaplingAddress =
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)

@InternalTezosSdkApi
public fun SaplingAddress.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun SaplingAddress.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): SaplingAddress =
    encodedBytesCoder.decode(bytes, SaplingAddress)

@InternalTezosSdkApi
public fun SaplingAddress.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): SaplingAddress =
    encodedBytesCoder.decodeConsuming(bytes, SaplingAddress)
