package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.Signature

// -- Signature <-> ByteArray --

public fun Signature.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().signatureBytesCoder)

public fun Signature.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Signature =
    Signature.decodeFromBytes(bytes, tezos.dependencyRegistry.core().signatureBytesCoder)

@InternalTezosSdkApi
public fun Signature.encodeToBytes(signatureBytesCoder: SignatureBytesCoder): ByteArray =
    signatureBytesCoder.encode(meta)

@InternalTezosSdkApi
public fun Signature.Companion.decodeFromBytes(bytes: ByteArray, signatureBytesCoder: SignatureBytesCoder): Signature =
    signatureBytesCoder.decode(bytes).encoded

@InternalTezosSdkApi
public fun Signature.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, signatureBytesCoder: SignatureBytesCoder): Signature =
    signatureBytesCoder.decodeConsuming(bytes).encoded
