package it.airgap.tezos.michelson.comparator

import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

// -- MichelineNode --

@OptIn(ExperimentalContracts::class)
public fun MichelineNode.isPrim(prim: Michelson.Prim): Boolean {
    contract {
        returns(true) implies (this@isPrim is MichelinePrimitiveApplication)
    }

    return this is MichelinePrimitiveApplication && isPrim(prim)
}

// -- MichelinePrimitiveApplication --

public fun MichelinePrimitiveApplication.isPrim(prim: Michelson.Prim): Boolean = this.prim.value == prim.name