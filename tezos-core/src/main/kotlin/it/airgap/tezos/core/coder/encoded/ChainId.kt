package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.ChainId

// -- ChainId <-> ByteArray --

public fun ChainId.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)

public fun ChainId.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ChainId =
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)

@InternalTezosSdkApi
public fun ChainId.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun ChainId.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): ChainId =
    encodedBytesCoder.decode(bytes, ChainId)

@InternalTezosSdkApi
public fun ChainId.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): ChainId =
    encodedBytesCoder.decodeConsuming(bytes, ChainId)
