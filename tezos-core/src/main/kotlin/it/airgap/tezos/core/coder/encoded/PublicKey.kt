package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.PublicKeyBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.PublicKey

// -- PublicKey <-> ByteArray --

public fun PublicKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().publicKeyBytesCoder)

public fun PublicKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): PublicKey =
    PublicKey.decodeFromBytes(bytes, tezos.dependencyRegistry.core().publicKeyBytesCoder)

@InternalTezosSdkApi
public fun PublicKey.encodeToBytes(publicKeyBytesCoder: PublicKeyBytesCoder): ByteArray =
    publicKeyBytesCoder.encode(meta)

@InternalTezosSdkApi
public fun PublicKey.Companion.decodeFromBytes(bytes: ByteArray, publicKeyBytesCoder: PublicKeyBytesCoder): PublicKey =
    publicKeyBytesCoder.decode(bytes).encoded

@InternalTezosSdkApi
public fun PublicKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, publicKeyBytesCoder: PublicKeyBytesCoder): PublicKey =
    publicKeyBytesCoder.decodeConsuming(bytes).encoded
