package it.airgap.tezos.core.internal.coder.tez

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.core.type.number.TezosNatural

internal class NanotezBytesCoder(private val tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>) :
    ConsumingBytesCoder<Nanotez> {
    override fun encode(value: Nanotez): ByteArray = tezosNaturalBytesCoder.encode(TezosNatural(value.value))
    override fun decode(value: ByteArray): Nanotez = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): Nanotez = Nanotez(tezosNaturalBytesCoder.decodeConsuming(value).int)
}