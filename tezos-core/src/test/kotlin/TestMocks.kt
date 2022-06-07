import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import it.airgap.tezos.core.CoreModule
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.TezosCoreModule
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
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
        val moduleRegistry = ModuleRegistry(
            builders = mapOf(
                TezosCoreModule::class to CoreModule,
            ),
        )

        every { it.dependencyRegistry } returns dependencyRegistry
        every { it.moduleRegistry } returns moduleRegistry
    }