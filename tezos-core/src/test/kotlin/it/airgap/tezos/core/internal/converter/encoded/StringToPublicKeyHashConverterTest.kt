package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.type.encoded.*
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringToPublicKeyHashConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var stringToPublicKeyHashConverter: StringToPublicKeyHashConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        stringToPublicKeyHashConverter = StringToPublicKeyHashConverter()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert string to ImplicitAddress`() = withTezosContext {
        addressesWithStrings.forEach {
            assertEquals(it.first, stringToPublicKeyHashConverter.convert(it.second))
            assertEquals(it.first, PublicKeyHash.fromString(it.second, tezos))
            assertEquals(it.first, PublicKeyHash.fromString(it.second, stringToPublicKeyHashConverter))
        }
    }

    @Test
    fun `should fail to convert invalid string to ImplicitAddress`() = withTezosContext {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { stringToPublicKeyHashConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { PublicKeyHash.fromString(it, tezos) }
            assertFailsWith<IllegalArgumentException> { PublicKeyHash.fromString(it, stringToPublicKeyHashConverter) }
        }
    }

    private val addressesWithStrings: List<Pair<PublicKeyHash, String>>
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