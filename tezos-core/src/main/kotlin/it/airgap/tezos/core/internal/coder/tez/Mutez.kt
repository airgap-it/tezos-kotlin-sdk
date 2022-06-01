package it.airgap.tezos.core.internal.coder.tez

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.type.tez.Mutez

@InternalTezosSdkApi
public interface MutezInternalCoderContext {
    public fun Mutez.encodeToBytes(mutezBytesCoder: ConsumingBytesCoder<Mutez>): ByteArray =
        mutezBytesCoder.encode(this)

    public fun Mutez.Companion.decodeFromBytes(bytes: ByteArray, mutezBytesCoder: ConsumingBytesCoder<Mutez>): Mutez =
        mutezBytesCoder.decode(bytes)

    public fun Mutez.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, mutezBytesCoder: ConsumingBytesCoder<Mutez>): Mutez =
        mutezBytesCoder.decodeConsuming(bytes)
}
