package it.airgap.tezos.core.internal.coder.tez

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.type.tez.Tez
import it.airgap.tezos.core.type.number.TezosNatural

internal class TezBytesCoder(private val tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>) :
    ConsumingBytesCoder<Tez> {
    override fun encode(value: Tez): ByteArray = tezosNaturalBytesCoder.encode(TezosNatural(value.value))
    override fun decode(value: ByteArray): Tez = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): Tez = Tez(tezosNaturalBytesCoder.decodeConsuming(value).int)
}