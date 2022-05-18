package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.PublicKey

// -- PublicKey <-> ByteArray --

public fun PublicKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.coreModule.dependencyRegistry.publicKeyBytesCoder)

public fun PublicKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): PublicKey =
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.publicKeyBytesCoder)

@InternalTezosSdkApi
public fun PublicKey.encodeToBytes(publicKeyBytesCoder: ConsumingBytesCoder<PublicKey>): ByteArray =
    publicKeyBytesCoder.encode(this)

@InternalTezosSdkApi
public fun PublicKey.Companion.decodeFromBytes(bytes: ByteArray, publicKeyBytesCoder: ConsumingBytesCoder<PublicKey>): PublicKey =
    publicKeyBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun PublicKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, publicKeyBytesCoder: ConsumingBytesCoder<PublicKey>): PublicKey =
    publicKeyBytesCoder.decodeConsuming(bytes)
