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
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.P256PublicKeyHash
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKeyHash
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BytesToImplicitAddressConverterTest {

    @MockK
    private lateinit var crypto: Crypto

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var bytesToImplicitAddressConverter: BytesToImplicitAddressConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        bytesToImplicitAddressConverter = BytesToImplicitAddressConverter(base58Check)

        every { dependencyRegistry.bytesToImplicitAddressConverter } returns bytesToImplicitAddressConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert bytes to ImplicitAddress`() {
        addressesWithBytes.forEach {
            assertEquals(it.first, bytesToImplicitAddressConverter.convert(it.second))
            assertEquals(it.first, ImplicitAddress.fromBytes(it.second))
            assertEquals(it.first, ImplicitAddress.fromBytes(it.second, bytesToImplicitAddressConverter))
        }
    }

    @Test
    fun `should fail to convert invalid bytes to ImplicitAddress`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { bytesToImplicitAddressConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { ImplicitAddress.fromBytes(it) }
            assertFailsWith<IllegalArgumentException> { ImplicitAddress.fromBytes(it, bytesToImplicitAddressConverter) }
        }
    }

    private val addressesWithBytes: List<Pair<ImplicitAddress<*>, ByteArray>>
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