package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash

// -- CryptoboxPublicKeyHash <-> ByteArray --

public fun CryptoboxPublicKeyHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.core().dependencyRegistry.encodedBytesCoder)

public fun CryptoboxPublicKeyHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): CryptoboxPublicKeyHash =
    decodeFromBytes(bytes, tezos.core().dependencyRegistry.encodedBytesCoder)

@InternalTezosSdkApi
public fun CryptoboxPublicKeyHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun CryptoboxPublicKeyHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): CryptoboxPublicKeyHash =
    encodedBytesCoder.decode(bytes, CryptoboxPublicKeyHash)

@InternalTezosSdkApi
public fun CryptoboxPublicKeyHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): CryptoboxPublicKeyHash =
    encodedBytesCoder.decodeConsuming(bytes, CryptoboxPublicKeyHash)
