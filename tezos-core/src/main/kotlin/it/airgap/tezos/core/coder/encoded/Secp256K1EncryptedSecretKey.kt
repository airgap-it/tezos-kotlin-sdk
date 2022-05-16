package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.type.encoded.Secp256K1EncryptedSecretKey

// -- Secp256K1EncryptedSecretKey <-> ByteArray --

public fun Secp256K1EncryptedSecretKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.core().dependencyRegistry.encodedBytesCoder)

public fun Secp256K1EncryptedSecretKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Secp256K1EncryptedSecretKey =
    decodeFromBytes(bytes, tezos.core().dependencyRegistry.encodedBytesCoder)

@InternalTezosSdkApi
public fun Secp256K1EncryptedSecretKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Secp256K1EncryptedSecretKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Secp256K1EncryptedSecretKey =
    encodedBytesCoder.decode(bytes, Secp256K1EncryptedSecretKey)

@InternalTezosSdkApi
public fun Secp256K1EncryptedSecretKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Secp256K1EncryptedSecretKey =
    encodedBytesCoder.decodeConsuming(bytes, Secp256K1EncryptedSecretKey)
