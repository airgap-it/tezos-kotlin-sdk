package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.ProtocolHash

// -- ProtocolHash <-> ByteArray --

public fun ProtocolHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)

public fun ProtocolHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ProtocolHash =
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)

@InternalTezosSdkApi
public fun ProtocolHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun ProtocolHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): ProtocolHash =
    encodedBytesCoder.decode(bytes, ProtocolHash)

@InternalTezosSdkApi
public fun ProtocolHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): ProtocolHash =
    encodedBytesCoder.decodeConsuming(bytes, ProtocolHash)
