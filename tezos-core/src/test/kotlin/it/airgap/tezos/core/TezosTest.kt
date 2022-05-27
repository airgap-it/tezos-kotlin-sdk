package it.airgap.tezos.core

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.core.internal.module.TezosModule
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class TezosTest {

    @Test
    fun `should build Tezos and set it as default instance if specified`() {
        val cryptoProvider = mockk<CryptoProvider>(relaxed = true)
        val cryptoProviderServiceLoader = mockk<ServiceLoader<CryptoProvider>>(relaxed = true)

        mockkStatic(ServiceLoader::class)
        every { ServiceLoader.load(any<Class<CryptoProvider>>(), any()) } returns cryptoProviderServiceLoader
        every { cryptoProviderServiceLoader.iterator() } returns mutableListOf(cryptoProvider).iterator()

        val tezos = Tezos { isDefault = true }
        assertEquals(tezos, Tezos.Default)
    }

    @Test
    fun `should build Tezos and not set it as default instance if not specified`() {
        val cryptoProvider = mockk<CryptoProvider>(relaxed = true)
        val cryptoProviderServiceLoader = mockk<ServiceLoader<CryptoProvider>>(relaxed = true)

        mockkStatic(ServiceLoader::class)
        every { ServiceLoader.load(any<Class<CryptoProvider>>(), any()) } returns cryptoProviderServiceLoader
        every { cryptoProviderServiceLoader.iterator() } returns mutableListOf(cryptoProvider).iterator()

        val tezos = Tezos()
        assertNotEquals(tezos, Tezos.Default)
    }

    @Test
    fun `should build Tezos with provided CryptoProvider`() {
        val cryptoProvider = mockk<CryptoProvider>(relaxed = true)

        val tezos = Tezos { this.cryptoProvider = cryptoProvider }
        tezos.dependencyRegistry.crypto.hashSha256(byteArrayOf())

        verify(exactly = 1) { cryptoProvider.sha256(byteArrayOf()) }
    }

    @Test
    fun `should build Tezos with provided module builders`() {
        val cryptoProvider = mockk<CryptoProvider>(relaxed = true)
        val cryptoProviderServiceLoader = mockk<ServiceLoader<CryptoProvider>>(relaxed = true)

        mockkStatic(ServiceLoader::class)
        every { ServiceLoader.load(any<Class<CryptoProvider>>(), any()) } returns cryptoProviderServiceLoader
        every { cryptoProviderServiceLoader.iterator() } returns mutableListOf(cryptoProvider).iterator()

        val tezos = Tezos {
            install(TestModule.Builder())
        }

        var defaultBuilderCalled = false
        tezos.moduleRegistry.module(tezos.dependencyRegistry) {
            defaultBuilderCalled = true
            TestModule.Builder()
        }

        assertFalse(defaultBuilderCalled)
    }

    private class TestModule : TezosModule {
        class Builder : TezosModule.Builder<TestModule> {
            @InternalTezosSdkApi
            override fun build(dependencyRegistry: DependencyRegistry, moduleRegistry: ModuleRegistry): TestModule =
                TestModule()
        }
    }
}