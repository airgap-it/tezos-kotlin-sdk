package it.airgap.tezos.core.internal.coder

import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.zarith.ZarithNatural

internal class MutezBytesCoder(private val zarithNaturalBytesCoder: ConsumingBytesCoder<ZarithNatural>) : ConsumingBytesCoder<Mutez> {
    override fun encode(value: Mutez): ByteArray = zarithNaturalBytesCoder.encode(ZarithNatural(value.value))
    override fun decode(value: ByteArray): Mutez = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): Mutez = Mutez(zarithNaturalBytesCoder.decodeConsuming(value).int)
}