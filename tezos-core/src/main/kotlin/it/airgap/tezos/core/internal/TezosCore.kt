package it.airgap.tezos.core.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.di.DependencyRegistry

internal class TezosCore(override val dependencyRegistry: DependencyRegistry) : Tezos() {
}