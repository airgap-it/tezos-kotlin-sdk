package it.airgap.tezos.core.internal.coder.number

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.type.number.TezosInteger

@InternalTezosSdkApi
public interface TezosIntegerInternalCoderContext {
    public fun TezosInteger.encodeToBytes(tezosIntegerBytesCoder: ConsumingBytesCoder<TezosInteger>): ByteArray =
        tezosIntegerBytesCoder.encode(this)

    public fun TezosInteger.Companion.decodeFromBytes(bytes: ByteArray, tezosIntegerBytesCoder: ConsumingBytesCoder<TezosInteger>): TezosInteger =
        tezosIntegerBytesCoder.decode(bytes)

    public fun TezosInteger.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, tezosIntegerBytesCoder: ConsumingBytesCoder<TezosInteger>): TezosInteger =
        tezosIntegerBytesCoder.decodeConsuming(bytes)
}
