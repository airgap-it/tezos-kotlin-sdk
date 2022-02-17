package it.airgap.tezos.michelson

import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication

public fun MichelinePrimitiveApplication.isPrim(prim: Michelson.Prim): Boolean = this.prim.value == prim.name