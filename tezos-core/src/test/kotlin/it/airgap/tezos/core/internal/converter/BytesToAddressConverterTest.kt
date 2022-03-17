package it.airgap.tezos.core.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.fromBytes
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.type.encoded.*
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BytesToAddressConverterTest {

    @MockK
    private lateinit var crypto: Crypto

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var bytesToAddressConverter: BytesToAddressConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        bytesToAddressConverter = BytesToAddressConverter(base58Check)

        every { dependencyRegistry.bytesToAddressConverter } returns bytesToAddressConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert bytes to Address`() {
        addressesWithBytes.forEach {
            assertEquals(it.first, bytesToAddressConverter.convert(it.second).encoded)
            assertEquals(it.first, Address.fromBytes(it.second))
            assertEquals(it.first, Address.fromBytes(it.second, bytesToAddressConverter))
        }
    }

    @Test
    fun `should fail to convert invalid bytes to Address`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { bytesToAddressConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { Address.fromBytes(it) }
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