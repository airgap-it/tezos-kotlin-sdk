package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromString
import it.airgap.tezos.core.type.encoded.Ed25519PublicKey
import it.airgap.tezos.core.type.encoded.P256PublicKey
import it.airgap.tezos.core.type.encoded.PublicKey
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKey
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringToPublicKeyConverterTest {

    private lateinit var tezos: Tezos

    private lateinit var stringToPublicKeyConverter: StringToPublicKeyConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        stringToPublicKeyConverter = StringToPublicKeyConverter()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert string to PublicKeyEncoded`() {
        publicKeysWithStrings.forEach {
            assertEquals(it.first, stringToPublicKeyConverter.convert(it.second))
            assertEquals(it.first, PublicKey.fromString(it.second, tezos))
            assertEquals(it.first, PublicKey.fromString(it.second, stringToPublicKeyConverter))
        }
    }

    @Test
    fun `should fail to convert invalid string to PublicKeyEncoded`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { stringToPublicKeyConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { PublicKey.fromString(it, tezos) }
            assertFailsWith<IllegalArgumentException> { PublicKey.fromString(it, stringToPublicKeyConverter) }
        }
    }

    private val publicKeysWithStrings: List<Pair<PublicKey, String>>
        get() = listOf(
            Ed25519PublicKey("edpkuc5nnH2rhy3HpoPTeAfLLGTMKX7PvCaBgrudEBFBQNWWSpc1vk") to "edpkuc5nnH2rhy3HpoPTeAfLLGTMKX7PvCaBgrudEBFBQNWWSpc1vk",
            Secp256K1PublicKey("sppk9yNXbVZv1ByJq5ENkKszqiGXbvhVYymJM8DXPEqXTmKssW6nBP2") to "sppk9yNXbVZv1ByJq5ENkKszqiGXbvhVYymJM8DXPEqXTmKssW6nBP2",
            P256PublicKey("p2pk7NgYhjSbWiNiii7kuvN6pRSr5EtUPbZmFxLnCt1CfNtZQjtuvdq") to "p2pk7NgYhjSbWiNiii7kuvN6pRSr5EtUPbZmFxLnCt1CfNtZQjtuvdq",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "00",
            "edpkuc5nnH2rhy3HpoPTeAfLLGTMKX7PvCaBgrudEBFBQNWWS",
            "9yNXbVZv1ByJq5ENkKszqiGXbvhVYymJM8DXPEqXTmKssW6nBP2",
            "p2pk7NgYhjSbWiNiii7kuvN6pRSr5EtUPbZmFxLnCt1CfNtZQjtuvdqQrk",
        )
}