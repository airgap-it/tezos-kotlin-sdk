package it.airgap.tezos.core

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.utils.failWithIllegalState

public class TezosSdk internal constructor(public val dependencyRegistry: DependencyRegistry) {

    public companion object {
        private var _instance: TezosSdk? = null

        @get:Throws(IllegalStateException::class)
        public val instance: TezosSdk
            get() = _instance ?: failWithIllegalState("TezosSdk was not initialized.")

        public fun init(
            cryptoProvider: CryptoProvider,
        ) {
            if (_instance != null) return

            val dependencyRegistry = CoreDependencyRegistry(cryptoProvider)
            _instance = TezosSdk(dependencyRegistry)
        }
    }
}