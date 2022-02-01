import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkObject
import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.core.internal.di.DependencyRegistry

// -- static --

internal fun mockTezosSdk(dependencyRegistry: DependencyRegistry = mockk(relaxed = true)): TezosSdk =
    mockkClass(TezosSdk::class).also {
        mockkObject(TezosSdk)
        every { TezosSdk.instance } returns it

        every { it.dependencyRegistry } returns dependencyRegistry
    }