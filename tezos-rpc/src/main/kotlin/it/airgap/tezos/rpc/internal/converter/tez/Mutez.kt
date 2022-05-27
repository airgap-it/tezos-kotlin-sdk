package it.airgap.tezos.rpc.internal.converter.tez

import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.rpc.internal.type.Nanotez
import it.airgap.tezos.rpc.internal.utils.times

// -- Mutez -> Nanotez --

internal fun Mutez.toNanotez(): Nanotez = Nanotez(value) * 1_000