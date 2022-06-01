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

/**
 * The main entry point to work with the library and configure it. Used to provide additional context to library features.
 * If only one [Tezos] instance is required, it can be set as the default instance during the build process.
 * The default implementation is implicitly used throughout the library.
 *
 * Example:
 * ```kotlin
 * Tezos { isDefault = true }
 *
 * val address = Address("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
 * val bytes = address.encodeToBytes(/* tezos = Tezos.Default */)
 * ```
 *
 * See `samples` for more configuration and usage examples.
 *
 * @see [Tezos.Builder]
 * @see [Tezos.Default]
 */
public class Tezos internal constructor(
    @InternalTezosSdkApi public val dependencyRegistry: DependencyRegistry,
    @InternalTezosSdkApi public val moduleRegistry: ModuleRegistry,
){
    private object Static {
        var defaultTezos: Tezos by default { Tezos() }

        val defaultCryptoProvider: CryptoProvider
            get() = cryptoProviders.firstOrNull() ?: failWithDependencyNotFound("CryptoProvider", "tezos-crypto")

        private val cryptoProviders: List<CryptoProvider> = CryptoProvider::class.java.let {
            ServiceLoader.load(it, it.classLoader).toList()
        }
    }

    /**
     * Builder of [Tezos].
     */
    public class Builder {

        /**
         * If `true`, the instance created with this builder will be registered as [Tezos.Default]. Otherwise, nothing will happen.
         * Set to `false` by default.
         *
         * NOTE: If another instance has previously been registered, the new instance will overwrite it.
         */
        public var isDefault: Boolean = false

        /**
         * The [CryptoProvider] implementation that should be used in the context of the [Tezos] instance created by this builder.
         * If not set during the builder configuration, the first implementation that can be found in the dependencies will be used.
         *
         * NOTE: It is recommended that you always set the [cryptoProvider] explicitly in a production environment
         * to ensure the correct implementation is being used.
         */
        public var cryptoProvider: CryptoProvider by default { Static.defaultCryptoProvider }

        /**
         * Installs a specific [builder] and optionally [configure]s it.
         */
        public inline fun <reified T : TezosModule, B : TezosModule.Builder<T>> install(builder: B, noinline configure: B.() -> Unit = {}): Builder =
            apply { install(T::class, builder, configure) }

        private val moduleBuilders: MutableMap<KClass<out TezosModule>, TezosModule.Builder<*>> = mutableMapOf()

        @PublishedApi
        internal fun <T : TezosModule, B : TezosModule.Builder<T>> install(moduleClass: KClass<T>, builder: B, configure: B.() -> Unit) {
            moduleBuilders[moduleClass] = builder.apply(configure)
        }

        /**
         * Constructs a new instance of [Tezos].
         */
        public fun build(): Tezos {
            val dependencyRegistry = DependencyRegistry(cryptoProvider)
            val moduleRegistry = ModuleRegistry(builders = moduleBuilders)

            return Tezos(dependencyRegistry, moduleRegistry).also {
                if (isDefault) {
                    Static.defaultTezos = it
                }
            }
        }

        public companion object {}
    }

    public companion object {
        /**
         * The default [Tezos] instance that will be implicitly used throughout the library.
         * Can be overwritten during the instance build process with the [Tezos.Builder.isDefault] flag.
         */
        public val Default: Tezos
            get() = Static.defaultTezos
    }
}

/**
 * Constructs a new instance of [Tezos] and optionally [configure]s it.
 *
 * See `samples/src/test/kotlin/Basic` for a sample usage.
 *
 * @see [Tezos.Builder]
 */
public inline fun Tezos(configure: Tezos.Builder.() -> Unit = {}): Tezos = Tezos.Builder().apply(configure).build()
