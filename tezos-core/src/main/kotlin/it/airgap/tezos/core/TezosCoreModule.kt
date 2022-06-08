package it.airgap.tezos.core

import it.airgap.tezos.core.internal.TezosCoreModule

public val CoreModule: TezosCoreModule.Builder
    get() = TezosCoreModule.Builder()