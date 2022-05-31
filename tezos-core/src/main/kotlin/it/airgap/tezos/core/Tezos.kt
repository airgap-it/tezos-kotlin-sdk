package it.airgap.tezos.core

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.context.TezosCoreContext.default
import it.airgap.tezos.core.internal.context.TezosCoreContext.failWithDependencyNotFound
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.core.internal.module.TezosModule
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
        public var isDefault: Boolean = false
        public var cryptoProvider: CryptoProvider by default { Static.defaultCryptoProvider }

        private val moduleBuilders: MutableMap<KClass<out TezosModule>, TezosModule.Builder<*>> = mutableMapOf()

        public inline fun <reified T : TezosModule, B : TezosModule.Builder<T>> install(builder: B, noinline builderConfiguration: B.() -> Unit = {}): Builder =
            apply { install(T::class, builder, builderConfiguration) }

        @PublishedApi
        internal fun <T : TezosModule, B : TezosModule.Builder<T>> install(moduleClass: KClass<T>, builder: B, builderConfiguration: B.() -> Unit) {
            moduleBuilders[moduleClass] = builder.apply(builderConfiguration)
        }

        public fun build(): Tezos {
            val dependencyRegistry = DependencyRegistry(cryptoProvider)
            val moduleRegistry = ModuleRegistry(builders = moduleBuilders)

            return Tezos(dependencyRegistry, moduleRegistry).also {
                if (isDefault) {
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
