package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.core.type.encoded.SaplingSpendingKey

// -- SaplingSpendingKey <-> ByteArray --

public fun SaplingSpendingKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.dependencyRegistry.core().encodedBytesCoder)

public fun SaplingSpendingKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): SaplingSpendingKey =
    SaplingSpendingKey.decodeFromBytes(bytes, tezos.dependencyRegistry.core().encodedBytesCoder)

@InternalTezosSdkApi
public fun SaplingSpendingKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun SaplingSpendingKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): SaplingSpendingKey =
    encodedBytesCoder.decode(bytes, SaplingSpendingKey)

@InternalTezosSdkApi
public fun SaplingSpendingKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): SaplingSpendingKey =
    encodedBytesCoder.decodeConsuming(bytes, SaplingSpendingKey)
