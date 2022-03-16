package it.airgap.tezos.core.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.fromString
import it.airgap.tezos.core.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.core.type.encoded.*
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringToAddressConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var stringToAddressConverter: StringToAddressConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        stringToAddressConverter = StringToAddressConverter()

        every { dependencyRegistry.stringToAddressConverter } returns stringToAddressConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert string to Address`() {
        addressesWithStrings.forEach {
            assertEquals(it.first, stringToAddressConverter.convert(it.second).toEncoded())
            assertEquals(it.first, Address.fromString(it.second))
            assertEquals(it.first, Address.fromString(it.second, stringToAddressConverter))
        }
    }

    @Test
    fun `should fail to convert invalid string to Address`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { stringToAddressConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { Address.fromString(it) }
            assertFailsWith<IllegalArgumentException> { Address.fromString(it, stringToAddressConverter) }
        }
    }

    private val addressesWithStrings: List<Pair<Address, String>>
        get() = listOf(
            Ed25519PublicKeyHash("tz1YLntubWUxHSQHiB8YnUspotACx6LeCZxk") to "tz1YLntubWUxHSQHiB8YnUspotACx6LeCZxk",
            Secp256K1PublicKeyHash("tz2VyE3NHiJL5bFjjJHGsbbP4dTZ6rFzHNiw") to "tz2VyE3NHiJL5bFjjJHGsbbP4dTZ6rFzHNiw",
            P256PublicKeyHash("tz3SeX5u65y4yHLVzwTf6NQkqViYanxdGNaF") to "tz3SeX5u65y4yHLVzwTf6NQkqViYanxdGNaF",
            ContractHash("KT1QME2yHnTBFS6qZ9Pbn68koFvbFBmvgjAZ") to "KT1QME2yHnTBFS6qZ9Pbn68koFvbFBmvgjAZ",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "00",
            "YLntubWUxHSQHiB8YnUspotACx6LeCZxk",
            "tz2VyE3NHiJL5bFjjJHGsbbP4dTZ6rFzHN",
            "tzSeX5u65y4yHLVzwTf6NQkqViYanxdGNaF",
            "KT1QME2yHnTBFS6qZ9Pbn68koFvbFBmvgjAZb7A",
        )
}