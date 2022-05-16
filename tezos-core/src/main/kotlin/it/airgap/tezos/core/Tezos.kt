package it.airgap.tezos.core

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.TezosInstance
import it.airgap.tezos.core.internal.TezosStatic
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.DependencyRegistry
import kotlin.reflect.KClass

public abstract class Tezos {
    internal abstract val dependencyRegistry: DependencyRegistry

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

    public class Builder(private val cryptoProvider: CryptoProvider = TezosStatic.defaultCryptoProvider) {
        public var default: Boolean = false

        public fun build(): Tezos {
            val dependencyRegistry = DependencyRegistry(cryptoProvider)

            return TezosInstance(dependencyRegistry).also {
                if (default) {
                    TezosStatic.defaultTezos = it
                }
            }
        }
    }

    public companion object {
        public val Default: Tezos
            get() = TezosStatic.defaultTezos
    }
}

public fun Tezos(
    cryptoProvider: CryptoProvider = TezosStatic.defaultCryptoProvider,
    builder: Tezos.Builder.() -> Unit = {}
): Tezos = Tezos.Builder(cryptoProvider).apply(builder).build()
