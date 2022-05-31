package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Signature

// -- Signature <-> ByteArray --

public fun Signature.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.signatureBytesCoder)
}

public fun Signature.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Signature = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.signatureBytesCoder)
}

@InternalTezosSdkApi
public interface SignatureCoderContext {
    public fun Signature.encodeToBytes(signatureBytesCoder: ConsumingBytesCoder<Signature>): ByteArray =
        signatureBytesCoder.encode(this)

    public fun Signature.Companion.decodeFromBytes(bytes: ByteArray, signatureBytesCoder: ConsumingBytesCoder<Signature>): Signature =
        signatureBytesCoder.decode(bytes)

    public fun Signature.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, signatureBytesCoder: ConsumingBytesCoder<Signature>): Signature =
        signatureBytesCoder.decodeConsuming(bytes)
}
