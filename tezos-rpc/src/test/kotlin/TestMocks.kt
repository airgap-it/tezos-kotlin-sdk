import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.TezosCoreModule
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.michelson.internal.TezosMichelsonModule
import it.airgap.tezos.michelson.internal.michelson
import it.airgap.tezos.operation.internal.TezosOperationModule
import it.airgap.tezos.operation.internal.operation
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.internal.TezosRpcModule
import it.airgap.tezos.rpc.internal.rpc
import java.security.MessageDigest

// -- Tezos --

internal fun mockTezos(cryptoProvider: CryptoProvider? = null, httpClientProvider: HttpClientProvider? = null): Tezos =
    mockkClass(Tezos::class).also {
        val cryptoProvider = cryptoProvider ?: mockk<CryptoProvider>(relaxed = true).also {
            every { it.sha256(any()) } answers {
                val messageDigest = MessageDigest.getInstance("SHA-256")
                messageDigest.digest(firstArg())
            }
        }

        val dependencyRegistry = DependencyRegistry(cryptoProvider)

        val core = TezosCoreModule.Builder().build(dependencyRegistry, emptyList())
        val michelson = TezosMichelsonModule.Builder().build(dependencyRegistry, listOf(core))
        val operation = TezosOperationModule.Builder().build(dependencyRegistry, listOf(core, michelson))
        val rpc = TezosRpcModule.Builder().build(dependencyRegistry, emptyList())

        every { it.dependencyRegistry } returns dependencyRegistry
        every { it.core() } returns core
        every { it.michelson() } returns michelson
        every { it.operation() } returns operation
        every { it.rpc() } returns rpc
    }