package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKey

// -- Secp256K1PublicKey <-> ByteArray --

public fun Secp256K1PublicKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)

public fun Secp256K1PublicKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Secp256K1PublicKey =
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)

@InternalTezosSdkApi
public fun Secp256K1PublicKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Secp256K1PublicKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Secp256K1PublicKey =
    encodedBytesCoder.decode(bytes, Secp256K1PublicKey)

@InternalTezosSdkApi
public fun Secp256K1PublicKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Secp256K1PublicKey =
    encodedBytesCoder.decodeConsuming(bytes, Secp256K1PublicKey)
