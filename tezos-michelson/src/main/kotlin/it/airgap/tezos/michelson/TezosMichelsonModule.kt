package it.airgap.tezos.michelson

import it.airgap.tezos.michelson.internal.TezosMichelsonModule

public val MichelsonModule: TezosMichelsonModule.Builder
    get() = TezosMichelsonModule.Builder()