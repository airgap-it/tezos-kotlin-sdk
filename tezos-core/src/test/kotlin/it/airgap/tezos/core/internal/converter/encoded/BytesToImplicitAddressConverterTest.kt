package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromBytes
import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.P256PublicKeyHash
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKeyHash
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BytesToImplicitAddressConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var bytesToImplicitAddressConverter: BytesToImplicitAddressConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        bytesToImplicitAddressConverter = BytesToImplicitAddressConverter(tezos.coreModule.dependencyRegistry.bytesToPublicKeyHashConverter)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert bytes to ImplicitAddress`() = withTezosContext {
        addressesWithBytes.forEach {
            assertEquals(it.first, bytesToImplicitAddressConverter.convert(it.second))
            assertEquals(it.first, ImplicitAddress.fromBytes(it.second, tezos))
            assertEquals(it.first, ImplicitAddress.fromBytes(it.second, bytesToImplicitAddressConverter))
        }
    }

    @Test
    fun `should fail to convert invalid bytes to ImplicitAddress`() = withTezosContext {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { bytesToImplicitAddressConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { ImplicitAddress.fromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { ImplicitAddress.fromBytes(it, bytesToImplicitAddressConverter) }
        }
    }

    private val addressesWithBytes: List<Pair<ImplicitAddress, ByteArray>>
        get() = listOf(
            Ed25519PublicKeyHash("tz1YLntubWUxHSQHiB8YnUspotACx6LeCZxk") to "06a19f8b570c01097efde52239e836dc76c790bb9d38e5".asHexString().toByteArray(),
            Ed25519PublicKeyHash("tz1YLntubWUxHSQHiB8YnUspotACx6LeCZxk") to "8b570c01097efde52239e836dc76c790bb9d38e5".asHexString().toByteArray(),
            Secp256K1PublicKeyHash("tz2VyE3NHiJL5bFjjJHGsbbP4dTZ6rFzHNiw") to "06a1a1ed89ee2a6ca819ca479a1cbc5d199b546d95f67c".asHexString().toByteArray(),
            P256PublicKeyHash("tz3SeX5u65y4yHLVzwTf6NQkqViYanxdGNaF") to "06a1a4454f2c27bec7d2275362220b5ffa2052fbb2ee1f".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "06a19f8b570c01097efde52239e836dc76c790",
            "06a1aca819ca479a1cbc5d199b546d95f67c",
            "4f2c27bec7d2275362220b5ffa2052fbb2ee1f",
        ).map { it.asHexString().toByteArray() }
}