package it.airgap.tezos.rpc.internal.converter.tez

import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.rpc.internal.type.Nanotez

// -- Nanotez -> Mutez --

internal fun Nanotez.toMutez(): Mutez = Mutez(bigInt.div(1_000, BigInt.RoundingMode.Up).toLongExact())