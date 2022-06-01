package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.OperationMetadataHash

/**
 * Encodes an [OperationMetadataHash] to bytes.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/OperationMetadataHash/OperationMetadataHashSamples.Coding#toBytes` for a sample usage.
 */
public fun OperationMetadataHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes an [OperationMetadataHash] from [bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/OperationMetadataHash/OperationMetadataHashSamples.Coding#fromBytes` for a sample usage.
 */
public fun OperationMetadataHash.Companion.decodeFromBytes(bytes: ByteArray,tezos: Tezos = Tezos.Default): OperationMetadataHash = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface OperationMetadataHashCoderContext {
    public fun OperationMetadataHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun OperationMetadataHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): OperationMetadataHash =
        encodedBytesCoder.decode(bytes, OperationMetadataHash)

    public fun OperationMetadataHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): OperationMetadataHash =
        encodedBytesCoder.decodeConsuming(bytes, OperationMetadataHash)
}
