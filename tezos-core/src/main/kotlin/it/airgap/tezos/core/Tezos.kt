package it.airgap.tezos.core

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.delegate.default
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.core.internal.module.TezosModule
import it.airgap.tezos.core.internal.module.withModuleResolver
import it.airgap.tezos.core.internal.utils.failWithDependencyNotFound
import java.util.*
import kotlin.reflect.KClass

public class Tezos internal constructor(
    @InternalTezosSdkApi public val dependencyRegistry: DependencyRegistry,
    @InternalTezosSdkApi public val moduleRegistry: ModuleRegistry,
){
    private object Static {
        var defaultTezos: Tezos by default { Tezos { cryptoProvider = defaultCryptoProvider } }

        val defaultCryptoProvider: CryptoProvider
            get() = cryptoProviders.firstOrNull() ?: failWithDependencyNotFound("CryptoProvider", "tezos-crypto")

        private val cryptoProviders: List<CryptoProvider> = CryptoProvider::class.java.let {
            ServiceLoader.load(it, it.classLoader).toList()
        }
    }

    public class Builder {
        public var default: Boolean = false
        public var cryptoProvider: CryptoProvider by default { Static.defaultCryptoProvider }

        @InternalTezosSdkApi
        public val moduleBuilders: MutableMap<KClass<out TezosModule>, TezosModule.Builder<*>> = mutableMapOf()

        public inline fun <reified T : TezosModule> install(builder: TezosModule.Builder<T>): Builder = apply { install(builder, T::class) }

        @PublishedApi
        internal fun <T : TezosModule> install(builder: TezosModule.Builder<T>, moduleClass: KClass<T>) {
            moduleBuilders[moduleClass] = builder
        }

        public fun build(): Tezos {
            val dependencyRegistry = DependencyRegistry(cryptoProvider)

            val modules = withModuleResolver { moduleBuilders.build(dependencyRegistry) }
            val moduleRegistry = ModuleRegistry(modules)

            return Tezos(dependencyRegistry, moduleRegistry).also {
                if (default) {
                    Static.defaultTezos = it
                }
            }
        }
    }

    public companion object {
        public val Default: Tezos
            get() = Static.defaultTezos
    }
}

public inline fun Tezos(builder: Tezos.Builder.() -> Unit = {}): Tezos = Tezos.Builder().apply(builder).build()
