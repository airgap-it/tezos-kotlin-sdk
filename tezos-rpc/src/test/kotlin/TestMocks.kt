import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.TezosCoreModule
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.michelson.internal.TezosMichelsonModule
import it.airgap.tezos.operation.internal.TezosOperationModule
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.internal.TezosRpcModule
import java.security.MessageDigest

// -- Tezos --

internal fun mockTezos(cryptoProvider: CryptoProvider? = null, httpClientProvider: HttpClientProvider = mockk(relaxed = true)): Tezos =
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
                TezosCoreModule::class to TezosCoreModule.Builder(),
                TezosMichelsonModule::class to TezosMichelsonModule.Builder(),
                TezosOperationModule::class to TezosOperationModule.Builder(),
                TezosRpcModule::class to TezosRpcModule.Builder().apply { this.httpClientProvider = httpClientProvider },
            ),
        )

        every { it.dependencyRegistry } returns dependencyRegistry
        every { it.moduleRegistry } returns moduleRegistry
    }