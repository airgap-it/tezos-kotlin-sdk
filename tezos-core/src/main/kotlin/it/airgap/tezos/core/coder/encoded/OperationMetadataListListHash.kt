package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.OperationMetadataListListHash

/**
 * Encodes an [OperationMetadataListListHash] to bytes.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/OperationMetadataListListHash/OperationMetadataListListHashSamples.Coding#toBytes` for a sample usage.
 */
public fun OperationMetadataListListHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes an [OperationMetadataListListHash] from [bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/OperationMetadataListListHash/OperationMetadataListListHashSamples.Coding#fromBytes` for a sample usage.
 */
public fun OperationMetadataListListHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): OperationMetadataListListHash = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface OperationMetadataListListHashCoderContext {
    public fun OperationMetadataListListHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun OperationMetadataListListHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): OperationMetadataListListHash =
        encodedBytesCoder.decode(bytes, OperationMetadataListListHash)

    public fun OperationMetadataListListHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): OperationMetadataListListHash =
        encodedBytesCoder.decodeConsuming(bytes, OperationMetadataListListHash)
}
