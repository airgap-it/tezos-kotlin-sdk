package it.airgap.tezos.core

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.delegate.default
import it.airgap.tezos.core.internal.di.DependencyRegistry
import java.util.*
import kotlin.reflect.KClass

public class Tezos internal constructor(@InternalTezosSdkApi public val dependencyRegistry: DependencyRegistry){
    private val dynamicModules: MutableMap<String, DynamicModule> = mutableMapOf()

    @InternalTezosSdkApi
    public fun registerDynamicModule(module: DynamicModule) {
        val key = module::class.qualifiedName ?: return
        dynamicModules[key] = module
    }

    @InternalTezosSdkApi
    public inline fun <reified T : DynamicModule> findModule(): T? = findModule(T::class)

    @PublishedApi
    @Suppress("UNCHECKED_CAST")
    internal fun <T : DynamicModule> findModule(targetClass: KClass<T>): T? {
        val key = targetClass.qualifiedName ?: return null
        return dynamicModules[key] as T?
    }

    @InternalTezosSdkApi
    public interface DynamicModule

    internal object Static {
        var defaultTezos: Tezos by default { Tezos { cryptoProvider = defaultCryptoProvider } }

        val defaultCryptoProvider: CryptoProvider
            get() = cryptoProviders.firstOrNull() ?: failWithDependencyNotFound("CryptoProvider", "tezos-crypto")

        private val cryptoProviders: List<CryptoProvider> = CryptoProvider::class.java.let {
            ServiceLoader.load(it, it.classLoader).toList()
        }

        private fun failWithDependencyNotFound(name: String, module: String): Nothing =
            error("Failed to find $name implementation in the classpath. Consider adding a `$module` dependency or use a manually created Tezos instance.")
    }

    public class Builder {
        public var default: Boolean = false
        public var cryptoProvider: CryptoProvider by default { Static.defaultCryptoProvider }

        public fun build(): Tezos {
            val dependencyRegistry = DependencyRegistry(cryptoProvider)

            return Tezos(dependencyRegistry).also {
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

public fun Tezos(builder: Tezos.Builder.() -> Unit = {}): Tezos = Tezos.Builder().apply(builder).build()
