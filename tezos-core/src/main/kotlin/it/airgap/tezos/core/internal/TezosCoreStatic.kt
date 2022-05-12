package it.airgap.tezos.core.internal

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.delegate.default
import java.util.*

internal object TezosCoreStatic {
    var defaultTezos: Tezos by default { Tezos(defaultCryptoProvider) }

    val defaultCryptoProvider: CryptoProvider
        get() = cryptoProviders.firstOrNull() ?: failWithDependencyNotFound("CryptoProvider", "tezos-crypto")

    private val cryptoProviders: List<CryptoProvider> = CryptoProvider::class.java.let {
        ServiceLoader.load(it, it.classLoader).toList()
    }

    private fun failWithDependencyNotFound(name: String, module: String): Nothing =
        error("Failed to find $name implementation in the classpath. Consider adding a `$module` dependency or use a manually created Tezos instance.")
}