package it.airgap.tezos.core.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.fromString
import it.airgap.tezos.core.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.core.type.encoded.Ed25519PublicKey
import it.airgap.tezos.core.type.encoded.P256PublicKey
import it.airgap.tezos.core.type.encoded.PublicKeyEncoded
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKey
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringToPublicKeyConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var stringToPublicKeyConverter: StringToPublicKeyConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        stringToPublicKeyConverter = StringToPublicKeyConverter()

        every { dependencyRegistry.stringToPublicKeyConverter } returns stringToPublicKeyConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert string to PublicKeyEncoded`() {
        publicKeysWithStrings.forEach {
            assertEquals(it.first, stringToPublicKeyConverter.convert(it.second).toEncoded())
            assertEquals(it.first, PublicKeyEncoded.fromString(it.second))
            assertEquals(it.first, PublicKeyEncoded.fromString(it.second, stringToPublicKeyConverter))
        }
    }

    @Test
    fun `should fail to convert invalid string to PublicKeyEncoded`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { stringToPublicKeyConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { PublicKeyEncoded.fromString(it) }
            assertFailsWith<IllegalArgumentException> { PublicKeyEncoded.fromString(it, stringToPublicKeyConverter) }
        }
    }

    private val publicKeysWithStrings: List<Pair<PublicKeyEncoded, String>>
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