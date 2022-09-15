import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import it.airgap.tezos.contract.ContractModule
import it.airgap.tezos.contract.internal.TezosContractModule
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.module.ModuleRegistry
import it.airgap.tezos.michelson.MichelsonModule
import it.airgap.tezos.michelson.internal.TezosMichelsonModule
import it.airgap.tezos.rpc.RpcModule
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
                TezosMichelsonModule::class to MichelsonModule,
                TezosRpcModule::class to RpcModule.apply { this.httpClientProvider = httpClientProvider },
                TezosContractModule::class to ContractModule,
            ),
        )

        every { it.dependencyRegistry } returns dependencyRegistry
        every { it.moduleRegistry } returns moduleRegistry
    }