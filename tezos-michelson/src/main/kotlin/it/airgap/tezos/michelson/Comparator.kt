package it.airgap.tezos.michelson

import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication

public fun MichelineNode.isPrim(prim: Michelson.Prim): Boolean = this is MichelinePrimitiveApplication && isPrim(prim)

public fun MichelinePrimitiveApplication.isPrim(prim: Michelson.Prim): Boolean = this.prim.value == prim.name