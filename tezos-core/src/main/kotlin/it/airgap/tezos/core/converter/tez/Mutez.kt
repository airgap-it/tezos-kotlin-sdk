package it.airgap.tezos.core.converter.tez

import it.airgap.tezos.core.internal.type.Number
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.core.type.tez.Tez

// -- Mutez -> Tez --

public fun Mutez.toTez(): Tez = Tez(bigInt.div(1_000_000, Number.RoundingMode.Up))

// -- Mutez -> Nanotez --

public fun Mutez.toNanotez(): Nanotez = Nanotez(bigInt * 1_000)