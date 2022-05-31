package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromString
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.Ed25519BlindedPublicKeyHash
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringToBlindedPublicKeyHashConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var stringToBlindedPublicKeyHashConverter: StringToBlindedPublicKeyHashConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        stringToBlindedPublicKeyHashConverter = StringToBlindedPublicKeyHashConverter()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert string to ImplicitAddress`() = withTezosContext {
        addressesWithStrings.forEach {
            assertEquals(it.first, stringToBlindedPublicKeyHashConverter.convert(it.second))
            assertEquals(it.first, BlindedPublicKeyHash.fromString(it.second, tezos))
            assertEquals(it.first, BlindedPublicKeyHash.fromString(it.second, stringToBlindedPublicKeyHashConverter))
        }
    }

    @Test
    fun `should fail to convert invalid string to ImplicitAddress`() = withTezosContext {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { stringToBlindedPublicKeyHashConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { BlindedPublicKeyHash.fromString(it, tezos) }
            assertFailsWith<IllegalArgumentException> { BlindedPublicKeyHash.fromString(it, stringToBlindedPublicKeyHashConverter) }
        }
    }

    private val addressesWithStrings: List<Pair<BlindedPublicKeyHash, String>>
        get() = listOf(
            Ed25519BlindedPublicKeyHash("btz1ecEEauoGbHrbyzVTVNZhK4B8YHUfVgkzj") to "btz1ecEEauoGbHrbyzVTVNZhK4B8YHUfVgkzj",
            Ed25519BlindedPublicKeyHash("btz1cnHeSrGs8LzwS3tJ8GKXfmjgtN2dRfRMW") to "btz1cnHeSrGs8LzwS3tJ8GKXfmjgtN2dRfRMW",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "00",
            "ecEEauoGbHrbyzVTVNZhK4B8YHUfVgkzj",
            "btz1ecEEauoGbHrbyzVTVNZhK4B8Y",
            "btz1cnHeSrGs8LzwS3tJ8GKXfmjgtN2dRfRMW1G",
        )
}