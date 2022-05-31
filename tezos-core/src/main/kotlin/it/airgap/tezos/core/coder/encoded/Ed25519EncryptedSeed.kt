package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Ed25519EncryptedSeed

// -- Ed25519EncryptedSeed <-> ByteArray --

public fun Ed25519EncryptedSeed.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

public fun Ed25519EncryptedSeed.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Ed25519EncryptedSeed = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface Ed25519EncryptedSeedCoderContext {
    public fun Ed25519EncryptedSeed.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun Ed25519EncryptedSeed.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Ed25519EncryptedSeed =
        encodedBytesCoder.decode(bytes, Ed25519EncryptedSeed)

    public fun Ed25519EncryptedSeed.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Ed25519EncryptedSeed =
        encodedBytesCoder.decodeConsuming(bytes, Ed25519EncryptedSeed)
}
