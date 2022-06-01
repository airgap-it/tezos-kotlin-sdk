package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Ed25519Signature

/**
 * Encodes an [Ed25519Signature] to [ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519Signature/Ed25519SignatureSamples.Coding#toBytes` for a sample usage.
 */
public fun Ed25519Signature.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes an [Ed25519Signature] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519Signature/Ed25519SignatureSamples.Coding#fromBytes` for a sample usage.
 */
public fun Ed25519Signature.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Ed25519Signature = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface Ed25519SignatureCoderContext {
    public fun Ed25519Signature.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun Ed25519Signature.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Ed25519Signature =
        encodedBytesCoder.decode(bytes, Ed25519Signature)

    public fun Ed25519Signature.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Ed25519Signature =
        encodedBytesCoder.decodeConsuming(bytes, Ed25519Signature)
}
