package it.airgap.tezos.michelson

import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication

public fun MichelinePrimitiveApplication.isPrim(grammarType: Michelson.GrammarType): Boolean = prim.value == grammarType.name