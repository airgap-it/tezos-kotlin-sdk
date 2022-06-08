package it.airgap.tezos.core.internal.coder.tez

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.type.number.TezosNatural
import it.airgap.tezos.core.type.tez.Mutez

internal class MutezBytesCoder(private val tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>) : ConsumingBytesCoder<Mutez> {
    override fun encode(value: Mutez): ByteArray = tezosNaturalBytesCoder.encode(TezosNatural(value.value.toULong()))
    override fun decode(value: ByteArray): Mutez = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): Mutez = Mutez(tezosNaturalBytesCoder.decodeConsuming(value).nat)
}