package it.airgap.tezos.core.coder.tez

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.tez.Mutez

// -- Mutez <-> ByteArray --

public fun Mutez.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.mutezBytesCoder)
}

public fun Mutez.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Mutez = withTezosContext {
    Mutez.decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.mutezBytesCoder)
}

@InternalTezosSdkApi
public interface MutezCoderContext {
    public fun Mutez.encodeToBytes(mutezBytesCoder: ConsumingBytesCoder<Mutez>): ByteArray =
        mutezBytesCoder.encode(this)

    public fun Mutez.Companion.decodeFromBytes(bytes: ByteArray, mutezBytesCoder: ConsumingBytesCoder<Mutez>): Mutez =
        mutezBytesCoder.decode(bytes)

    public fun Mutez.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, mutezBytesCoder: ConsumingBytesCoder<Mutez>): Mutez =
        mutezBytesCoder.decodeConsuming(bytes)
}
