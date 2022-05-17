import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.TezosCoreModule
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.di.DependencyRegistry
import java.security.MessageDigest

// -- Tezos --

internal fun mockTezos(cryptoProvider: CryptoProvider? = null): Tezos =
    mockkClass(Tezos::class).also {
        val cryptoProvider = cryptoProvider ?: mockk<CryptoProvider>(relaxed = true).also {
            every { it.sha256(any()) } answers {
                val messageDigest = MessageDigest.getInstance("SHA-256")
                messageDigest.digest(firstArg())
            }
        }

        val dependencyRegistry = DependencyRegistry(cryptoProvider)
        val coreDependencyRegistry = CoreDependencyRegistry(dependencyRegistry)

        val core = TezosCoreModule(coreDependencyRegistry)

        every { it.dependencyRegistry } returns dependencyRegistry
        every { it.core() } returns core
    }