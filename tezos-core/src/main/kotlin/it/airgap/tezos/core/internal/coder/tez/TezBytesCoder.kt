package it.airgap.tezos.core.internal.coder.tez

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.type.tez.Tez
import it.airgap.tezos.core.type.zarith.ZarithNatural

internal class TezBytesCoder(private val zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural>) :
    ConsumingBytesCoder<Tez> {
    override fun encode(value: Tez): ByteArray = zarithNaturalBytesCoder.encode(ZarithNatural(value.value))
    override fun decode(value: ByteArray): Tez = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): Tez = Tez(zarithNaturalBytesCoder.decodeConsuming(value).int)
}