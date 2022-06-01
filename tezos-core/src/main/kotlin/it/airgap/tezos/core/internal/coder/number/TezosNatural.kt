package it.airgap.tezos.core.internal.coder.number

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.type.number.TezosNatural

@InternalTezosSdkApi
public interface TezosNaturalInternalCoderContext {
    public fun TezosNatural.encodeToBytes(tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>): ByteArray =
        tezosNaturalBytesCoder.encode(this)

    public fun TezosNatural.Companion.decodeFromBytes(bytes: ByteArray, tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>): TezosNatural =
        tezosNaturalBytesCoder.decode(bytes)

    public fun TezosNatural.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>): TezosNatural =
        tezosNaturalBytesCoder.decodeConsuming(bytes)
}