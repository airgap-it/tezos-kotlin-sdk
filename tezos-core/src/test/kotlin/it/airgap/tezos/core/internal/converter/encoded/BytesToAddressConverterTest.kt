package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromBytes
import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.type.encoded.*
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BytesToAddressConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var bytesToAddressConverter: BytesToAddressConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        bytesToAddressConverter = BytesToAddressConverter(tezos.dependencyRegistry.base58Check)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert bytes to Address`() = withTezosContext {
        addressesWithBytes.forEach {
            assertEquals(it.first, bytesToAddressConverter.convert(it.second))
            assertEquals(it.first, Address.fromBytes(it.second, tezos))
            assertEquals(it.first, Address.fromBytes(it.second, bytesToAddressConverter))
        }
    }

    @Test
    fun `should fail to convert invalid bytes to Address`() = withTezosContext {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { bytesToAddressConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { Address.fromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { Address.fromBytes(it, bytesToAddressConverter) }
        }
    }

    private val addressesWithBytes: List<Pair<Address, ByteArray>>
        get() = listOf(
            Ed25519PublicKeyHash("tz1YLntubWUxHSQHiB8YnUspotACx6LeCZxk") to "06a19f8b570c01097efde52239e836dc76c790bb9d38e5".asHexString().toByteArray(),
            Ed25519PublicKeyHash("tz1YLntubWUxHSQHiB8YnUspotACx6LeCZxk") to "8b570c01097efde52239e836dc76c790bb9d38e5".asHexString().toByteArray(),
            Secp256K1PublicKeyHash("tz2VyE3NHiJL5bFjjJHGsbbP4dTZ6rFzHNiw") to "06a1a1ed89ee2a6ca819ca479a1cbc5d199b546d95f67c".asHexString().toByteArray(),
            P256PublicKeyHash("tz3SeX5u65y4yHLVzwTf6NQkqViYanxdGNaF") to "06a1a4454f2c27bec7d2275362220b5ffa2052fbb2ee1f".asHexString().toByteArray(),
            ContractHash("KT1QME2yHnTBFS6qZ9Pbn68koFvbFBmvgjAZ") to "025a79acf248ca860d1e02d338bc71927b7ffbc202d39d".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "06a19f8b570c01097efde52239e836dc76c790",
            "06a1aca819ca479a1cbc5d199b546d95f67c",
            "4f2c27bec7d2275362220b5ffa2052fbb2ee1f",
            "025a79acf248ca860d1e02d338bc71927b7ffbc202d39df6ab",
        ).map { it.asHexString().toByteArray() }
}