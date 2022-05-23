package it.airgap.tezos.core.converter.tez

import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez

// -- Mutez -> Nanotez --

public fun Mutez.toNanotez(): Nanotez = Nanotez(bigInt * 1_000)