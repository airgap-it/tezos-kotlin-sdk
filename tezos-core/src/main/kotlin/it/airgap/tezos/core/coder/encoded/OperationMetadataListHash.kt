package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.OperationMetadataListHash

/**
 * Encodes an [OperationMetadataListHash] to [ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/OperationMetadataListHash/OperationMetadataListHashSamples.Coding#toBytes` for a sample usage.
 */
public fun OperationMetadataListHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes an [OperationMetadataListHash] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/OperationMetadataListHash/OperationMetadataListHashSamples.Coding#fromBytes` for a sample usage.
 */
public fun OperationMetadataListHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): OperationMetadataListHash = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface OperationMetadataListHashCoderContext {
    public fun OperationMetadataListHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun OperationMetadataListHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): OperationMetadataListHash =
        encodedBytesCoder.decode(bytes, OperationMetadataListHash)

    public fun OperationMetadataListHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): OperationMetadataListHash =
        encodedBytesCoder.decodeConsuming(bytes, OperationMetadataListHash)
}
