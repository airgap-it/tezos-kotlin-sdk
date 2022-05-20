package it.airgap.tezos.core.converter.tez

import it.airgap.tezos.core.internal.type.Number
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.core.type.tez.Tez

// -- Nanotez -> Tez --

public fun Nanotez.toTez(): Tez = Tez(bigInt.div(1_000_000_000, Number.RoundingMode.Up))

// -- Nanotez -> Mutez --

public fun Nanotez.toMutez(): Mutez = Mutez(bigInt.div(1_000, Number.RoundingMode.Up))