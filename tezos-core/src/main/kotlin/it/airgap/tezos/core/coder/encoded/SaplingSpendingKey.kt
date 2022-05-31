package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.SaplingSpendingKey

// -- SaplingSpendingKey <-> ByteArray --

public fun SaplingSpendingKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

public fun SaplingSpendingKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): SaplingSpendingKey = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface SaplingSpendingKeyCoderContext {
    public fun SaplingSpendingKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun SaplingSpendingKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): SaplingSpendingKey =
        encodedBytesCoder.decode(bytes, SaplingSpendingKey)

    public fun SaplingSpendingKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): SaplingSpendingKey =
        encodedBytesCoder.decodeConsuming(bytes, SaplingSpendingKey)
}
