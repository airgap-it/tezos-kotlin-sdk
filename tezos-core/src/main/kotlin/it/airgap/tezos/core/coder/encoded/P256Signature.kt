package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.P256Signature

/**
 * Encodes a [P256Signature] to [ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/P256Signature/P256SignatureSamples.Coding#toBytes` for a sample usage.
 */
public fun P256Signature.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes a [P256Signature] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/P256Signature/P256SignatureSamples.Coding#fromBytes` for a sample usage.
 */
public fun P256Signature.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): P256Signature = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface P256SignatureCoderContext {
    public fun P256Signature.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun P256Signature.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): P256Signature =
        encodedBytesCoder.decode(bytes, P256Signature)

    public fun P256Signature.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): P256Signature =
        encodedBytesCoder.decodeConsuming(bytes, P256Signature)
}
