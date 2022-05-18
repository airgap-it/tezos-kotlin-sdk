package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.ScriptExprHash

// -- ScriptExprHash <-> ByteArray --

public fun ScriptExprHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)

public fun ScriptExprHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ScriptExprHash =
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)

@InternalTezosSdkApi
public fun ScriptExprHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
    encodedBytesCoder.encode(this)

@InternalTezosSdkApi
public fun ScriptExprHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): ScriptExprHash =
    encodedBytesCoder.decode(bytes, ScriptExprHash)

@InternalTezosSdkApi
public fun ScriptExprHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): ScriptExprHash =
    encodedBytesCoder.decodeConsuming(bytes, ScriptExprHash)
