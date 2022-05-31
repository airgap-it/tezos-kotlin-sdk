package it.airgap.tezos.core.coder.number

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.number.TezosNatural

// -- TezosNatural <-> ByteArray --

public fun TezosNatural.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.tezosNaturalBytesCoder)
}

public fun TezosNatural.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): TezosNatural = withTezosContext {
    TezosNatural.decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.tezosNaturalBytesCoder)
}

@InternalTezosSdkApi
public interface TezosNaturalCoderContext {
    public fun TezosNatural.encodeToBytes(tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>): ByteArray =
        tezosNaturalBytesCoder.encode(this)

    public fun TezosNatural.Companion.decodeFromBytes(bytes: ByteArray, tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>): TezosNatural =
        tezosNaturalBytesCoder.decode(bytes)

    public fun TezosNatural.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>): TezosNatural =
        tezosNaturalBytesCoder.decodeConsuming(bytes)
}