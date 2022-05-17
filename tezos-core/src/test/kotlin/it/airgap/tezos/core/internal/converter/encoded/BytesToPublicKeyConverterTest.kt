package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromBytes
import it.airgap.tezos.core.internal.utils.asHexString
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

class BytesToPublicKeyConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var bytesToPublicKeyConverter: BytesToPublicKeyConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        bytesToPublicKeyConverter = BytesToPublicKeyConverter(tezos.dependencyRegistry.base58Check)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert bytes to PublicKeyEncoded`() {
        publicKeysWithBytes.forEach {
            assertEquals(it.first, bytesToPublicKeyConverter.convert(it.second))
            assertEquals(it.first, PublicKey.fromBytes(it.second, tezos))
            assertEquals(it.first, PublicKey.fromBytes(it.second, bytesToPublicKeyConverter))
        }
    }

    @Test
    fun `should fail to convert invalid bytes to PublicKeyEncoded`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { bytesToPublicKeyConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { PublicKey.fromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { PublicKey.fromBytes(it, bytesToPublicKeyConverter) }
        }
    }

    private val publicKeysWithBytes: List<Pair<PublicKey, ByteArray>>
        get() = listOf(
            Ed25519PublicKey("edpkuc5nnH2rhy3HpoPTeAfLLGTMKX7PvCaBgrudEBFBQNWWSpc1vk") to "0d0f25d97ed5fe9bff28a71b682400b1f83348e9a4669e6fae70d840cf794d9e222243a7".asHexString().toByteArray(),
            Ed25519PublicKey("edpkuc5nnH2rhy3HpoPTeAfLLGTMKX7PvCaBgrudEBFBQNWWSpc1vk") to "7ed5fe9bff28a71b682400b1f83348e9a4669e6fae70d840cf794d9e222243a7".asHexString().toByteArray(),
            Secp256K1PublicKey("sppk9yNXbVZv1ByJq5ENkKszqiGXbvhVYymJM8DXPEqXTmKssW6nBP2") to "03fee2564a0e4a6f8fa69a58e6ad048e331da54c74f77fb62d81ef5bc14541974882d4ff17".asHexString().toByteArray(),
            P256PublicKey("p2pk7NgYhjSbWiNiii7kuvN6pRSr5EtUPbZmFxLnCt1CfNtZQjtuvdq") to "03b28b7f2927669057825f9c63f397359d0cb9e6dfd6df6a41b0b7cb3591f4f5d0ee1c3053".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "0d0f25d97ed5fe9bff28a71b682400b1f83348e9a4669e6fae70d840cf794d",
            "03fee25a58e6ad048e331da54c74f77fb62d81ef5bc14541974882d4ff17",
            "03b28b7f2927669057825f9c63f397359d0cb9e6dfd6df6a41b0b7cb3591f4f5d0ee1c305353fa",
        ).map { it.asHexString().toByteArray() }
}