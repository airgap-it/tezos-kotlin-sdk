package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.P256PublicKeyHash

// -- P256PublicKeyHash <-> ByteArray --

public fun P256PublicKeyHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)

public fun P256PublicKeyHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): P256PublicKeyHash =
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)

@InternalTezosSdkApi
public fun P256PublicKeyHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun P256PublicKeyHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): P256PublicKeyHash =
    encodedBytesCoder.decode(bytes, P256PublicKeyHash)

@InternalTezosSdkApi
public fun P256PublicKeyHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): P256PublicKeyHash =
    encodedBytesCoder.decodeConsuming(bytes, P256PublicKeyHash)
