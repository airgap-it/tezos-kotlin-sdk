package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.Signature

/**
 * Encodes a [Signature] to [bytes][ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Signature/SignatureSamples.Coding#toBytes` for a sample usage.
 */
public fun Signature.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.signatureBytesCoder)
}

/**
 * Decodes a [Signature] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Signature/SignatureSamples.Coding#fromBytes` for a sample usage.
 */
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
