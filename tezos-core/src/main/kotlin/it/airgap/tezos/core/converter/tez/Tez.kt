package it.airgap.tezos.core.converter.tez

import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.core.type.tez.Tez

// -- Tez -> Mutez --

public fun Tez.toMutez(): Mutez = Mutez(bigInt * 1_000_000)

// -- Tez -> Nanotez --

public fun Tez.toNanotez(): Nanotez = Nanotez(bigInt * 1_000_000_000)