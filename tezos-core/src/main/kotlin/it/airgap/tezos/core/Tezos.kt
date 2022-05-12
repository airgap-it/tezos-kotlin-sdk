package it.airgap.tezos.core

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.TezosCore
import it.airgap.tezos.core.internal.TezosCoreStatic
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.di.DependencyRegistry

public abstract class Tezos {
    internal abstract val dependencyRegistry: DependencyRegistry

    public companion object {
        public val Default: Tezos
            get() = TezosCoreStatic.defaultTezos
    }

    public class Builder(private val cryptoProvider: CryptoProvider = TezosCoreStatic.defaultCryptoProvider) {
        public var default: Boolean = false

        public fun build(): Tezos {
            val dependencyRegistry = CoreDependencyRegistry(cryptoProvider)

            return TezosCore(dependencyRegistry).also {
                if (default) {
                    TezosCoreStatic.defaultTezos = it
                }
            }
        }
    }
}

public fun Tezos(
    cryptoProvider: CryptoProvider = TezosCoreStatic.defaultCryptoProvider,
    builder: Tezos.Builder.() -> Unit = {}
): Tezos = Tezos.Builder(cryptoProvider).apply(builder).build()
