package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.GenericSignature

/**
 * Encodes a [GenericSignature] to bytes.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/GenericSignature/GenericSignatureSamples.Coding#toBytes` for a sample usage.
 */
public fun GenericSignature.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes a [GenericSignature] from [bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/GenericSignature/GenericSignatureSamples.Coding#fromBytes` for a sample usage.
 */
public fun GenericSignature.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): GenericSignature = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface GenericSignatureCoderContext {
    public fun GenericSignature.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun GenericSignature.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): GenericSignature =
        encodedBytesCoder.decode(bytes, GenericSignature)

    public fun GenericSignature.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): GenericSignature =
        encodedBytesCoder.decodeConsuming(bytes, GenericSignature)
}
