package it.airgap.tezos.core.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.fromString
import it.airgap.tezos.core.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.encoded.P256PublicKeyHash
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKeyHash
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringToImplicitAddressConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var stringToImplicitAddressConverter: StringToImplicitAddressConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        stringToImplicitAddressConverter = StringToImplicitAddressConverter()

        every { dependencyRegistry.stringToImplicitAddressConverter } returns stringToImplicitAddressConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert string to ImplicitAddress`() {
        addressesWithStrings.forEach {
            assertEquals(it.first, stringToImplicitAddressConverter.convert(it.second).toEncoded())
            assertEquals(it.first, ImplicitAddress.fromString(it.second))
            assertEquals(it.first, ImplicitAddress.fromString(it.second, stringToImplicitAddressConverter))
        }
    }

    @Test
    fun `should fail to convert invalid string to ImplicitAddress`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { stringToImplicitAddressConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { ImplicitAddress.fromString(it) }
            assertFailsWith<IllegalArgumentException> { ImplicitAddress.fromString(it, stringToImplicitAddressConverter) }
        }
    }

    private val addressesWithStrings: List<Pair<ImplicitAddress, String>>
        get() = listOf(
            Ed25519PublicKeyHash("tz1YLntubWUxHSQHiB8YnUspotACx6LeCZxk") to "tz1YLntubWUxHSQHiB8YnUspotACx6LeCZxk",
            Secp256K1PublicKeyHash("tz2VyE3NHiJL5bFjjJHGsbbP4dTZ6rFzHNiw") to "tz2VyE3NHiJL5bFjjJHGsbbP4dTZ6rFzHNiw",
            P256PublicKeyHash("tz3SeX5u65y4yHLVzwTf6NQkqViYanxdGNaF") to "tz3SeX5u65y4yHLVzwTf6NQkqViYanxdGNaF",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "00",
            "YLntubWUxHSQHiB8YnUspotACx6LeCZxk",
            "tz2VyE3NHiJL5bFjjJHGsbbP4dTZ6rFzHN",
            "tzSeX5u65y4yHLVzwTf6NQkqViYanxdGNaF",
            "tz3SeX5u65y4yHLVzwTf6NQkqViYanxdGNaF74Bz",
        )
}