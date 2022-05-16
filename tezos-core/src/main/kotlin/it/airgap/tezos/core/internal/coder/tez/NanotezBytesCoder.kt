package it.airgap.tezos.core.internal.coder.tez

import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.core.type.zarith.ZarithNatural

internal class NanotezBytesCoder(private val zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural>) :
    ConsumingBytesCoder<Nanotez> {
    override fun encode(value: Nanotez): ByteArray = zarithNaturalBytesCoder.encode(ZarithNatural(value.value))
    override fun decode(value: ByteArray): Nanotez = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): Nanotez = Nanotez(zarithNaturalBytesCoder.decodeConsuming(value).int)
}