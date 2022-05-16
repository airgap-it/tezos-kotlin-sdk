import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.TezosCore
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.michelson.internal.TezosMichelson
import it.airgap.tezos.michelson.internal.di.MichelsonDependencyRegistry
import it.airgap.tezos.michelson.internal.michelson

// -- Tezos --

internal fun mockTezos(cryptoProvider: CryptoProvider = mockk(relaxed = true)): Tezos =
    mockkClass(Tezos::class).also {
        val dependencyRegistry = DependencyRegistry(cryptoProvider)
        val coreDependencyRegistry = CoreDependencyRegistry(dependencyRegistry)
        val michelsonDependencyRegistry = MichelsonDependencyRegistry(coreDependencyRegistry)

        val core = TezosCore(coreDependencyRegistry)
        val michelson = TezosMichelson(michelsonDependencyRegistry)

        every { it.core() } returns core
        every { it.michelson() } returns michelson
    }