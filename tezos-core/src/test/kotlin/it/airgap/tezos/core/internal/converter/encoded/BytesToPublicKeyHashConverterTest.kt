package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromBytes
import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.encoded.P256PublicKeyHash
import it.airgap.tezos.core.type.encoded.PublicKeyHash
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKeyHash
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BytesToPublicKeyHashConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var bytesToPublicKeyHashConverter: BytesToPublicKeyHashConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        bytesToPublicKeyHashConverter = BytesToPublicKeyHashConverter(tezos.dependencyRegistry.base58Check)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert bytes to ImplicitAddress`() = withTezosContext {
        addressesWithBytes.forEach {
            assertEquals(it.first, bytesToPublicKeyHashConverter.convert(it.second))
            assertEquals(it.first, PublicKeyHash.fromBytes(it.second, tezos))
            assertEquals(it.first, PublicKeyHash.fromBytes(it.second, bytesToPublicKeyHashConverter))
        }
    }

    @Test
    fun `should fail to convert invalid bytes to ImplicitAddress`() = withTezosContext {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { bytesToPublicKeyHashConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { PublicKeyHash.fromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { PublicKeyHash.fromBytes(it, bytesToPublicKeyHashConverter) }
        }
    }

    private val addressesWithBytes: List<Pair<PublicKeyHash, ByteArray>>
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