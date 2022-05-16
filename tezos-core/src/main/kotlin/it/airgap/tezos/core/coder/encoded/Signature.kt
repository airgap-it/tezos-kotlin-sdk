package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.Signature

// -- Signature <-> ByteArray --

public fun Signature.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().signatureBytesCoder)

public fun Signature.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Signature =
    Signature.decodeFromBytes(bytes, tezos.dependencyRegistry.core().signatureBytesCoder)

@InternalTezosSdkApi
public fun Signature.encodeToBytes(signatureBytesCoder: ConsumingBytesCoder<Signature>): ByteArray =
    signatureBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Signature.Companion.decodeFromBytes(bytes: ByteArray, signatureBytesCoder: ConsumingBytesCoder<Signature>): Signature =
    signatureBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun Signature.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, signatureBytesCoder: ConsumingBytesCoder<Signature>): Signature =
    signatureBytesCoder.decodeConsuming(bytes)
