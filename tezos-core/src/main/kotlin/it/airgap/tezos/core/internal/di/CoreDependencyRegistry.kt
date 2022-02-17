package it.airgap.tezos.core.internal.di

import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.delegate.lazyWeak
import kotlin.reflect.KClass

internal class CoreDependencyRegistry(
    cryptoProvider: CryptoProvider,
    scopedDependencyRegistryFactories: List<DependencyRegistryFactory> = emptyList(),
) : DependencyRegistry {

    // -- scoped --

    private val _scoped: MutableMap<String, DependencyRegistry> = mutableMapOf()
    override val scoped: Map<String, DependencyRegistry>
        get() = _scoped

    override fun addScoped(scoped: DependencyRegistry) {
        val key = scoped::class.qualifiedName ?: return
        _scoped[key] = scoped
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : DependencyRegistry> findScoped(targetClass: KClass<T>): T? {
        val key = targetClass.qualifiedName ?: return null
        return scoped[key] as T?
    }

    init {
        scopedDependencyRegistryFactories.forEach { addScoped(it(this)) }
    }

    // -- base58 --

    override val base58: Base58 by lazyWeak { Base58() }
    override val base58Check: Base58Check by lazyWeak { Base58Check(base58, crypto) }

    // -- crypto --

    override val crypto: Crypto by lazyWeak { Crypto(cryptoProvider) }
}