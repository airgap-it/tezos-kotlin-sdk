import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.TezosCore
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.di.DependencyRegistry

// -- Tezos --

internal fun mockTezos(cryptoProvider: CryptoProvider = mockk(relaxed = true)): Tezos =
    mockkClass(Tezos::class).also {
        val dependencyRegistry = DependencyRegistry(cryptoProvider)
        val coreDependencyRegistry = CoreDependencyRegistry(dependencyRegistry)

        val core = TezosCore(coreDependencyRegistry)

        every { it.dependencyRegistry } returns dependencyRegistry
        every { it.core() } returns core
    }