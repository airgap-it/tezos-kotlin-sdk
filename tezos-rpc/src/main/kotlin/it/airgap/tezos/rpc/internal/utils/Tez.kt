package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.rpc.internal.type.Nanotez

// -- Mutez --

internal operator fun Mutez.plus(other: Mutez): Mutez = Mutez(value + other.value)

// -- Nanotez --

internal operator fun Nanotez.times(other: Int): Nanotez = Nanotez(bigInt * other)
internal operator fun Nanotez.times(other: BigInt): Nanotez = Nanotez(bigInt * other)
internal operator fun Nanotez.times(other: Nanotez): Nanotez = Nanotez(bigInt * other.bigInt)
