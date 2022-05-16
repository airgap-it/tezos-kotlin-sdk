package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKeyHash

// -- Secp256K1PublicKeyHash <-> ByteArray --

public fun Secp256K1PublicKeyHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().encodedBytesCoder)

public fun Secp256K1PublicKeyHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Secp256K1PublicKeyHash =
    Secp256K1PublicKeyHash.decodeFromBytes(bytes, tezos.dependencyRegistry.core().encodedBytesCoder)

@InternalTezosSdkApi
public fun Secp256K1PublicKeyHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Secp256K1PublicKeyHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Secp256K1PublicKeyHash =
    encodedBytesCoder.decode(bytes, Secp256K1PublicKeyHash)

@InternalTezosSdkApi
public fun Secp256K1PublicKeyHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Secp256K1PublicKeyHash =
    encodedBytesCoder.decodeConsuming(bytes, Secp256K1PublicKeyHash)
